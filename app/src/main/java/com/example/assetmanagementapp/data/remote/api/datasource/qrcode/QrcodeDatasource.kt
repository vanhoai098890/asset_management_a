package com.example.assetmanagementapp.data.remote.api.datasource.qrcode

import com.example.assetmanagementapp.data.remote.api.model.qrcode.QrcodeRequest
import com.example.assetmanagementapp.data.remote.api.model.qrcode.QrcodeResponse

interface QrcodeDatasource {
    suspend fun generateQrcode(qrcodeRequest: QrcodeRequest): QrcodeResponse
}
