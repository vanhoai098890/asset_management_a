package com.example.assetmanagementapp.data.remote.api.datasource.notification

import com.example.app_common.base.exception.apiCall
import com.example.assetmanagementapp.data.remote.ApiService
import com.example.assetmanagementapp.data.remote.api.model.notification.NotificationItemResponse
import okhttp3.ResponseBody
import javax.inject.Inject

class NotificationDatasourceImpl @Inject constructor(private val apiService: ApiService) :
    NotificationDatasource {
    override suspend fun getNotification(): NotificationItemResponse {
        return apiCall {
            apiService.getNotifications()
        }
    }

    override suspend fun getExcelFileFromPath(path: String): ResponseBody {
        return apiCall {
            apiService.getExcelFileFromPath(path)
        }
    }
}
