package com.example.assetmanagementapp.ui.usermanagement

import androidx.lifecycle.viewModelScope
import com.example.app_common.base.viewmodel.BaseViewModelV2
import com.example.app_common.extensions.bindLoading
import com.example.app_common.extensions.onSuccess
import com.example.assetmanagementapp.data.remote.api.model.customer.UserInfo
import com.example.assetmanagementapp.data.remote.api.model.searchdevice.SearchListDeviceRequest
import com.example.assetmanagementapp.data.repositories.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class UserManagementViewModel @Inject constructor(
    private val customerRepository: CustomerRepository
) : BaseViewModelV2<UserManagementState>() {
    override fun initState() = UserManagementState()

    init {
        getAllUser()
    }

    fun getAllUser() {
        customerRepository.getAllUser().bindLoading(this).onSuccess {
            dispatchState(currentState.copy(listUserInfo = it.data))
        }.launchIn(viewModelScope)
    }

    fun searchUser(nameUser: String) {
        customerRepository.searchUser(SearchListDeviceRequest(searchString = nameUser)).onSuccess {
            dispatchState(currentState.copy(listUserInfo = it.data))
        }.launchIn(viewModelScope)
    }

}

data class UserManagementState(
    val listUserInfo: List<UserInfo> = listOf()
)
