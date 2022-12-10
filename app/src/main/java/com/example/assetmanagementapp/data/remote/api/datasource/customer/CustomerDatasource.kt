package com.example.assetmanagementapp.data.remote.api.datasource.customer

import com.example.app_common.base.response.CommonResponse
import com.example.assetmanagementapp.data.remote.api.model.customer.CityListResponse
import com.example.assetmanagementapp.data.remote.api.model.customer.CountryListResponse
import com.example.assetmanagementapp.data.remote.api.model.customer.CustomerPropertyResponse
import com.example.assetmanagementapp.data.remote.api.model.customer.ListUserInfoResponse
import com.example.assetmanagementapp.data.remote.api.model.customer.MajorListResponse
import com.example.assetmanagementapp.data.remote.api.model.customer.ProfileRequest
import com.example.assetmanagementapp.data.remote.api.model.customer.UserInfoResponse
import com.example.assetmanagementapp.data.remote.api.model.resetpassword.InputPhoneRequest
import com.example.assetmanagementapp.data.remote.api.model.searchdevice.SearchListDeviceRequest

interface CustomerDatasource {
    suspend fun getCustomerById(customerId: Int): CustomerPropertyResponse
    suspend fun getUserInfoByPhoneNumber(phoneRequest: InputPhoneRequest): UserInfoResponse
    suspend fun updateProfile(profileRequest: ProfileRequest): CommonResponse
    suspend fun getAllUser(): ListUserInfoResponse
    suspend fun searchUser(searchListDeviceRequest: SearchListDeviceRequest): ListUserInfoResponse
    suspend fun getCountry(): CountryListResponse
    suspend fun getCity(): CityListResponse
    suspend fun getMajor(): MajorListResponse
}
