package com.example.assetmanagementapp.data.remote.api.datasource.detaildevice

import com.example.assetmanagementapp.data.remote.api.model.detaildevice.CheckDeviceExistRequest
import com.example.assetmanagementapp.data.remote.api.model.detaildevice.CheckDeviceExistResponse
import com.example.assetmanagementapp.data.remote.api.model.detaildevice.DetailDeviceResponse

interface DetailDeviceDataSource {

    suspend fun getDetailDevice(deviceId: Int, phoneNumber: String): DetailDeviceResponse

    suspend fun checkDeviceExist(checkDeviceExistRequest: CheckDeviceExistRequest): CheckDeviceExistResponse

}
