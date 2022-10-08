package com.example.assetmanagementapp.data.remote.api.datasource.resetpassword

import com.example.app_common.base.exception.apiCall
import com.example.app_common.base.response.CommonResponse
import com.example.assetmanagementapp.data.remote.ApiService
import com.example.assetmanagementapp.data.remote.api.model.forgetpassword.ForgetPasswordRequestDto
import com.example.assetmanagementapp.data.remote.api.model.forgetpassword.ForgetPasswordResponse
import com.example.assetmanagementapp.data.remote.api.model.forgetpassword.InputPhoneResponse
import com.example.assetmanagementapp.data.remote.api.model.resetpassword.InputPhoneRequest
import com.example.assetmanagementapp.data.remote.api.model.setnewpassword.ConfirmForgotPasswordRequestDto
import javax.inject.Inject

class ForgetPasswordDataSourceImpl @Inject constructor(private val apiService: ApiService) :
    ForgetPasswordDataSource {
    override suspend fun getForgetPasswordInfo(username: String): ForgetPasswordResponse =
        apiCall {
            apiService.forgetPassword(ForgetPasswordRequestDto(username))
        }

    override suspend fun confirmForgotPassword(
        phoneNumber: String,
        password: String,
        confirmationCode: String
    ): CommonResponse = apiCall {
        apiService.confirmForgotPassword(
            ConfirmForgotPasswordRequestDto(
                phoneNumber,
                confirmationCode
            )
        )
    }

    override suspend fun checkPhoneResetPassword(phoneRequest: String): InputPhoneResponse =
        apiCall {
            apiService.checkPhoneNumberResetPassword(InputPhoneRequest(phoneRequest))
        }
}
