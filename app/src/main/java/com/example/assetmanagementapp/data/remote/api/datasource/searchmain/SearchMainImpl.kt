package com.example.assetmanagementapp.data.remote.api.datasource.searchmain

import com.example.app_common.base.exception.apiCall
import com.example.assetmanagementapp.data.remote.ApiService
import com.example.assetmanagementapp.data.remote.api.model.device.ListDeviceMainResponse
import com.example.assetmanagementapp.data.remote.api.model.device.ListMainDeviceRequest
import javax.inject.Inject

class SearchMainImpl @Inject constructor(private val apiService: ApiService) :
    SearchMainSource {
    override suspend fun getListSearchMain(listMainDeviceRequest: ListMainDeviceRequest): ListDeviceMainResponse =
        apiCall {
            apiService.getListMainDevice(listMainDeviceRequest)
        }
}
