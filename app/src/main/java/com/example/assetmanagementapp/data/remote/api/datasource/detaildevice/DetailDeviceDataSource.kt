package com.example.assetmanagementapp.data.remote.api.datasource.detaildevice

import com.example.app_common.base.response.CommonResponse
import com.example.assetmanagementapp.data.remote.api.model.detaildevice.CheckDeviceExistRequest
import com.example.assetmanagementapp.data.remote.api.model.detaildevice.CheckDeviceExistResponse
import com.example.assetmanagementapp.data.remote.api.model.detaildevice.DetailDeviceResponse
import com.example.assetmanagementapp.data.remote.api.model.detaildevice.EditDeviceRequest

interface DetailDeviceDataSource {

    suspend fun getDetailDevice(deviceId: Int, phoneNumber: String): DetailDeviceResponse

    suspend fun editDevice(editDeviceRequest: EditDeviceRequest): CommonResponse

    suspend fun checkDeviceExist(checkDeviceExistRequest: CheckDeviceExistRequest): CheckDeviceExistResponse

}
