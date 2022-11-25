package com.example.assetmanagementapp.data.repositories


import com.example.app_common.base.response.CommonResponse
import com.example.app_common.extensions.FlowResult
import com.example.app_common.extensions.safeFlow
import com.example.assetmanagementapp.data.remote.api.datasource.resetpassword.ForgetPasswordDataSourceImpl
import com.example.assetmanagementapp.data.remote.api.model.changepassword.ChangePasswordRequest
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResetPasswordRepository @Inject constructor(private val dataSource: ForgetPasswordDataSourceImpl) {
    internal fun sendOTPResetPassword(phoneNumber: String, password: String) = safeFlow {
        dataSource.getForgetPasswordInfo(phoneNumber, password)
    }

    internal fun confirmForgotPassword(
        phoneNumber: String,
        password: String,
        confirmationCode: String
    ): Flow<FlowResult<CommonResponse>> = safeFlow {
        dataSource.confirmForgotPassword(phoneNumber, password, confirmationCode)
    }

    internal fun checkPhoneResetPassword(phoneRequest: String) = safeFlow {
        dataSource.checkPhoneResetPassword(phoneRequest)
    }

    internal fun changePassword(changePasswordRequest: ChangePasswordRequest) = safeFlow {
        dataSource.changePassword(changePasswordRequest)
    }
}
