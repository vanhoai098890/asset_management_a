package com.example.assetmanagementapp.data.remote.api.datasource.customer

import com.example.app_common.base.response.CommonResponse
import com.example.assetmanagementapp.data.remote.api.model.customer.CustomerPropertyResponse
import com.example.assetmanagementapp.data.remote.api.model.customer.ProfileRequest
import com.example.assetmanagementapp.data.remote.api.model.customer.UserInfoResponse
import com.example.assetmanagementapp.data.remote.api.model.resetpassword.InputPhoneRequest

interface CustomerDatasource {
    suspend fun getCustomerById(customerId: Int): CustomerPropertyResponse
    suspend fun getUserInfoByPhoneNumber(phoneRequest: InputPhoneRequest): UserInfoResponse
    suspend fun updateProfile(profileRequest: ProfileRequest): CommonResponse
}
