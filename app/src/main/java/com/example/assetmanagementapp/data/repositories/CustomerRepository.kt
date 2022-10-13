package com.example.assetmanagementapp.data.repositories

import com.example.app_common.extensions.onSuccess
import com.example.app_common.extensions.safeFlow
import com.example.assetmanagementapp.data.local.LoginSessionManager
import com.example.assetmanagementapp.data.remote.api.datasource.customer.CustomerDatasourceImpl
import com.example.assetmanagementapp.data.remote.api.model.resetpassword.InputPhoneRequest
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
        loginSessionManager.saveCustomer(it)
    }
}
