package com.example.assetmanagementapp.data.remote.api.datasource.notification

import com.example.assetmanagementapp.data.remote.api.model.notification.NotificationItemResponse
import okhttp3.ResponseBody
import java.io.ByteArrayInputStream

interface NotificationDatasource {
    suspend fun getNotification(): NotificationItemResponse
    suspend fun getExcelFileFromPath(path: String): ResponseBody
}
