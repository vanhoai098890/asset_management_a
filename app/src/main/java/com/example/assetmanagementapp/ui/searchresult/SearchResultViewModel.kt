package com.example.assetmanagementapp.ui.searchresult

import androidx.lifecycle.viewModelScope
import com.example.app_common.base.viewmodel.BaseViewModelV2
import com.example.app_common.extensions.onError
import com.example.app_common.extensions.onSuccess
import com.example.app_common.utils.LogUtils
import com.example.assetmanagementapp.data.remote.api.model.favourite.DeviceItem
import com.example.assetmanagementapp.data.remote.api.model.searchdevice.SearchListDeviceRequest
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAsset
import com.example.assetmanagementapp.data.repositories.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class SearchResultViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository
) : BaseViewModelV2<SearchResultState>() {
    override fun initState(): SearchResultState = SearchResultState()

    fun onLoadMore(searchString: String) {
        synchronized(currentState.block) {
            if (!currentState.mapSearchListDevice.containsKey(
                    Pair(
                        searchString,
                        currentState.currentSelectedPos.id
                    )
                ) || currentState.stateLoadingDevice || currentState.isEndOfList
            ) {
                return
            }
            LogUtils.d("searchDevices onLoadMore")
            val page = currentState.mapSearchListDevice[Pair(
                searchString,
                currentState.currentSelectedPos.id
            )]!!
            deviceRepository.searchListDevice(
                SearchListDeviceRequest(
                    searchString = searchString,
                    category = currentState.currentSelectedPos.id,
                    page = page,
                    size = currentState.size,
                    departmentId = currentState.departmentId,
                    roomId = currentState.roomId,
                    statusId = currentState.currentSelectedStatus.id
                )
            )
                .onStart {
                    currentState.stateLoadingDevice = true
                    dispatchState(
                        currentState.copy(
                            listSearchDevice = ArrayList(currentState.listSearchDevice).apply {
                                add(
                                    DeviceItem()
                                )
                            })
                    )
                }
                .onSuccess {
                    if (it.data.size < 10) {
                        currentState.isEndOfList = true
                    }
                    currentState.mapSearchListDevice[Pair(
                        searchString,
                        currentState.currentSelectedPos.id
                    )] = currentState.mapSearchListDevice[Pair(
                        searchString,
                        currentState.currentSelectedPos.id
                    )]!! + 1
                    dispatchState(
                        currentState.copy(
                            listSearchDevice = ArrayList(currentState.listSearchDevice).apply {
                                removeIf { item -> item.id == 0 }
                                addAll(it.data)
                            }
                        )
                    )
                }
                .onError {
                    dispatchState(
                        currentState.copy(
                            listSearchDevice = ArrayList(currentState.listSearchDevice).apply {
                                removeIf { item -> item.id == 0 }
                            }
                        )
                    )
                }
                .onCompletion {
                    currentState.stateLoadingDevice = false
                }
                .launchIn(viewModelScope)
        }
    }

    fun searchDevices(searchString: String) {
        if (currentState.stateLoadingDevice) {
            return
        }
        LogUtils.d("searchDevices")
        currentState.stateLoadingDevice = true
        currentState.isEndOfList = false
        dispatchState(currentState.copy(listSearchDevice = mutableListOf()))
        currentState.mapSearchListDevice.clear()
        currentState.mapSearchListDevice[Pair(
            searchString,
            currentState.currentSelectedPos.id
        )] = 0
        val page = currentState.mapSearchListDevice[Pair(
            searchString,
            currentState.currentSelectedPos.id
        )]!!
        deviceRepository.searchListDevice(
            SearchListDeviceRequest(
                searchString = searchString,
                category = currentState.currentSelectedPos.id,
                page = page,
                size = currentState.size,
                departmentId = currentState.departmentId,
                roomId = currentState.roomId,
                statusId = currentState.currentSelectedStatus.id
            )
        )
            .onStart {
                dispatchState(
                    currentState.copy(
                        listSearchDevice = ArrayList(currentState.listSearchDevice).apply {
                            add(
                                DeviceItem()
                            )
                        })
                )
            }
            .onSuccess {
                if (it.data.size < 10) {
                    currentState.isEndOfList = true
                }
                currentState.mapSearchListDevice[Pair(
                    searchString,
                    currentState.currentSelectedPos.id
                )] = currentState.mapSearchListDevice[Pair(
                    searchString,
                    currentState.currentSelectedPos.id
                )]!! + 1
                dispatchState(
                    currentState.copy(
                        listSearchDevice = ArrayList(currentState.listSearchDevice).apply {
                            removeIf { item -> item.id == 0 }
                            addAll(it.data)
                        }
                    )
                )
            }
            .onError {
                dispatchState(
                    currentState.copy(
                        listSearchDevice = ArrayList(currentState.listSearchDevice).apply {
                            removeIf { item -> item.id == 0 }
                        }
                    )
                )
            }
            .onCompletion {
                currentState.stateLoadingDevice = false
            }
            .launchIn(viewModelScope)
    }

    fun dispatchCategorySelected(typeAsset: TypeAsset) {
        dispatchState(currentState.copy(currentSelectedPos = typeAsset))
    }

    fun dispatchStatusTypeSelected(typeAsset: TypeAsset) {
        dispatchState(currentState.copy(currentSelectedStatus = typeAsset))
    }

    fun dispatchStateIsAdmin(isAdmin:Boolean) {
        dispatchState(currentState.copy(stateIsAdmin = isAdmin))
    }
}

data class SearchResultState(
    var currentSelectedPos: TypeAsset = TypeAsset(0, "All"),
    var currentSelectedStatus: TypeAsset = TypeAsset(0, "All"),
    val listSearchDevice: MutableList<DeviceItem> = mutableListOf(),
    var size: Int = 10,
    /**
     * contain pair key value (searchString,category) -> page
     */
    var mapSearchListDevice: MutableMap<Pair<String, Int>, Int> = mutableMapOf(),
    var stateLoadingDevice: Boolean = false,
    var isEndOfList: Boolean = false,
    var block: Any = "",
    var stateNoResult: Boolean = false,
    var departmentId: Int = 0,
    var roomId: Int = 0,
    var stateIsAdmin:Boolean = false
)
