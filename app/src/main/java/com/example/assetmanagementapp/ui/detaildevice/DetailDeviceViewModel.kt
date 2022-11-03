package com.example.assetmanagementapp.ui.detaildevice

import androidx.lifecycle.viewModelScope
import com.example.app_common.base.viewmodel.BaseViewModelV2
import com.example.app_common.extensions.bindLoading
import com.example.app_common.extensions.onError
import com.example.app_common.extensions.onSuccess
import com.example.assetmanagementapp.data.local.LoginSessionManager
import com.example.assetmanagementapp.data.repositories.DeviceRepository
import com.example.assetmanagementapp.data.repositories.FavouriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailDeviceViewModel @Inject constructor(
    private val loginSessionManager: LoginSessionManager,
    private val deviceRepository: DeviceRepository,
    private val favouriteRepository: FavouriteRepository
) : BaseViewModelV2<DetailDeviceState>() {
    override fun initState() = DetailDeviceState()

    private var isBlockSave: Boolean = false

    private val stateSetJobOnSaveDeviceRequest = HashSet<Int>()

    fun getDetailDevice() {
        currentState.deviceId?.let { deviceId ->
            deviceRepository.getDetailDevice(deviceId, loginSessionManager.getUsername())
                .bindLoading(this).onSuccess {
                    dispatchState(currentState.copy(deviceItem = it.data, stateVisibleMask = false, stateIsFavourite = it.data.isFavourite))
                }.onError {
                    dispatchState(currentState.copy(stateShowSomethingWrong = true))
                }.launchIn(viewModelScope)
        }
    }


    fun saveDevices(deviceId: Int, isSave: Boolean) {
        if (stateSetJobOnSaveDeviceRequest.contains(deviceId)) return
        synchronized(isBlockSave) {
            viewModelScope.launch {
                stateSetJobOnSaveDeviceRequest.add(deviceId)
                favouriteRepository.saveDevices(loginSessionManager.getUsername(), deviceId, isSave)
                    .onSuccess {
                        dispatchState(
                            currentState.copy(
                                stateIsShowSnackBar = isSave,
                                stateIsFavourite = isSave
                            )
                        )
                    }.onCompletion {
                        stateSetJobOnSaveDeviceRequest.remove(deviceId)
                    }.launchIn(viewModelScope)
            }
        }
    }

    fun dispatchResetSnackBar() {
        dispatchState(currentState.copy(stateIsShowSnackBar = null))
    }
}
