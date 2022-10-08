package com.example.assetmanagementapp.ui.sign_up

import android.text.Editable
import androidx.lifecycle.viewModelScope
import com.example.app_common.base.exception.DefaultError
import com.example.app_common.base.response.ApiResponseCode
import com.example.app_common.base.viewmodel.BaseViewModel
import com.example.app_common.extensions.bindLoading
import com.example.app_common.extensions.getFormatString
import com.example.app_common.extensions.onError
import com.example.app_common.extensions.onSuccess
import com.example.app_common.extensions.validateCitizenId
import com.example.app_common.extensions.validateEmail
import com.example.app_common.extensions.validatePhone
import com.example.app_common.utils.text_watcher.TextWatcherImpl
import com.example.assetmanagementapp.data.remote.api.model.signup.request.SignUpRequestDto
import com.example.assetmanagementapp.data.repositories.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : BaseViewModel() {
    companion object {
        const val NORMAL = 0
        const val WRONG_EMAIL = 1
        const val WRONG_PHONE = 2
        const val USERNAME_EXISTED = 3
        const val REQUIRE_FIELD = 4
        const val REQUIRE_FIELD_NOT_BLANK = 5
        const val WRONG_CITIZEN = 6
    }

    var isPasswordVisible = false
    val statusNavToOnBoardScreen = MutableStateFlow(false)
    val name = MutableStateFlow("")
    val nameWatcherImpl = object : TextWatcherImpl() {
        override fun afterTextChanged(s: Editable?) {
            name.value = s.toString()
            isShowError.value = NORMAL
        }
    }
    val email = MutableStateFlow("")
    val emailWatcherImpl = object : TextWatcherImpl() {
        override fun afterTextChanged(s: Editable?) {
            email.value = s.toString()
            isShowError.value = NORMAL
        }
    }

    val citizenId = MutableStateFlow("")
    val citizenIdImpl = object : TextWatcherImpl() {
        override fun afterTextChanged(s: Editable?) {
            citizenId.value = s.toString()
            isShowError.value = NORMAL
        }
    }
    val phone = MutableStateFlow("")
    val phoneWatcherImpl = object : TextWatcherImpl() {
        override fun afterTextChanged(s: Editable?) {
            phone.value = s.toString()
            isShowError.value = NORMAL
        }
    }
    val username = MutableStateFlow("")
    val usernameWatcherImpl = object : TextWatcherImpl() {
        override fun afterTextChanged(s: Editable?) {
            username.value = s.toString()
            isShowError.value = NORMAL
        }
    }
    val password = MutableStateFlow("")
    val passwordWatcherImpl = object : TextWatcherImpl() {
        override fun afterTextChanged(s: Editable?) {
            password.value = s.toString()
            isShowError.value = NORMAL
        }
    }
    val isShowError = MutableStateFlow(NORMAL)

    fun signUpAction() {
        isShowError.value = NORMAL
        if (name.value.isBlank()) {
            isShowError.value = REQUIRE_FIELD_NOT_BLANK
            return
        }
        if (!email.value.validateEmail()) {
            isShowError.value = WRONG_EMAIL
            return
        }
        if (!citizenId.value.validateCitizenId()) {
            isShowError.value = WRONG_CITIZEN
            return
        }
        if (!phone.value.validatePhone()) {
            isShowError.value = WRONG_PHONE
            return
        }
        if (username.value.length < 6 || password.value.length < 6) {
            isShowError.value = REQUIRE_FIELD
        } else {
            loginRepository.signUp(
                SignUpRequestDto(
                    customerName = name.value,
                    email = email.value,
                    phoneNumber = phone.value,
                    username = username.value,
                    password = password.value,
                    citizenId = citizenId.value,
                    birthday = Date().getFormatString()
                )
            )
                .bindLoading(this)
                .onError {
                    if (it is DefaultError) {
                        if (it.apiErrorCode?.code == ApiResponseCode.E402.code) {
                            isShowError.value = USERNAME_EXISTED
                        }
                    }
                }.onSuccess {
                    statusNavToOnBoardScreen.value = true
                }.launchIn(viewModelScope)
        }
    }
}
