package com.example.assetmanagementapp.ui.detaildevicedialog

import com.example.app_common.base.response.CommonResponse
import com.example.app_common.base.viewmodel.BaseViewModelV2
import com.example.app_common.extensions.FlowResult
import com.example.app_common.extensions.bindLoading
import com.example.app_common.extensions.onError
import com.example.app_common.extensions.onSuccess
import com.example.assetmanagementapp.data.remote.api.model.detaildevice.EditDeviceRequest
import com.example.assetmanagementapp.data.remote.api.model.favourite.StatusDevice
import com.example.assetmanagementapp.data.remote.api.model.room.RoomItem
import com.example.assetmanagementapp.data.repositories.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailDeviceDialogViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository
) : BaseViewModelV2<DetailDeviceDialogState>() {
    override fun initState() = DetailDeviceDialogState()

    fun dispatchStateRoom(roomItem: RoomItem) {
        dispatchState(currentState.copy(currentRoom = roomItem))
        if (
            (currentState.currentStatus == StatusDevice.LIQUIDATED.statusName
                    || currentState.currentStatus == StatusDevice.WARRANTY.statusName)
            && (currentState.currentRoom?.roomId ?: 0) != WAREHOUSE
        ) {
            dispatchState(currentState.copy(stateEnableButtonSubmit = false))
        } else {
            dispatchState(currentState.copy(stateEnableButtonSubmit = true))
        }
    }

    fun dispatchStateStatus(status: String) {
        dispatchState(currentState.copy(currentStatus = status))
        if (
            (currentState.currentStatus == StatusDevice.LIQUIDATED.statusName
                    || currentState.currentStatus == StatusDevice.WARRANTY.statusName)
            && (currentState.currentRoom?.roomId ?: 0) != WAREHOUSE
        ) {
            dispatchState(currentState.copy(stateEnableButtonSubmit = false))
        } else {
            dispatchState(currentState.copy(stateEnableButtonSubmit = true))
        }
    }

    fun submitEditDevice(): kotlinx.coroutines.flow.Flow<FlowResult<CommonResponse>>? {
        if (currentState.deviceId == 0) return null
        if (
            (currentState.currentStatus == StatusDevice.LIQUIDATED.statusName
                    || currentState.currentStatus == StatusDevice.WARRANTY.statusName)
            && (currentState.currentRoom?.roomId ?: 0) != WAREHOUSE
        ) {
            dispatchState(currentState.copy(stateEnableButtonSubmit = false))
            return null
        }
        currentState.currentRoom?.let { roomItem ->
            return deviceRepository.editDevice(
                EditDeviceRequest(
                    roomName = roomItem.roomName,
                    statusName = currentState.currentStatus,
                    deviceId = currentState.deviceId
                )
            )
                .bindLoading(this@DetailDeviceDialogViewModel)
                .onSuccess {
                    dispatchState(currentState.copy(stateSubmitSuccess = true))
                }
                .onError {
                    dispatchState(currentState.copy(stateSubmitSuccess = false))
                }
        }
        return null
    }

    fun resetStateSubmit() {
        dispatchState(currentState.copy(stateSubmitSuccess = null))
    }

    companion object {
        private const val WAREHOUSE = 18
    }

}

data class DetailDeviceDialogState(
    val any: Any = "",
    val currentRoom: RoomItem? = null,
    val currentStatus: String = "",
    val stateEnableButtonSubmit: Boolean = true,
    val stateSubmitSuccess: Boolean? = null,
    var deviceId: Int = 0
)
