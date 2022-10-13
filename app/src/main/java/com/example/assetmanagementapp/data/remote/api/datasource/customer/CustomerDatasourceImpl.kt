package com.example.assetmanagementapp.data.remote.api.datasource.customer

import com.example.app_common.base.exception.apiCall
import com.example.assetmanagementapp.data.remote.ApiService
import com.example.assetmanagementapp.data.remote.api.model.customer.CustomerPropertyResponse
import com.example.assetmanagementapp.data.remote.api.model.customer.UserInfoResponse
import com.example.assetmanagementapp.data.remote.api.model.resetpassword.InputPhoneRequest
import javax.inject.Inject

class CustomerDatasourceImpl @Inject constructor(private val apiService: ApiService) :
    CustomerDatasource {
    override suspend fun getCustomerById(customerId: Int): CustomerPropertyResponse {
        return apiCall {
            apiService.getCustomerById(customerId)
        }
    }

    override suspend fun getUserInfoByPhoneNumber(phoneRequest: InputPhoneRequest): UserInfoResponse {
        return apiCall {
            apiService.getUserInfo(phoneRequest)
        }
    }
}
