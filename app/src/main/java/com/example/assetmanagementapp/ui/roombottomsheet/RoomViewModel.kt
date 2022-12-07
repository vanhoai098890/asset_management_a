package com.example.assetmanagementapp.ui.roombottomsheet

import androidx.lifecycle.viewModelScope
import com.example.app_common.base.viewmodel.BaseViewModelV2
import com.example.app_common.extensions.bindLoading
import com.example.app_common.extensions.onSuccess
import com.example.assetmanagementapp.data.remote.api.model.room.RoomItem
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAsset
import com.example.assetmanagementapp.data.repositories.DepartmentRepository
import com.example.assetmanagementapp.data.repositories.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class RoomViewModel @Inject constructor(
    private val departmentRepository: DepartmentRepository,
    private val deviceRepository: DeviceRepository
) : BaseViewModelV2<ProviderState>() {
    override fun initState(): ProviderState {
        return ProviderState()
    }

    init {
        getRoom()
        getStatus()
    }

    private fun getRoom() {
        departmentRepository.getRooms().bindLoading(this).onSuccess {
            dispatchState(currentState.copy(listRoom = it.data))
        }.launchIn(viewModelScope)
    }

    private fun getStatus() {
        deviceRepository.getListStatusType().bindLoading(this).onSuccess {
            dispatchState(currentState.copy(listStatus = it.data))
        }.launchIn(viewModelScope)
    }
}

data class ProviderState(
    val listRoom: List<RoomItem> = listOf(),
    val listStatus: List<TypeAsset> = listOf()
)
