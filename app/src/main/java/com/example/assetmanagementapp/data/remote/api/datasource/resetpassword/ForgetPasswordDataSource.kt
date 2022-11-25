package com.example.assetmanagementapp.data.remote.api.datasource.resetpassword

import com.example.app_common.base.response.CommonResponse
import com.example.assetmanagementapp.data.remote.api.model.changepassword.ChangePasswordRequest
import com.example.assetmanagementapp.data.remote.api.model.changepassword.ChangePasswordResponse
import com.example.assetmanagementapp.data.remote.api.model.forgetpassword.ForgetPasswordResponse
import com.example.assetmanagementapp.data.remote.api.model.forgetpassword.InputPhoneResponse

interface ForgetPasswordDataSource {
    suspend fun getForgetPasswordInfo(username: String, password: String): ForgetPasswordResponse

    suspend fun confirmForgotPassword(
        phoneNumber: String,
        password: String,
        confirmationCode: String
    ): CommonResponse

    suspend fun checkPhoneResetPassword(phoneRequest: String): InputPhoneResponse

    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): ChangePasswordResponse
}
