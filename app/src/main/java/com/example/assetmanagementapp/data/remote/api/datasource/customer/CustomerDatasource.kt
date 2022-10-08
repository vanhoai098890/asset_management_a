package com.example.assetmanagementapp.data.remote.api.datasource.customer

import com.example.app_common.base.response.CommonResponse
import com.example.assetmanagementapp.data.remote.api.model.customer.CustomerProperty
import com.example.assetmanagementapp.data.remote.api.model.customer.CustomerPropertyResponse

interface CustomerDatasource {
    suspend fun getCustomerById(customerId: Int): CustomerPropertyResponse
    suspend fun updateCustomerById(
        customerId: Int, customerRequest: CustomerProperty
    ): CommonResponse
}
