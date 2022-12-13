package com.example.assetmanagementapp.ui.notification

import androidx.lifecycle.viewModelScope
import com.example.app_common.base.viewmodel.BaseViewModelV2
import com.example.app_common.extensions.bindLoading
import com.example.app_common.extensions.onError
import com.example.app_common.extensions.onSuccess
import com.example.assetmanagementapp.data.local.LoginSessionManager
import com.example.assetmanagementapp.data.remote.api.model.notification.NotificationItem
import com.example.assetmanagementapp.data.remote.api.model.notification.StateDownload
import com.example.assetmanagementapp.data.repositories.NotificationRepository
import com.example.assetmanagementapp.data.repositories.RoomRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    loginSessionManager: LoginSessionManager,
    private val roomRepository: RoomRepository
) : BaseViewModelV2<NotificationState>() {
    override fun initState() = NotificationState()

    val stateIsAdmin = MutableStateFlow(loginSessionManager.isAdmin())

    init {
        getNotification()
    }

    fun getNotification() {
        notificationRepository.getNotification().bindLoading(this).onSuccess {
            dispatchState(currentState.copy(stateListNotification = it.data))
        }.launchIn(viewModelScope)
    }

    fun downloadExcelFile(data: NotificationItem) {
        viewModelScope.launch(Dispatchers.IO) {
            notificationRepository.getFileFromPath(data.path)
                .bindLoading(this@NotificationViewModel)
                .onStart {
                    dispatchState(currentState.copy(
                        stateListNotification = currentState.stateListNotification.map {
                            if (it.id == data.id) {
                                it.copy(stateLoading = StateDownload.LOADING)
                            } else {
                                it
                            }
                        }
                    ))
                }
                .onError {
                    dispatchState(currentState.copy(
                        stateListNotification = currentState.stateListNotification.map {
                            if (it.id == data.id) {
                                it.copy(stateLoading = StateDownload.ERROR)
                            } else {
                                it
                            }
                        }
                    ))
                }
                .onSuccess { response ->
                    currentState.mapResponse[response] = data.path
                    dispatchState(
                        currentState.copy(
                            stateListNotification = currentState.stateListNotification.map {
                                if (it.id == data.id) {
                                    it.copy(stateLoading = StateDownload.SUCCESS)
                                } else {
                                    it
                                }
                            },
                            fileResponse = response
                        )
                    )
                }
                .launchIn(viewModelScope)
        }
    }

    fun updateFileExcelTicket(notification: NotificationItem, excelFile: File) {
        val requestFile =
            excelFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData(
            "file",
            excelFile.name,
            requestFile
        )
        roomRepository.updateNotification(notificationItem = notification, part = multipartBody)
            .onStart {
                dispatchState(currentState.copy(stateListNotification = currentState.stateListNotification.map {
                    if (it.id == notification.id) {
                        it.copy(stateUpdating = StateDownload.UPLOADING)
                    } else {
                        it
                    }
                }))
            }
            .bindLoading(this)
            .onSuccess { itemResponse ->
                dispatchState(currentState.copy(stateListNotification = currentState.stateListNotification.map {
                    if (it.id == itemResponse.data.id) {
                        itemResponse.data.copy(stateUpdating = StateDownload.SUCCESS)
                    } else {
                        it
                    }
                }))
            }.onError {
                dispatchState(currentState.copy(stateListNotification = currentState.stateListNotification.map {
                    if (it.id == notification.id) {
                        notification.copy(stateUpdating = StateDownload.ERROR)
                    } else {
                        it
                    }
                }))
            }.launchIn(viewModelScope)
    }
}

data class NotificationState(
    val any: Any = "",
    val stateListNotification: List<NotificationItem> = listOf(),
    val fileResponse: ResponseBody? = null,
    val mapResponse: HashMap<ResponseBody, String> = hashMapOf()
)
