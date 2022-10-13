package com.example.assetmanagementapp.ui.searchmain

import com.example.app_common.base.viewmodel.BaseViewModel
import com.example.app_common.extensions.bindLoading
import com.example.app_common.extensions.onSuccess
import com.example.assetmanagementapp.data.local.LoginSessionManager
import com.example.assetmanagementapp.data.remote.api.model.customer.UserInfo
import com.example.assetmanagementapp.data.repositories.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchMainViewModel @Inject constructor(
    loginSessionManager: LoginSessionManager,
    customerRepository: CustomerRepository
) : BaseViewModel() {
    val stateUserInfo: MutableStateFlow<UserInfo?> = MutableStateFlow(null)
    val listItemDeviceData: MutableStateFlow<MutableList<ItemDeviceData>> = MutableStateFlow(
        mutableListOf()
    )

    init {
        customerRepository.getUserInfoByPhoneNumber(loginSessionManager.getUsername())
            .bindLoading(this).onSuccess {
                stateUserInfo.value = it.userInfo
            }
        listItemDeviceData.value = ArrayList(listItemDeviceData.value).apply {
            addAll(
                mutableListOf(
                    ItemDeviceData(
                        name = "Device 1",
                        image = "https://images.microcms-assets.io/assets/1a4f9c91d3b5437ba7410c8fcc5d55c1/cc3f909fca2248ebba7f49bcec969d91/thumb-1920-438767.png"
                    ),
                    ItemDeviceData(
                        name = "Device 2",
                        image = "https://images.microcms-assets.io/assets/1a4f9c91d3b5437ba7410c8fcc5d55c1/cc3f909fca2248ebba7f49bcec969d91/thumb-1920-438767.png"
                    ),
                    ItemDeviceData(
                        name = "Device 3",
                        image = "https://images.microcms-assets.io/assets/1a4f9c91d3b5437ba7410c8fcc5d55c1/cc3f909fca2248ebba7f49bcec969d91/thumb-1920-438767.png"
                    ),
                    ItemDeviceData(
                        name = "Device 4",
                        image = "https://images.microcms-assets.io/assets/1a4f9c91d3b5437ba7410c8fcc5d55c1/cc3f909fca2248ebba7f49bcec969d91/thumb-1920-438767.png"
                    ),
                    ItemDeviceData(
                        name = "Device 5",
                        image = "https://images.microcms-assets.io/assets/1a4f9c91d3b5437ba7410c8fcc5d55c1/cc3f909fca2248ebba7f49bcec969d91/thumb-1920-438767.png"
                    ),
                    ItemDeviceData(
                        name = "Device 6",
                        image = "https://images.microcms-assets.io/assets/1a4f9c91d3b5437ba7410c8fcc5d55c1/cc3f909fca2248ebba7f49bcec969d91/thumb-1920-438767.png"
                    ),
                    ItemDeviceData(
                        name = "Device 7",
                        image = "https://images.microcms-assets.io/assets/1a4f9c91d3b5437ba7410c8fcc5d55c1/cc3f909fca2248ebba7f49bcec969d91/thumb-1920-438767.png"
                    ),
                    ItemDeviceData(
                        name = "Device 8",
                        image = "https://images.microcms-assets.io/assets/1a4f9c91d3b5437ba7410c8fcc5d55c1/cc3f909fca2248ebba7f49bcec969d91/thumb-1920-438767.png"
                    ),
                    ItemDeviceData(
                        name = "Device 9",
                        image = "https://images.microcms-assets.io/assets/1a4f9c91d3b5437ba7410c8fcc5d55c1/cc3f909fca2248ebba7f49bcec969d91/thumb-1920-438767.png"
                    )
                )
            )
        }
    }
}
