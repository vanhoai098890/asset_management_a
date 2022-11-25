package com.example.assetmanagementapp.ui.qrcode

import androidx.lifecycle.viewModelScope
import com.example.app_common.base.viewmodel.BaseViewModelV2
import com.example.app_common.extensions.onSuccess
import com.example.assetmanagementapp.data.remote.api.model.detaildevice.CheckDeviceExistRequest
import com.example.assetmanagementapp.data.repositories.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class CustomBarcodeScannerViewModel @Inject constructor(
    private val assetRepository: DeviceRepository
) : BaseViewModelV2<BarcodeScannerState>() {

    override fun initState(): BarcodeScannerState = BarcodeScannerState()

    fun checkDeviceExist(deviceId: Int) {
        currentState.stateCurrentDeviceId = deviceId
        assetRepository.checkDeviceExist(CheckDeviceExistRequest(deviceId)).onSuccess {
            dispatchState(currentState.copy(stateDeviceExist = it.data))
        }.launchIn(viewModelScope)
    }

}

data class BarcodeScannerState(
    val stateDeviceExist: Boolean? = null,
    var stateCurrentDeviceId: Int = 0
)