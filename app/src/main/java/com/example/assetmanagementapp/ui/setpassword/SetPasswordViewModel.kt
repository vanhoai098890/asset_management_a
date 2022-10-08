package com.example.assetmanagementapp.ui.setpassword

import android.text.Editable
import com.example.app_common.base.viewmodel.BaseViewModel
import com.example.app_common.constant.AppConstant
import com.example.app_common.extensions.bindLoading
import com.example.app_common.utils.text_watcher.TextWatcherImpl
import com.example.assetmanagementapp.data.remote.api.model.resetpassword.InputPhoneRequest
import com.example.assetmanagementapp.data.repositories.ResetPasswordRepository
import com.example.assetmanagementapp.data.repositories.SignUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SetPasswordViewModel @Inject constructor(
    private val repository: SignUpRepository,
    private val resetPasswordRepository: ResetPasswordRepository,
) : BaseViewModel() {

    companion object {
        private const val SIX_TO_TWENTY_CHARACTERS_PATTERN = ".{8,}"
        private const val AT_LEAST_ONE_LETTER_PATTERN = "[a-zA-Z]+"
        private const val NUMBER_PATTERN = "[0-9]+"
    }

    private val _password: MutableStateFlow<String> = MutableStateFlow(AppConstant.EMPTY)
    private val _confirmPassword: MutableStateFlow<String> = MutableStateFlow(AppConstant.EMPTY)

    internal var phoneNumber: String = AppConstant.EMPTY
    internal var typeFlow: Int = -1
    internal var isPasswordVisible = false
    internal var isRePasswordVisible = false

    val password: StateFlow<String> = _password
    val confirmPassword: StateFlow<String> = _confirmPassword

    val isSixToTweentyCharacters = MutableStateFlow(false)
    val isAtLeastOneLetter = MutableStateFlow(false)
    val isNumber = MutableStateFlow(false)
    val isSpecialCharacters = MutableStateFlow(false)
    val isPasswordMatched = MutableStateFlow(false)
    val enableSetPasswordButton = MutableStateFlow(false)

    val passwordTextWatcher = object : TextWatcherImpl() {
        override fun afterTextChanged(s: Editable?) {
            setPassword(s.toString())
            validateSetPassword()
        }
    }

    val confirmPasswordTextWatcher = object : TextWatcherImpl() {
        override fun afterTextChanged(s: Editable?) {
            seConfirmPassword(s.toString())
            isPasswordMatched(password.value, confirmPassword.value)
            validateSetPassword()
        }
    }

    fun validateSetPassword() {
        enableSetPasswordButton.value =
            isSixToTweentyCharacters.value && isAtLeastOneLetter.value && isNumber.value && isPasswordMatched.value
    }

    fun isPasswordMatched(password: String, confirmPassword: String) {
        if (password.isNotEmpty() && confirmPassword.isNotEmpty()) {
            isPasswordMatched.value = password == confirmPassword
        } else {
            isPasswordMatched.value = false
        }
    }

    internal fun postPhoneSignUp(phoneNumber: String) =
        repository.postPhoneSignUp(InputPhoneRequest(phoneNumber))
            .bindLoading(this)

    internal fun postPhoneResetPassword(phoneNumber: String) =
        resetPasswordRepository.sendOTPResetPassword(phoneNumber)
            .bindLoading(this)

    private fun isSixToTwentyCharacters(password: String) {
        isSixToTweentyCharacters.value = password.matches(
            SIX_TO_TWENTY_CHARACTERS_PATTERN.toRegex()
        )
    }

    private fun isAtLeastOneLetter(password: String) {
        isAtLeastOneLetter.value = password.contains(AT_LEAST_ONE_LETTER_PATTERN.toRegex())
    }

    private fun isNumber(password: String) {
        isNumber.value = password.contains(NUMBER_PATTERN.toRegex())
    }

    private fun seConfirmPassword(value: String) {
        _confirmPassword.value = value
    }

    private fun setPassword(value: String) {
        _password.value = value
        isSixToTwentyCharacters(value)
        isAtLeastOneLetter(value)
        isNumber(value)
        isPasswordMatched(password.value, confirmPassword.value)
    }
}
