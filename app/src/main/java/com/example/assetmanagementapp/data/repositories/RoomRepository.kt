package com.example.assetmanagementapp.data.repositories

import com.example.app_common.extensions.safeFlow
import com.example.assetmanagementapp.data.remote.api.datasource.room.RoomDatasourceImpl
import com.example.assetmanagementapp.data.remote.api.model.room.AddRoomRequest
import com.example.assetmanagementapp.data.remote.api.model.room.AssetItemRequest
import com.example.assetmanagementapp.data.remote.api.model.room.RoomItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RoomRepository @Inject constructor(
    private val roomDatasourceImpl: RoomDatasourceImpl,
) {
    fun addRoomByDepartmentId(addRoomRequest: AddRoomRequest) = safeFlow {
        roomDatasourceImpl.addRoomByDepartmentId(addRoomRequest)
    }

    fun getAssetByRoomId(assetItemRequest: AssetItemRequest) = safeFlow {
        roomDatasourceImpl.getAssetByRoomId(assetItemRequest)
    }

    fun createNotification(roomItem: RoomItem) = safeFlow {
        roomDatasourceImpl.createNotification(roomItem.roomId)
    }
}
