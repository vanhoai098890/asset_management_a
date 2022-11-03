package com.example.assetmanagementapp.data.remote.api.datasource.detaildevice

import com.example.app_common.base.exception.apiCall
import com.example.assetmanagementapp.data.remote.ApiService
import com.example.assetmanagementapp.data.remote.api.model.detaildevice.DetailDeviceRequest
import com.example.assetmanagementapp.data.remote.api.model.detaildevice.DetailDeviceResponse
import javax.inject.Inject

class DetailDeviceDatasourceImpl @Inject constructor(private val apiService: ApiService) :
    DetailDeviceDataSource {
    override suspend fun getDetailDevice(deviceId: Int, phoneNumber: String): DetailDeviceResponse =
        apiCall {
            apiService.getDetailDevice(
                DetailDeviceRequest(
                    assetId = deviceId,
                    phoneNumber = phoneNumber
                )
            )
        }
}
