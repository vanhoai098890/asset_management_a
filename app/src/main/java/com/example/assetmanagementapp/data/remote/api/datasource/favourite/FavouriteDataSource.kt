package com.example.assetmanagementapp.data.remote.api.datasource.favourite

import com.example.app_common.base.response.CommonResponse
import com.example.assetmanagementapp.data.remote.api.model.favourite.DeviceItemResponse

interface FavouriteDataSource {

    suspend fun getFavouriteDevices(phoneNumber: String, page: Int, size: Int): DeviceItemResponse

    suspend fun saveDevices(
        phoneNumber: String,
        devicesId: Int,
        isSave: Boolean
    ): CommonResponse
}
