package com.example.assetmanagementapp.data.remote.api.datasource.customer

import com.example.app_common.base.exception.apiCall
import com.example.app_common.base.response.CommonResponse
import com.example.assetmanagementapp.data.remote.ApiService
import com.example.assetmanagementapp.data.remote.api.model.customer.CityListResponse
import com.example.assetmanagementapp.data.remote.api.model.customer.CountryListResponse
import com.example.assetmanagementapp.data.remote.api.model.customer.CustomerPropertyResponse
import com.example.assetmanagementapp.data.remote.api.model.customer.ListUserInfoResponse
import com.example.assetmanagementapp.data.remote.api.model.customer.MajorListResponse
import com.example.assetmanagementapp.data.remote.api.model.customer.ProfileRequest
import com.example.assetmanagementapp.data.remote.api.model.customer.UserInfoResponse
import com.example.assetmanagementapp.data.remote.api.model.resetpassword.InputPhoneRequest
import com.example.assetmanagementapp.data.remote.api.model.searchdevice.SearchListDeviceRequest
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

    override suspend fun updateProfile(profileRequest: ProfileRequest): CommonResponse {
        return apiCall {
            apiService.updateProfiles(profileRequest = profileRequest)
        }
    }

    override suspend fun getAllUser(): ListUserInfoResponse {
        return apiCall {
            apiService.getAllUser()
        }
    }

    override suspend fun searchUser(searchListDeviceRequest: SearchListDeviceRequest): ListUserInfoResponse {
        return apiCall {
            apiService.searchUser(searchListDeviceRequest)
        }
    }

    override suspend fun getCountry(): CountryListResponse {
        return apiCall {
            apiService.getCountry()
        }
    }

    override suspend fun getCity(): CityListResponse {
        return apiCall {
            apiService.getCity()
        }
    }

    override suspend fun getMajor(): MajorListResponse {
        return apiCall {
            apiService.getMajor()
        }
    }
}
