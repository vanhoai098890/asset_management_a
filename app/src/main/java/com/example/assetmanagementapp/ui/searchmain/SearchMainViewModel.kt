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
import javax.inject.Inject

@HiltViewModel
class SearchMainViewModel @Inject constructor(
    private val loginSessionManager: LoginSessionManager,
    customerRepository: CustomerRepository,
    private val deviceRepository: DeviceRepository
) : BaseViewModel() {

    private var page = 0
    private val size = 10
    val stateUserInfo: MutableStateFlow<UserInfo?> = MutableStateFlow(null)
    val listItemDeviceData: MutableStateFlow<MutableList<ItemDeviceData>> = MutableStateFlow(
        mutableListOf()
    )
    val stateShowSnackBar = MutableStateFlow(false)

    init {
        customerRepository.getUserInfoByPhoneNumber(loginSessionManager.getUsername())
            .bindLoading(this).onSuccess {
                stateUserInfo.value = it.userInfo
            }
        deviceRepository.getListSearchMain(
            ListMainDeviceRequest(
                phoneNumber = loginSessionManager.getUsername(),
                page = page,
                size = size
            )
        ).bindLoading(this).onSuccess {
            listItemDeviceData.value = it.data as MutableList<ItemDeviceData>
            page += 1
        }.onError {
            stateShowSnackBar.value = true
        }.launchIn(viewModelScope)
    }

    fun onLoadMore() {
        deviceRepository.getListSearchMain(
            ListMainDeviceRequest(
                phoneNumber = loginSessionManager.getUsername(),
                page = page,
                size = size
            )
        ).bindLoading(this).onSuccess {
            listItemDeviceData.value.addAll(it.data as MutableList<ItemDeviceData>)
            page += 1
        }.onError {
            stateShowSnackBar.value = true
        }.launchIn(viewModelScope)
    }
}
