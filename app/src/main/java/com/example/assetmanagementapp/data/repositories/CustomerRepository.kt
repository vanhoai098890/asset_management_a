package com.example.assetmanagementapp.data.repositories

import com.example.app_common.extensions.onSuccess
import com.example.app_common.extensions.safeFlow
import com.example.assetmanagementapp.data.local.LoginSessionManager
import com.example.assetmanagementapp.data.remote.api.datasource.customer.CustomerDatasourceImpl
import com.example.assetmanagementapp.data.remote.api.model.customer.CustomerProperty
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CustomerRepository @Inject constructor(
    private val customerDatasourceImpl: CustomerDatasourceImpl,
    private val loginSessionManager: LoginSessionManager
) {
    fun getCustomerById(customerId: Int) = safeFlow {
        customerDatasourceImpl.getCustomerById(customerId)
    }

    fun updateCustomerById(customerRequest: CustomerProperty) = safeFlow {
        customerDatasourceImpl.updateCustomerById(
            customerId = customerRequest.customerId ?: 0,
            customerRequest
        )
    }.onSuccess {
        loginSessionManager.saveCustomer(customerRequest)
    }
}
