package com.example.assetmanagementapp.ui.favourite

import androidx.lifecycle.viewModelScope
import com.example.app_common.base.viewmodel.BaseViewModelV2
import com.example.app_common.extensions.onSuccess
import com.example.assetmanagementapp.data.local.LoginSessionManager
import com.example.assetmanagementapp.data.remote.api.model.favourite.DeviceItem
import com.example.assetmanagementapp.data.repositories.FavouriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    loginSessionManager: LoginSessionManager,
    private val favouriteRepository: FavouriteRepository
) : BaseViewModelV2<FavouriteFragmentState>() {

    private var isBlockSave: Boolean = false

    private val stateSetJobOnSaveDeviceRequest = HashSet<Int>()
    private var page = 0
    private val size = 10

    init {
        dispatchState(currentState.copy(stateCurrentUserInfo = loginSessionManager.getCustomerLocal()?.userInfo))
    }

    override fun initState() = FavouriteFragmentState()

    fun getFavouriteDevices() {
        dispatchState(currentState.copy(stateListFavouriteRoom = mutableListOf()))
        page = 0
        currentState.stateCurrentUserInfo?.phoneNumber?.apply {
            favouriteRepository.getFavouriteDevices(
                phoneNumber = this,
                page = page,
                size = size
            ).onSuccess {
                dispatchState(currentState.copy(stateListFavouriteRoom = it.data as MutableList<DeviceItem>))
                dispatchState(currentState.copy(stateVisibleNotFoundItem = it.data.isEmpty()))
                page += 1
            }.launchIn(viewModelScope)
        }
    }

    fun onLoadMore() {
        currentState.stateCurrentUserInfo?.phoneNumber?.apply {
            favouriteRepository.getFavouriteDevices(
                phoneNumber = this,
                page = page,
                size = size
            ).onSuccess {
                dispatchState(currentState.copy(stateListFavouriteRoom = ArrayList(currentState.stateListFavouriteRoom).apply {
                    addAll(
                        it.data
                    )
                }))
                page += 1
            }.launchIn(viewModelScope)
        }
    }

    fun getFavouriteDevicesMockEmpty() {
        dispatchState(currentState.copy(stateVisibleNotFoundItem = true))
    }

    fun getFavouriteDevicesMock() {
        dispatchState(
            currentState.copy(
                stateListFavouriteRoom = mutableListOf(
                    DeviceItem(id = 1),
                    DeviceItem(id = 2, status = "Damaged"),
                    DeviceItem(id = 3, status = "Borrowed"),
                    DeviceItem(id = 4, status = "Warranty"),
                    DeviceItem(id = 5),
                    DeviceItem(id = 6),
                    DeviceItem(id = 7),
                    DeviceItem(id = 8),
                    DeviceItem(id = 9),
                    DeviceItem(id = 10)
                )
            )
        )
        dispatchState(currentState.copy(stateVisibleNotFoundItem = false))
    }

    fun saveDevices(deviceId: Int, isSave: Boolean) {
        if (stateSetJobOnSaveDeviceRequest.contains(deviceId)) return
        currentState.stateCurrentUserInfo?.apply {
            viewModelScope.launch {
                synchronized(isBlockSave) {
                    stateSetJobOnSaveDeviceRequest.add(deviceId)
                    favouriteRepository.saveDevices(phoneNumber, deviceId, isSave).onSuccess {
                        dispatchState(
                            currentState.copy(
                                stateIsShowSnackBar = isSave,
                                stateListFavouriteRoom = currentState.stateListFavouriteRoom.map { item ->
                                    if (item.id == deviceId) {
                                        item.copy(isFavourite = isSave)
                                    } else {
                                        item
                                    }
                                })
                        )
                    }.onCompletion {
                        stateSetJobOnSaveDeviceRequest.remove(deviceId)
                    }
                }
            }
        }
    }

    fun dispatchResetSnackBar() {
        dispatchState(currentState.copy(stateIsShowSnackBar = null))
    }
}
