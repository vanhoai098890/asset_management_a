package com.example.assetmanagementapp.data.remote.api.datasource.searchmain

import com.example.app_common.base.exception.apiCall
import com.example.assetmanagementapp.data.remote.ApiService
import com.example.assetmanagementapp.data.remote.api.model.device.ListDeviceMainResponse
import com.example.assetmanagementapp.data.remote.api.model.device.ListMainDeviceRequest
import com.example.assetmanagementapp.data.remote.api.model.favourite.DeviceItemResponse
import com.example.assetmanagementapp.data.remote.api.model.infomain.InfoMainResponse
import com.example.assetmanagementapp.data.remote.api.model.searchdevice.SearchListDeviceRequest
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAssetRequest
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAssetResponse
import javax.inject.Inject

class SearchMainImpl @Inject constructor(private val apiService: ApiService) :
    SearchMainSource {
    override suspend fun getListSearchMain(listMainDeviceRequest: ListMainDeviceRequest): ListDeviceMainResponse =
        apiCall {
            apiService.getListMainDevice(listMainDeviceRequest)
        }

    override suspend fun getListCategories(typeAssetRequest: TypeAssetRequest): TypeAssetResponse =
        apiCall {
            apiService.getCategories(typeAssetRequest)
        }

    override suspend fun getListCategories(): TypeAssetResponse =
        apiCall {
            apiService.getCategories()
        }

    override suspend fun getListStatusType(): TypeAssetResponse =
        apiCall {
            apiService.getStatusType()
        }

    override suspend fun searchListDevice(searchListDeviceRequest: SearchListDeviceRequest): DeviceItemResponse =
        apiCall {
            apiService.searchListDevice(searchListDeviceRequest)
        }

    override suspend fun getInfoMain(): InfoMainResponse {
        return apiCall {
            apiService.getInfoMain()
        }
    }
}
