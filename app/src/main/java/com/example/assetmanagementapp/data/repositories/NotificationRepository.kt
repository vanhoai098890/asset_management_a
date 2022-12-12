package com.example.assetmanagementapp.data.repositories

import com.example.app_common.extensions.safeFlow
import com.example.assetmanagementapp.data.remote.api.datasource.notification.NotificationDatasourceImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationRepository @Inject constructor(
    private val notificationDatasourceImpl: NotificationDatasourceImpl,
) {

    fun getNotification() = safeFlow {
        notificationDatasourceImpl.getNotification()
    }

    fun getFileFromPath(path: String) = safeFlow {
        notificationDatasourceImpl.getExcelFileFromPath(path)
    }
}
