package com.example.app_common.base.viewmodel

import androidx.lifecycle.ViewModel
import com.example.app_common.base.exception.BaseError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel : ViewModel() {

    private val loadingState = MutableStateFlow(false)

    private val errorState = MutableStateFlow<BaseError?>(null)

    fun loadingState(): StateFlow<Boolean> = loadingState

    fun errorState(): StateFlow<BaseError?> = errorState

    fun handleError(error: BaseError?) {
        errorState.value = error
    }

    fun handleLoading(isLoading: Boolean) {
        loadingState.value = isLoading
    }
}
