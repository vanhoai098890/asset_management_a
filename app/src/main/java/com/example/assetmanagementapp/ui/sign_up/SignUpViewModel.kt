package com.example.assetmanagementapp.ui.sign_up

import android.text.Editable
import androidx.lifecycle.viewModelScope
import com.example.app_common.base.exception.DefaultError
import com.example.app_common.base.response.ApiResponseCode
import com.example.app_common.base.viewmodel.BaseViewModel
import com.example.app_common.extensions.bindLoading
import com.example.app_common.extensions.onError
import com.example.app_common.extensions.onSuccess
import com.example.app_common.extensions.validateCitizenId
import com.example.app_common.extensions.validateEmail
import com.example.app_common.extensions.validatePhone
import com.example.app_common.utils.text_watcher.TextWatcherImpl
import com.example.assetmanagementapp.data.remote.api.model.customer.UserInfo
import com.example.assetmanagementapp.data.remote.api.model.customer.toTypeAsset
import com.example.assetmanagementapp.data.remote.api.model.signup.request.SignUpRequestDto
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAsset
import com.example.assetmanagementapp.data.repositories.CustomerRepository
import com.example.assetmanagementapp.data.repositories.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val customerRepository: CustomerRepository
) : BaseViewModel() {
    companion object {
        const val NORMAL = 0
        const val WRONG_EMAIL = 1
        const val WRONG_PHONE = 2
        const val USERNAME_EXISTED = 3
        const val REQUIRE_FIELD = 4
        const val REQUIRE_FIELD_NOT_BLANK = 5
        const val WRONG_CITIZEN = 6
        const val SOMETHING_WRONG = 7
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

    val stateBirthday = MutableStateFlow("")

    val stateCity: MutableStateFlow<TypeAsset?> = MutableStateFlow(null)
    val stateListCity: MutableStateFlow<List<TypeAsset>> = MutableStateFlow(listOf())

    val stateCountry: MutableStateFlow<TypeAsset?> = MutableStateFlow(null)
    val stateListCountry: MutableStateFlow<List<TypeAsset>> = MutableStateFlow(listOf())

    val stateMajor: MutableStateFlow<TypeAsset?> = MutableStateFlow(null)
    val stateListMajor: MutableStateFlow<List<TypeAsset>> = MutableStateFlow(listOf())

    val stateRole: MutableStateFlow<TypeAsset?> = MutableStateFlow(null)
    val stateListRole: MutableStateFlow<List<TypeAsset>> = MutableStateFlow(
        listOf(
            TypeAsset(id = 1, typeName = "User"),
            TypeAsset(id = 2, typeName = "Admin")
        )
    )
    val citizenId = MutableStateFlow("")
    val citizenIdImpl = object : TextWatcherImpl() {
        override fun afterTextChanged(s: Editable?) {
            citizenId.value = s.toString()
            isShowError.value = NORMAL
        }
    }
    val stateAddress = MutableStateFlow("")
    val stateAddressImpl = object : TextWatcherImpl() {
        override fun afterTextChanged(s: Editable?) {
            stateAddress.value = s.toString()
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
    val password = MutableStateFlow("")
    val passwordWatcherImpl = object : TextWatcherImpl() {
        override fun afterTextChanged(s: Editable?) {
            password.value = s.toString()
            isShowError.value = NORMAL
        }
    }
    val isShowError = MutableStateFlow(NORMAL)

    init {
        customerRepository.getCountry().onSuccess {
            stateListCountry.value = it.data.map { data ->
                TypeAsset(id = data.countryId, typeName = data.nameCountry)
            }
        }.launchIn(viewModelScope)
        customerRepository.getCity().onSuccess {
            stateListCity.value = it.data.map { data ->
                TypeAsset(id = data.cityId, typeName = data.cityName)
            }
        }.launchIn(viewModelScope)
        customerRepository.getMajor().onSuccess {
            stateListMajor.value = it.data.map { data ->
                TypeAsset(id = data.majorId, typeName = data.nameMajor)
            }
        }.launchIn(viewModelScope)
    }

    private val stateUserInfo: MutableStateFlow<UserInfo?> = MutableStateFlow(null)

    fun dispatchStateUserInfo(userInfo: UserInfo) {
        stateUserInfo.value = userInfo
        name.value = userInfo.username
        email.value = userInfo.email
        stateBirthday.value = userInfo.birthday.substring(0, 10)
        stateCity.value = userInfo.city?.toTypeAsset()
        stateCountry.value = userInfo.country?.toTypeAsset()
        stateMajor.value = userInfo.major?.toTypeAsset()
        stateRole.value =
            if (userInfo.isAdmin) TypeAsset(id = 1, typeName = "Admin")
            else TypeAsset(id = 2, typeName = "User")
        citizenId.value = userInfo.cmnd
        stateAddress.value = userInfo.address
        phone.value = userInfo.phoneNumber
    }

    fun signUpAction() {
        isShowError.value = NORMAL
        when {
            name.value.isBlank() -> {
                isShowError.value = REQUIRE_FIELD_NOT_BLANK
            }
            !email.value.validateEmail() -> {
                isShowError.value = WRONG_EMAIL
            }
            !citizenId.value.validateCitizenId() -> {
                isShowError.value = WRONG_CITIZEN
            }
            stateBirthday.value.isEmpty() -> {
                isShowError.value = REQUIRE_FIELD_NOT_BLANK
            }
            stateAddress.value.isEmpty() -> {
                isShowError.value = REQUIRE_FIELD_NOT_BLANK
            }
            stateCity.value == null -> {
                isShowError.value = REQUIRE_FIELD_NOT_BLANK
            }
            stateCountry.value == null -> {
                isShowError.value = REQUIRE_FIELD_NOT_BLANK
            }
            stateMajor.value == null -> {
                isShowError.value = REQUIRE_FIELD_NOT_BLANK
            }
            stateRole.value == null -> {
                isShowError.value = REQUIRE_FIELD_NOT_BLANK
            }
            !phone.value.validatePhone() -> {
                isShowError.value = WRONG_PHONE
            }
            password.value.length < 6 -> {
                isShowError.value = REQUIRE_FIELD
            }
            else -> {
                if (stateUserInfo.value == null) {
                    loginRepository.signUp(
                        SignUpRequestDto(
                            customerName = name.value,
                            email = email.value,
                            phoneNumber = phone.value,
                            password = password.value,
                            birthday = stateBirthday.value,
                            cityId = stateCity.value?.id ?: 0,
                            countryId = stateCountry.value?.id ?: 0,
                            majorId = stateMajor.value?.id ?: 0,
                            roleId = stateRole.value?.id ?: 0,
                            address = stateAddress.value,
                            cmnd = citizenId.value
                        )
                    )
                        .bindLoading(this)
                        .onError {
                            if (it is DefaultError) {
                                if (it.apiErrorCode?.code == ApiResponseCode.E402.code) {
                                    isShowError.value = USERNAME_EXISTED
                                } else {
                                    isShowError.value = SOMETHING_WRONG
                                }
                            }
                        }.onSuccess {
                            statusNavToOnBoardScreen.value = true
                        }.launchIn(viewModelScope)
                } else {
                    loginRepository.editUser(
                        SignUpRequestDto(
                            customerName = name.value,
                            email = email.value,
                            phoneNumber = phone.value,
                            password = password.value,
                            birthday = stateBirthday.value,
                            cityId = stateCity.value?.id ?: 0,
                            countryId = stateCountry.value?.id ?: 0,
                            majorId = stateMajor.value?.id ?: 0,
                            roleId = stateRole.value?.id ?: 0,
                            address = stateAddress.value,
                            cmnd = citizenId.value
                        )
                    )
                        .bindLoading(this)
                        .onError {
                            if (it is DefaultError) {
                                isShowError.value = SOMETHING_WRONG
                            }
                        }.onSuccess {
                            statusNavToOnBoardScreen.value = true
                        }.launchIn(viewModelScope)
                }
            }
        }
    }
}
