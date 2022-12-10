package com.example.assetmanagementapp.data.remote.api.datasource.searchmain

import com.example.app_common.base.response.CommonResponse
import com.example.assetmanagementapp.data.remote.api.model.device.ListDeviceMainResponse
import com.example.assetmanagementapp.data.remote.api.model.device.ListMainDeviceRequest
import com.example.assetmanagementapp.data.remote.api.model.favourite.DeviceItemResponse
import com.example.assetmanagementapp.data.remote.api.model.infomain.InfoMainResponse
import com.example.assetmanagementapp.data.remote.api.model.searchdevice.SearchListDeviceRequest
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAsset
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAssetItemResponse
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAssetRequest
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAssetResponse

interface SearchMainSource {
    suspend fun getListSearchMain(listMainDeviceRequest: ListMainDeviceRequest): ListDeviceMainResponse
    suspend fun getListCategories(typeAssetRequest: TypeAssetRequest): TypeAssetResponse
    suspend fun getListCategories(): TypeAssetResponse
    suspend fun editCategory(typeAsset: TypeAsset): CommonResponse
    suspend fun addCategory(typeAsset: TypeAsset): TypeAssetItemResponse
    suspend fun getListStatusType(): TypeAssetResponse
    suspend fun searchListDevice(searchListDeviceRequest: SearchListDeviceRequest): DeviceItemResponse
    suspend fun getInfoMain(): InfoMainResponse
}
