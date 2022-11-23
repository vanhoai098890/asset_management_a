package com.example.assetmanagementapp.data.remote.api.datasource.signup

import com.example.app_common.base.response.CommonResponse
import com.example.assetmanagementapp.data.remote.api.model.checkotp.VerifyOTPRequest
import com.example.assetmanagementapp.data.remote.api.model.forgetpassword.InputPhoneResponse
import com.example.assetmanagementapp.data.remote.api.model.resetpassword.InputPhoneRequest

interface SignUpRemoteDataSource {
    suspend fun postPhoneSignUp(phoneRequest: InputPhoneRequest): InputPhoneResponse

    suspend fun getResendOTP(phone: String): CommonResponse

    suspend fun postVerifyOTP(verifyOTPRequest: VerifyOTPRequest): CommonResponse
}
