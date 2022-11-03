package com.example.assetmanagementapp.data.repositories

import com.example.app_common.extensions.safeFlow
import com.example.assetmanagementapp.data.remote.api.datasource.detaildevice.DetailDeviceDatasourceImpl
import com.example.assetmanagementapp.data.remote.api.datasource.searchmain.SearchMainImpl
import com.example.assetmanagementapp.data.remote.api.model.device.ListMainDeviceRequest
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
}
