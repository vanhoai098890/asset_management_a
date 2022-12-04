package com.example.assetmanagementapp.ui.searchmain

import androidx.lifecycle.viewModelScope
import com.example.app_common.base.viewmodel.BaseViewModel
import com.example.app_common.extensions.onError
import com.example.app_common.extensions.onSuccess
import com.example.app_common.utils.LogUtils
import com.example.assetmanagementapp.data.local.LoginSessionManager
import com.example.assetmanagementapp.data.remote.api.model.customer.UserInfo
import com.example.assetmanagementapp.data.remote.api.model.infomain.InfoMain
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAsset
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAssetRequest
import com.example.assetmanagementapp.data.repositories.CustomerRepository
import com.example.assetmanagementapp.data.repositories.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class SearchMainViewModel @Inject constructor(
    private val loginSessionManager: LoginSessionManager,
    private val customerRepository: CustomerRepository,
    private val deviceRepository: DeviceRepository
) : BaseViewModel() {
    val stateUserInfo: MutableStateFlow<UserInfo?> = MutableStateFlow(null)
    val listCategory: MutableStateFlow<List<TypeAsset>> = MutableStateFlow(mutableListOf())
    val stateValueTotalDepreciation: MutableStateFlow<Float> = MutableStateFlow(0f)
    val stateInfoMain: MutableStateFlow<InfoMain?> = MutableStateFlow(null)
    var stateOldIndex = -1
    var stateIsAdmin = MutableStateFlow(loginSessionManager.isAdmin())

    fun getCustomerInfo() {
        customerRepository.getUserInfoByPhoneNumber(loginSessionManager.getUsername())
            .onSuccess {
                stateUserInfo.value = it.userInfo
            }
            .onError {
                LogUtils.d("${it.message}")
            }
            .launchIn(viewModelScope)
    }

    fun getListCategory() {
        deviceRepository.getListCategories(TypeAssetRequest()).onSuccess {
            var totalDepreciation = 0f
            val listResult =
                it.data.map { typeAsset -> typeAsset.copy(totalDepreciation = typeAsset.totalDepreciation * 1000) }
            listResult.forEach { typeAsset ->
                totalDepreciation += typeAsset.totalDepreciation
            }
            stateValueTotalDepreciation.value = totalDepreciation
            listCategory.value = listResult.map { item ->
                listCategory.value.find { typeAsset -> typeAsset.id == item.id }?.apply {
                    if (item.totalDepreciation == this.totalDepreciation) {
                        return@map this
                    } else {
                        return@map item
                    }
                } ?: item
            }
        }.launchIn(viewModelScope)
    }

    fun getInfoMain() {
        deviceRepository.getInfoMain().onSuccess {
            stateInfoMain.value = it.data
        }.launchIn(viewModelScope)
    }
}
