package com.example.assetmanagementapp.data.remote.api.datasource.qrcode

import com.example.app_common.base.exception.apiCall
import com.example.assetmanagementapp.data.remote.ApiService
import com.example.assetmanagementapp.data.remote.api.model.qrcode.QrcodeRequest
import com.example.assetmanagementapp.data.remote.api.model.qrcode.QrcodeResponse
import javax.inject.Inject

class QrcodeDatasourceImpl @Inject constructor(private val apiService: ApiService) :
    QrcodeDatasource {
    override suspend fun generateQrcode(qrcodeRequest: QrcodeRequest): QrcodeResponse {
        return apiCall {
            apiService.generateQrcode(qrcodeRequest)
        }
    }
}
