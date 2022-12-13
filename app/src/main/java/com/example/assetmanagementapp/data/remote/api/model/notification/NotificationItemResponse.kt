package com.example.assetmanagementapp.data.remote.api.model.notification

import com.example.app_common.base.response.CommonResponse
import com.google.gson.annotations.SerializedName

data class NotificationItemResponse(
    @SerializedName("data")
    val data: List<NotificationItem>
) : CommonResponse()

data class NotificationItemSingleResponse(
    @SerializedName("data")
    val data: NotificationItem
) : CommonResponse()

data class NotificationItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("typeNotification")
    val typeNotification: Int,
    @SerializedName("time")
    val time: String,
    @SerializedName("roomName")
    val roomName: String,
    @SerializedName("departmentName")
    val departmentName: String,
    @SerializedName("path")
    val path: String,
    @SerializedName("isUpdated")
    val isUpdated: Boolean,
    val stateLoading: StateDownload? = StateDownload.DEFAULT,
    val stateUpdating: StateDownload? = StateDownload.DEFAULT
)

enum class StateDownload {
    DEFAULT,
    LOADING,
    SUCCESS,
    ERROR,
    UPLOADING
}
