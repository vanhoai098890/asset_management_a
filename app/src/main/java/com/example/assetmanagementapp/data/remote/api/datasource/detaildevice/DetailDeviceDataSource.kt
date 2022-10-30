package com.example.assetmanagementapp.data.remote.api.datasource.detaildevice

import com.example.assetmanagementapp.data.remote.api.model.detaildevice.DetailDeviceResponse

interface DetailDeviceDataSource {
    suspend fun getDetailDevice(deviceId: Int): DetailDeviceResponse
}
