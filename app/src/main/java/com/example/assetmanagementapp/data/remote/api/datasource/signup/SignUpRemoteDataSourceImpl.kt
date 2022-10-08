package com.example.assetmanagementapp.data.remote.api.datasource.signup

import com.example.app_common.base.exception.apiCall
import com.example.app_common.base.response.CommonResponse
import com.example.assetmanagementapp.data.remote.ApiService
import com.example.assetmanagementapp.data.remote.api.model.checkotp.VerifyOTPRequest
import com.example.assetmanagementapp.data.remote.api.model.forgetpassword.InputPhoneResponse
import com.example.assetmanagementapp.data.remote.api.model.resetpassword.InputPhoneRequest
import javax.inject.Inject

class SignUpRemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) :
    SignUpRemoteDataSource {
    override suspend fun postPhoneSignUp(phoneRequest: InputPhoneRequest): InputPhoneResponse =
        apiCall {
            apiService.postPhoneSignUp(phoneRequest)
        }

    override suspend fun getResendOTP(phone: String): CommonResponse = apiCall {
        apiService.getOtpResendInfo(InputPhoneRequest(phone))
    }

    override suspend fun postVerifyOTP(verifyOTPRequest: VerifyOTPRequest): CommonResponse =
        apiCall {
            apiService.postVerifyOTP(verifyOTPRequest)
        }
}
