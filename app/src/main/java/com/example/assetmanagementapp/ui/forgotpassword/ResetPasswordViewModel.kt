package com.example.assetmanagementapp.ui.forgotpassword

import android.text.Editable
import com.example.app_common.base.viewmodel.BaseViewModel
import com.example.app_common.extensions.FlowResult
import com.example.app_common.extensions.bindLoading
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.data.remote.api.model.forgetpassword.InputPhoneResponse
import com.example.assetmanagementapp.data.repositories.ResetPasswordRepository
import com.example.assetmanagementapp.utils.PhoneFormatTextWatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val repository: ResetPasswordRepository,
) : BaseViewModel() {

    companion object {
        private const val PHONE_NUMBER_LENGTH: Int = 10
    }

    private val _phoneNumber: MutableStateFlow<String> = MutableStateFlow("")
    private val _disableResetPassword: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val phoneNumber: StateFlow<String> = _phoneNumber
    val disableLogin: StateFlow<Boolean> = _disableResetPassword
    val errorMessage: MutableStateFlow<Int> = MutableStateFlow(-1)
    val phoneNumberTextWatcher = object : PhoneFormatTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            super.afterTextChanged(s)
            errorMessage.value = 0
            _phoneNumber.value = rawPhone
            checkPhoneNumberPasswordIsEmpty()
        }
    }

    fun checkPhoneNumberPasswordIsEmpty() {
        _disableResetPassword.value = phoneNumber.value.isNotEmpty()
    }

    fun validationPhoneNumber(value: String) {
        if (!isPhoneNumberEqual11Digits(value)) {
            errorMessage.value = R.string.common_phone_number_length_error
            _disableResetPassword.value = false
        } else {
            errorMessage.value = 0
            _disableResetPassword.value = true
        }
    }

    fun isPhoneNumberEqual11Digits(inputPhoneNumber: String): Boolean {
        //TODO: accept phone number with length 10 for testing
        return inputPhoneNumber.length >= PHONE_NUMBER_LENGTH
    }

    fun resetPassword(): Flow<FlowResult<InputPhoneResponse>> {
        return repository.checkPhoneResetPassword(_phoneNumber.value).bindLoading(this)
    }

    fun setErrorMessage(errorCodeResponse: Int) {
        errorMessage.value = errorCodeResponse
    }

    fun enableNextButton(isEnable: Boolean) {
        _disableResetPassword.value = isEnable
    }
}
