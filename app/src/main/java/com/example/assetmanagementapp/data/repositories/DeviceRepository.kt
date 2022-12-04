package com.example.assetmanagementapp.data.repositories

import com.example.app_common.extensions.safeFlow
import com.example.assetmanagementapp.data.remote.api.datasource.detaildevice.DetailDeviceDatasourceImpl
import com.example.assetmanagementapp.data.remote.api.datasource.searchmain.SearchMainImpl
import com.example.assetmanagementapp.data.remote.api.model.detaildevice.CheckDeviceExistRequest
import com.example.assetmanagementapp.data.remote.api.model.device.ListMainDeviceRequest
import com.example.assetmanagementapp.data.remote.api.model.searchdevice.SearchListDeviceRequest
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAssetRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepository @Inject constructor(
    private val deviceDatasourceImpl: DetailDeviceDatasourceImpl,
    private val searchMainImpl: SearchMainImpl,
) {
    fun getDetailDevice(deviceId: Int, phoneNumber: String) = safeFlow {
        deviceDatasourceImpl.getDetailDevice(deviceId, phoneNumber)
    }

    fun getListSearchMain(listMainDeviceRequest: ListMainDeviceRequest) = safeFlow {
        searchMainImpl.getListSearchMain(listMainDeviceRequest)
    }

    fun getListCategories(typeAssetRequest: TypeAssetRequest) = safeFlow {
        searchMainImpl.getListCategories(typeAssetRequest)
    }

    fun searchListDevice(searchListDeviceRequest: SearchListDeviceRequest) = safeFlow {
        searchMainImpl.searchListDevice(searchListDeviceRequest)
    }

    fun getInfoMain() = safeFlow {
        searchMainImpl.getInfoMain()
    }

    fun checkDeviceExist(checkDeviceExistRequest: CheckDeviceExistRequest) = safeFlow {
        deviceDatasourceImpl.checkDeviceExist(checkDeviceExistRequest)
    }
}
