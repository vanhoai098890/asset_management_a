package com.example.assetmanagementapp.data.remote.api.datasource.searchmain

import com.example.assetmanagementapp.data.remote.api.model.device.ListDeviceMainResponse
import com.example.assetmanagementapp.data.remote.api.model.device.ListMainDeviceRequest
import com.example.assetmanagementapp.data.remote.api.model.favourite.DeviceItemResponse
import com.example.assetmanagementapp.data.remote.api.model.searchdevice.SearchListDeviceRequest
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAssetResponse

interface SearchMainSource {
    suspend fun getListSearchMain(listMainDeviceRequest: ListMainDeviceRequest): ListDeviceMainResponse
    suspend fun getListCategories(): TypeAssetResponse
    suspend fun searchListDevice(searchListDeviceRequest: SearchListDeviceRequest): DeviceItemResponse
}
