package com.example.assetmanagementapp.ui.room_detail

import androidx.lifecycle.viewModelScope
import com.example.app_common.base.viewmodel.BaseViewModelV2
import com.example.app_common.extensions.bindLoading
import com.example.app_common.extensions.onSuccess
import com.example.assetmanagementapp.data.remote.api.model.favourite.DeviceItem
import com.example.assetmanagementapp.data.remote.api.model.room.AssetItemRequest
import com.example.assetmanagementapp.data.repositories.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class RoomDetailViewModel @Inject constructor(
    private val roomRepository: RoomRepository
) : BaseViewModelV2<RoomDetailState>() {
    override fun initState(): RoomDetailState = RoomDetailState()

    fun getAssetByRoomId() {
        roomRepository.getAssetByRoomId(AssetItemRequest(currentState.roomId)).bindLoading(this)
            .onSuccess {
                dispatchState(currentState.copy(listSearchDevice = it.data as MutableList<DeviceItem>))
            }.launchIn(viewModelScope)
    }

}

data class RoomDetailState(
    val any: Any = "",
    val listSearchDevice: MutableList<DeviceItem> = mutableListOf(),
    var roomId: Int = 0
)