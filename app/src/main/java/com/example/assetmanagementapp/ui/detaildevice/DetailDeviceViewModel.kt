package com.example.assetmanagementapp.ui.detaildevice

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.lifecycle.viewModelScope
import com.example.app_common.base.viewmodel.BaseViewModelV2
import com.example.app_common.constant.AppConstant
import com.example.app_common.constant.AppConstant.ERROR_BITMAP
import com.example.app_common.constant.AppConstant.LOADING_BITMAP
import com.example.app_common.constant.AppConstant.REGEX_BASE_64_IMAGE
import com.example.app_common.constant.AppConstant.SHOW_BITMAP
import com.example.app_common.extensions.bindLoading
import com.example.app_common.extensions.onError
import com.example.app_common.extensions.onSuccess
import com.example.app_common.utils.LogUtils
import com.example.assetmanagementapp.data.local.LoginSessionManager
import com.example.assetmanagementapp.data.remote.api.model.qrcode.QrcodeRequest
import com.example.assetmanagementapp.data.repositories.DeviceRepository
import com.example.assetmanagementapp.data.repositories.FavouriteRepository
import com.example.assetmanagementapp.data.repositories.QrcodeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailDeviceViewModel @Inject constructor(
    private val loginSessionManager: LoginSessionManager,
    private val deviceRepository: DeviceRepository,
    private val favouriteRepository: FavouriteRepository,
    private val qrcodeRepository: QrcodeRepository
) : BaseViewModelV2<DetailDeviceState>() {
    override fun initState() = DetailDeviceState()

    private var isBlockSave: Boolean = false

    private val stateSetJobOnSaveDeviceRequest = HashSet<Int>()

    fun getDetailDevice() {
        currentState.deviceId?.let { deviceId ->
            deviceRepository.getDetailDevice(deviceId, loginSessionManager.getUsername())
                .bindLoading(this).onSuccess {
                    dispatchState(
                        currentState.copy(
                            deviceItem = it.data,
                            stateVisibleMask = false,
                            stateIsFavourite = it.data.isFavourite
                        )
                    )
                }.onError {
                    dispatchState(currentState.copy(stateShowSomethingWrong = true))
                }.launchIn(viewModelScope)
        }
    }


    fun saveDevices(deviceId: Int, isSave: Boolean) {
        if (stateSetJobOnSaveDeviceRequest.contains(deviceId)) return
        synchronized(isBlockSave) {
            viewModelScope.launch {
                stateSetJobOnSaveDeviceRequest.add(deviceId)
                favouriteRepository.saveDevices(loginSessionManager.getUsername(), deviceId, isSave)
                    .onSuccess {
                        dispatchState(
                            currentState.copy(
                                stateIsShowSnackBar = isSave,
                                stateIsFavourite = isSave
                            )
                        )
                    }.onCompletion {
                        stateSetJobOnSaveDeviceRequest.remove(deviceId)
                    }.launchIn(viewModelScope)
            }
        }
    }

    fun dispatchResetSnackBar() {
        dispatchState(currentState.copy(stateIsShowSnackBar = null))
    }

    fun dispatchResetStateDialog() {
        dispatchState(currentState.copy(stateShowDialogBitmap = 0))
    }

    fun getQrcodeAsset() {
        currentState.deviceId?.let { assetId ->
            qrcodeRepository.generateQrcode(QrcodeRequest(assetId)).onSuccess {
                try {
                    val bytes: ByteArray =
                        Base64.decode(
                            it.data.replace(
                                Regex(REGEX_BASE_64_IMAGE),
                                AppConstant.EMPTY
                            ), Base64.DEFAULT
                        )
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    currentState.qrCodeBitmap = bitmap
                } catch (e: Exception) {
                    LogUtils.e(e.stackTraceToString())
                    currentState.stateErrorGenerateQr = true
                }
            }.launchIn(viewModelScope)
        }
    }

    fun showDialogQrcode() {
        when {
            !currentState.stateErrorGenerateQr && currentState.qrCodeBitmap == null -> {
                dispatchState(currentState.copy(stateShowDialogBitmap = LOADING_BITMAP))
            }
            !currentState.stateErrorGenerateQr -> {
                dispatchState(currentState.copy(stateShowDialogBitmap = SHOW_BITMAP))
            }
            else -> {
                dispatchState(currentState.copy(stateShowDialogBitmap = ERROR_BITMAP))
            }
        }
    }
}
