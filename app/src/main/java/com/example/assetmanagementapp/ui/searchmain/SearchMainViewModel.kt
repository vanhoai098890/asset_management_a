package com.example.assetmanagementapp.ui.searchmain

import androidx.lifecycle.viewModelScope
import com.example.app_common.base.viewmodel.BaseViewModel
import com.example.app_common.extensions.bindLoading
import com.example.app_common.extensions.onError
import com.example.app_common.extensions.onSuccess
import com.example.assetmanagementapp.data.local.LoginSessionManager
import com.example.assetmanagementapp.data.remote.api.model.customer.UserInfo
import com.example.assetmanagementapp.data.remote.api.model.device.ItemDeviceData
import com.example.assetmanagementapp.data.remote.api.model.device.ListMainDeviceRequest
import com.example.assetmanagementapp.data.repositories.CustomerRepository
import com.example.assetmanagementapp.data.repositories.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class SearchMainViewModel @Inject constructor(
    private val loginSessionManager: LoginSessionManager,
    private val customerRepository: CustomerRepository,
    private val deviceRepository: DeviceRepository
) : BaseViewModel() {
    private var page = 0
    private var stateIsEndOfList = false
    private val size = 10
    val stateUserInfo: MutableStateFlow<UserInfo?> = MutableStateFlow(null)
    val listItemDeviceData: MutableStateFlow<MutableList<ItemDeviceData>> = MutableStateFlow(
        mutableListOf()
    )
    val stateShowSnackBar = MutableStateFlow(false)
    var stateLoadingListMain = MutableStateFlow(false)

    init {
        getCustomerInfo()
        onLoadMore()
    }

    fun getCustomerInfo() {
        customerRepository.getUserInfoByPhoneNumber(loginSessionManager.getUsername())
            .bindLoading(this)
            .onSuccess {
                stateUserInfo.value = it.userInfo
            }
            .launchIn(viewModelScope)
    }

    fun onLoadMore() {
        if (stateLoadingListMain.value || stateIsEndOfList) return
        deviceRepository.getListSearchMain(
            ListMainDeviceRequest(
                phoneNumber = loginSessionManager.getUsername(),
                page = page,
                size = size
            )
        )
            .bindLoading(this)
            .onStart {
                stateLoadingListMain.value = true
                listItemDeviceData.value = ArrayList(listItemDeviceData.value).apply {
                    add(ItemDeviceData())
                }
            }
            .onSuccess {
                if (it.data.size < 10) stateIsEndOfList = true
                listItemDeviceData.value = ArrayList(listItemDeviceData.value).apply {
                    removeLast()
                }
                listItemDeviceData.value = ArrayList(listItemDeviceData.value).apply {
                    addAll(it.data as MutableList<ItemDeviceData>)
                }
                page += 1
            }
            .onError {
                stateIsEndOfList = true
                listItemDeviceData.value = ArrayList(listItemDeviceData.value).apply {
                    removeLast()
                }
                stateShowSnackBar.value = true
            }
            .onCompletion {
                stateLoadingListMain.value = false
            }
            .launchIn(viewModelScope)
    }
}
