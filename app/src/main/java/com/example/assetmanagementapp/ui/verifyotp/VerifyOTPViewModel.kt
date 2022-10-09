package com.example.assetmanagementapp.ui.verifyotp

import android.view.View
import com.example.app_common.base.exception.DefaultError
import com.example.app_common.base.response.CommonResponse
import com.example.app_common.base.viewmodel.BaseViewModel
import com.example.app_common.extensions.FlowResult
import com.example.app_common.extensions.bindError
import com.example.app_common.extensions.bindLoading
import com.example.app_common.extensions.countDownTimer
import com.example.app_common.extensions.onError
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.data.remote.api.model.checkotp.VerifyOTPRequest
import com.example.assetmanagementapp.data.remote.api.model.setpassword.SetPasswordResponseDto
import com.example.assetmanagementapp.data.repositories.ResetPasswordRepository
import com.example.assetmanagementapp.data.repositories.SetPasswordRepository
import com.example.assetmanagementapp.data.repositories.SignUpRepository
import com.example.assetmanagementapp.ui.forgotpassword.SetPasswordTypeFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

/**
 * @IdScreen S01003
 *
 * Canow
 * <p>
 * Created by FPT.
 */
@HiltViewModel
class VerifyOTPViewModel @Inject constructor(
    private val repository: SignUpRepository,
    private val setPasswordRepository: SetPasswordRepository,
    private val resetPasswordRepository: ResetPasswordRepository,
    private val signUpRepository: SignUpRepository
) :
    BaseViewModel() {
    companion object {
        private const val RESEND_OTP_DURATION = 60
    }

    internal var phoneNumber: String = ""
    internal var password: String = ""
    internal var typeFlow: Int = -1

    val isEnableResendOTP = MutableStateFlow(false)
    val stateViewError = MutableStateFlow(View.GONE)
    val messageError = MutableStateFlow(R.string.common_message_error_e002)

    internal fun apiToResendOTP(): Flow<FlowResult<CommonResponse>> {
        return if (typeFlow == SetPasswordTypeFlow.FLOW_SIGN_UP.ordinal) {
            repository.getResendOTP(phoneNumber)
                .bindLoading(this)
                .bindError(this)
        } else {
            signUpRepository.getResendOTP(phoneNumber)
                .bindLoading(this)
        }
    }

    internal fun apiToVerifyOTP(
        enteredOTP: String,
        typeFlow: Int
    ): Flow<FlowResult<CommonResponse>> =
        if (typeFlow == SetPasswordTypeFlow.FLOW_SIGN_UP.ordinal) {
            repository.postVerifyOTP(VerifyOTPRequest(phoneNumber, enteredOTP))
                .bindLoading(this)
                .bindError(this)
                .onError {
                    (it as? DefaultError)?.let { error ->
                        stateViewError.value = View.VISIBLE
                        messageError.value = error.apiErrorCode?.idStringResource ?: 0
                    }
                }
        } else {
            resetPasswordRepository.confirmForgotPassword(phoneNumber, password, enteredOTP)
                .bindLoading(this)
                .bindError(this)
                .onError {
                    (it as? DefaultError)?.let { error ->
                        stateViewError.value = View.VISIBLE
                        messageError.value = error.apiErrorCode?.idStringResource ?: 0
                    }
                }
        }

    internal fun initCountDownResendOTP() = countDownTimer(RESEND_OTP_DURATION).onStart {
        isEnableResendOTP.value = false
    }.onCompletion {
        isEnableResendOTP.value = true
    }

    internal fun postSignupSetPassword(): Flow<FlowResult<SetPasswordResponseDto>> =
        setPasswordRepository.getSetPasswordInfo(
            phoneNumber,
            password
        ).bindLoading(this)
}
