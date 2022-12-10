package com.example.assetmanagementapp.data.repositories

import com.example.app_common.extensions.onSuccess
import com.example.app_common.extensions.safeFlow
import com.example.assetmanagementapp.data.local.LoginSessionManager
import com.example.assetmanagementapp.data.remote.api.datasource.customer.CustomerDatasourceImpl
import com.example.assetmanagementapp.data.remote.api.model.customer.ProfileRequest
import com.example.assetmanagementapp.data.remote.api.model.resetpassword.InputPhoneRequest
import com.example.assetmanagementapp.data.remote.api.model.searchdevice.SearchListDeviceRequest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerRepository @Inject constructor(
    private val customerDatasourceImpl: CustomerDatasourceImpl,
    private val loginSessionManager: LoginSessionManager
) {
    fun getUserInfoByPhoneNumber(phoneNumber: String) = safeFlow {
        customerDatasourceImpl.getUserInfoByPhoneNumber(InputPhoneRequest(phoneNumber))
    }.onSuccess {
        loginSessionManager.saveCustomer(it.userInfo)
    }

    fun updateProfile(profileRequest: ProfileRequest) = safeFlow {
        customerDatasourceImpl.updateProfile(profileRequest = profileRequest)
    }

    fun getAllUser() = safeFlow {
        customerDatasourceImpl.getAllUser()
    }

    fun searchUser(searchListDeviceRequest: SearchListDeviceRequest) = safeFlow {
        customerDatasourceImpl.searchUser(searchListDeviceRequest)
    }

    fun getCountry() = safeFlow {
        customerDatasourceImpl.getCountry()
    }

    fun getCity() = safeFlow {
        customerDatasourceImpl.getCity()
    }

    fun getMajor() = safeFlow {
        customerDatasourceImpl.getMajor()
    }
}
