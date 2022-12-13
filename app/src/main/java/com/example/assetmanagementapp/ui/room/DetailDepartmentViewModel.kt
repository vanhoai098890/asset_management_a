package com.example.assetmanagementapp.ui.room

import androidx.lifecycle.viewModelScope
import com.example.app_common.base.viewmodel.BaseViewModelV2
import com.example.app_common.extensions.bindLoading
import com.example.app_common.extensions.onError
import com.example.app_common.extensions.onSuccess
import com.example.assetmanagementapp.data.local.LoginSessionManager
import com.example.assetmanagementapp.data.remote.api.model.department.DepartmentDetailRequest
import com.example.assetmanagementapp.data.remote.api.model.room.AddRoomRequest
import com.example.assetmanagementapp.data.remote.api.model.room.RoomItem
import com.example.assetmanagementapp.data.repositories.DepartmentRepository
import com.example.assetmanagementapp.data.repositories.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class DetailDepartmentViewModel @Inject constructor(
    loginSessionManager: LoginSessionManager,
    private val departmentRepository: DepartmentRepository,
    private val roomRepository: RoomRepository
) : BaseViewModelV2<DepartmentDetailState>() {

    val stateIsAdmin = MutableStateFlow(loginSessionManager.isAdmin())

    override fun initState(): DepartmentDetailState {
        return DepartmentDetailState()
    }

    fun getRoomsByDepartmentId(departmentId: Int) {
        departmentRepository.getRoomByDepartmentId(DepartmentDetailRequest(departmentId))
            .bindLoading(this).onSuccess {
                dispatchState(
                    currentState.copy(
                        listRoom = it.data as MutableList<RoomItem>
                    )
                )
            }.launchIn(viewModelScope)
    }

    fun addRoom(roomName: String) {
        roomRepository.addRoomByDepartmentId(
            AddRoomRequest(
                departmentId = currentState.departmentId,
                roomName = roomName
            )
        ).bindLoading(this).onSuccess {
            dispatchState(
                currentState.copy(
                    stateAddRoomSuccess = true,
                    listRoom = ArrayList(currentState.listRoom).apply {
                        add(it.data)
                    }
                )
            )
        }.onError {
            dispatchState(currentState.copy(stateAddRoomSuccess = false))
        }.launchIn(viewModelScope)
    }

    fun resetStateSnackBar() {
        dispatchState(
            currentState.copy(
                stateAddRoomSuccess = null,
                stateCreateNotificationSuccess = null
            )
        )
    }

    fun createNotification(roomItem: RoomItem) {
        roomRepository.createNotification(roomItem).bindLoading(this)
            .onSuccess {
                dispatchState(currentState.copy(stateCreateNotificationSuccess = true))
            }.onError {
                dispatchState(currentState.copy(stateCreateNotificationSuccess = true))
            }.launchIn(viewModelScope)
    }
}

data class DepartmentDetailState(
    val listRoom: MutableList<RoomItem> = mutableListOf(),
    val stateAddRoomSuccess: Boolean? = null,
    var departmentId: Int = 0,
    val stateCreateNotificationSuccess: Boolean? = null
)
