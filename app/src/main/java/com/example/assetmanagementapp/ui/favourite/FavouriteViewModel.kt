package com.example.assetmanagementapp.ui.favourite

import androidx.lifecycle.viewModelScope
import com.example.app_common.base.viewmodel.BaseViewModelV2
import com.example.app_common.extensions.onError
import com.example.app_common.extensions.onSuccess
import com.example.assetmanagementapp.data.local.LoginSessionManager
import com.example.assetmanagementapp.data.remote.api.model.favourite.DeviceItem
import com.example.assetmanagementapp.data.repositories.FavouriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
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
        dispatchState(currentState.copy(stateCurrentUserInfo = loginSessionManager.getCustomerLocal()))
    }

    override fun initState() = FavouriteFragmentState()

    fun getFavouriteDevices() {
        dispatchState(
            currentState.copy(
                stateListFavouriteRoom = mutableListOf(),
                isEndOfList = false
            )
        )
        page = 0
        onLoadMore()
    }

    fun onLoadMore() {
        if (currentState.stateLoadingListMain || currentState.isEndOfList) return
        currentState.stateCurrentUserInfo?.phoneNumber?.apply {
            favouriteRepository.getFavouriteDevices(
                phoneNumber = this,
                page = page,
                size = size
            ).onStart {
                dispatchState(
                    currentState.copy(
                        stateLoadingListMain = true,
                        stateListFavouriteRoom = ArrayList(currentState.stateListFavouriteRoom).apply {
                            add(
                                DeviceItem()
                            )
                        }
                    )
                )
            }.onSuccess {
                if (it.data.size < 10) {
                    currentState.isEndOfList = true
                }
                dispatchState(
                    currentState.copy(
                        stateListFavouriteRoom = ArrayList(currentState.stateListFavouriteRoom).apply {
                            if (size > 0) {
                                removeLast()
                            }
                            addAll(
                                it.data
                            )
                        }
                    )
                )
                dispatchState(
                    currentState.copy(
                        stateVisibleNotFoundItem = currentState.stateListFavouriteRoom.isEmpty()
                    )
                )
                page += 1
            }.onError {
                dispatchState(
                    currentState.copy(
                        stateListFavouriteRoom = ArrayList(currentState.stateListFavouriteRoom).apply {
                            if (size > 0) {
                                removeLast()
                            }
                        }
                    )
                )
            }.onCompletion {
                dispatchState(
                    currentState.copy(
                        stateLoadingListMain = false
                    )
                )
            }.launchIn(viewModelScope)
        }
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
                    }.launchIn(viewModelScope)
                }
            }
        }
    }

    fun dispatchResetSnackBar() {
        dispatchState(currentState.copy(stateIsShowSnackBar = null))
    }

    fun dispatchFragmentResultFavourite(id: Int, isFavourite: Boolean) {
        dispatchState(currentState.copy(stateListFavouriteRoom = currentState.stateListFavouriteRoom.map { item ->
            if (item.id == id) {
                item.copy(isFavourite = isFavourite)
            } else {
                item
            }
        }))
    }
}
