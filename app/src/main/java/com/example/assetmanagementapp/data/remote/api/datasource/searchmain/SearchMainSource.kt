package com.example.assetmanagementapp.data.remote.api.datasource.searchmain

import com.example.assetmanagementapp.data.remote.api.model.device.ListDeviceMainResponse
import com.example.assetmanagementapp.data.remote.api.model.device.ListMainDeviceRequest

interface SearchMainSource {
    suspend fun getListSearchMain(listMainDeviceRequest: ListMainDeviceRequest): ListDeviceMainResponse
}
