package com.example.assetmanagementapp.ui.admin

import androidx.lifecycle.viewModelScope
import com.example.app_common.base.viewmodel.BaseViewModelV2
import com.example.app_common.extensions.bindLoading
import com.example.app_common.extensions.onError
import com.example.app_common.extensions.onSuccess
import com.example.assetmanagementapp.data.repositories.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : BaseViewModelV2<AdminState>() {
    override fun initState() = AdminState()


    fun dispatchResetSnackBar() {
        dispatchState(currentState.copy(stateCreateLiquidationSuccess = null))
    }

    fun createLiquidation() {
        roomRepository.createLiquidation().bindLoading(this).onSuccess {
            dispatchState(currentState.copy(stateCreateLiquidationSuccess = true))
        }.onError {
            dispatchState(currentState.copy(stateCreateLiquidationSuccess = false))
        }.launchIn(viewModelScope)
    }

}

data class AdminState(
    val any: Any = "",
    val stateCreateLiquidationSuccess: Boolean? = null
)
