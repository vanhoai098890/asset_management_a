package com.example.assetmanagementapp.data.repositories

import com.example.app_common.extensions.safeFlow
import com.example.assetmanagementapp.data.remote.api.datasource.signup.SignUpRemoteDataSourceImpl
import com.example.assetmanagementapp.data.remote.api.model.checkotp.VerifyOTPRequest
import com.example.assetmanagementapp.data.remote.api.model.resetpassword.InputPhoneRequest
import javax.inject.Inject

class SignUpRepository @Inject constructor(private val signUpDataSource: SignUpRemoteDataSourceImpl) {
    internal fun postPhoneSignUp(inputPhoneRequest: InputPhoneRequest) = safeFlow {
        signUpDataSource.postPhoneSignUp(inputPhoneRequest)
    }

    internal fun getResendOTP(phone: String) = safeFlow {
        signUpDataSource.getResendOTP(phone)
    }

    internal fun postVerifyOTP(verifyOTPRequest: VerifyOTPRequest) = safeFlow {
        signUpDataSource.postVerifyOTP(verifyOTPRequest)
    }
}
