package com.example.app_common.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.example.app_common.base.exception.BaseError
import com.example.app_common.base.exception.DefaultError
import com.example.app_common.base.viewmodel.BaseViewModel
import com.example.app_common.utils.LogUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import java.net.UnknownHostException

sealed class FlowResult<out T> {
    data class Success<T>(val value: T) : FlowResult<T>()
    data class Error(val baseError: BaseError) : FlowResult<Nothing>()
}

suspend inline fun <T> safeUseCase(
    crossinline block: suspend () -> T,
): FlowResult<T> = try {
    FlowResult.Success(block())
} catch (e: BaseError) {
    FlowResult.Error(e)
}

inline fun <T> safeFlow(
    crossinline block: suspend () -> T,
): Flow<FlowResult<T>> = flow {
    try {
        val repoResult = block()
        emit(FlowResult.Success(repoResult))
    } catch (e: BaseError) {
        emit(FlowResult.Error(e))
    } catch (e: UnknownHostException) {
        LogUtils.d(e.stackTraceToString())
    } catch (e: Exception) {
        LogUtils.e(e.stackTraceToString())
        emit(FlowResult.Error(DefaultError()))
    }
}

fun <T> Flow<FlowResult<T>>.onSuccess(action: suspend (T) -> Unit): Flow<FlowResult<T>> =
    transform { result ->
        if (result is FlowResult.Success<T>) {
            action(result.value)
        }
        return@transform emit(result)
    }

fun <T> Flow<FlowResult<T>>.mapSuccess(): Flow<T> =
    transform { result ->
        if (result is FlowResult.Success<T>) {
            emit(result.value)
        }
    }

fun <T> Flow<FlowResult<T>>.onError(
    action: suspend (BaseError) -> Unit = {}
): Flow<FlowResult<T>> =
    transform { result ->
        if (result is FlowResult.Error) {
            action(result.baseError)
        }
        return@transform emit(result)
    }

fun <H, X> Flow<H>.bindLoading(x: X): Flow<H> where  X : BaseViewModel =
    this.onStart {
        x.handleLoading(true)
    }.onCompletion {
        x.handleLoading(false)
    }

fun <H, X> Flow<FlowResult<H>>.bindError(x: X): Flow<FlowResult<H>> where  X : BaseViewModel =
    this.onError {
        x.handleError(it)
    }

/**
 * Count down using flow
 */
fun countDownTimer(totalSeconds: Int, stepSeconds: Long = 1000): Flow<Int> =
    (totalSeconds - 1 downTo 0).asFlow() // Emit total - 1 because the first was emitted onStart
        .onEach { delay(stepSeconds) } // Each second later emit a number
        .onStart { emit(totalSeconds) } // Emit total seconds immediately
        .conflate() // In case the operation onTick takes some time, conflate keeps the time ticking separately

fun <T> LiveData<T>.distinctUntilChangedByRef(): LiveData<T> =
    MediatorLiveData<T>().also { mediatorLiveData ->
        mediatorLiveData.addSource(this, object : Observer<T> {

            private var isInitialized = false
            private var previousValue: T? = null

            override fun onChanged(newValue: T) {
                val wasInitialzed = isInitialized
                if (!isInitialized) {
                    isInitialized = true

                }
                if (!wasInitialzed || newValue !== previousValue) {
                    previousValue = newValue
                    mediatorLiveData.postValue(newValue)
                }
            }

        })

    }
