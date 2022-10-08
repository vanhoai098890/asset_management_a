package com.example.assetmanagementapp.data.remote.api.datasource.customer

import com.example.app_common.base.exception.apiCall
import com.example.app_common.base.response.CommonResponse
import com.example.assetmanagementapp.data.remote.ApiService
import com.example.assetmanagementapp.data.remote.api.model.customer.CustomerProperty
import com.example.assetmanagementapp.data.remote.api.model.customer.CustomerPropertyResponse
import javax.inject.Inject

class CustomerDatasourceImpl @Inject constructor(private val apiService: ApiService) :
    CustomerDatasource {
    override suspend fun getCustomerById(customerId: Int): CustomerPropertyResponse {
        return apiCall {
            apiService.getCustomerById(customerId)
        }
    }

    override suspend fun updateCustomerById(
        customerId: Int,
        customerRequest: CustomerProperty
    ): CommonResponse {
        return apiCall {
            apiService.updateCustomerById(customerId, customerRequest)
        }
    }
}
