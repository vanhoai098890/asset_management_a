package com.example.assetmanagementapp.data.repositories

import com.example.app_common.extensions.safeFlow
import com.example.assetmanagementapp.data.remote.api.datasource.qrcode.QrcodeDatasourceImpl
import com.example.assetmanagementapp.data.remote.api.model.qrcode.QrcodeRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QrcodeRepository @Inject constructor(
    private val qrcodeDatasourceImpl: QrcodeDatasourceImpl
) {

    fun generateQrcode(qrcodeRequest: QrcodeRequest) = safeFlow {
        qrcodeDatasourceImpl.generateQrcode(qrcodeRequest)
    }
}
