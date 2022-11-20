package com.example.assetmanagementapp.ui.personal

import androidx.lifecycle.viewModelScope
import com.example.app_common.base.viewmodel.BaseViewModel
import com.example.app_common.extensions.bindLoading
import com.example.app_common.extensions.onSuccess
import com.example.assetmanagementapp.data.local.LoginSessionManager
import com.example.assetmanagementapp.data.repositories.CustomerRepository
import com.example.assetmanagementapp.data.repositories.PersonalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class PersonalViewModel @Inject constructor(
    private val personalRepository: PersonalRepository,
    private val localSessionManager: LoginSessionManager,
    private val customerRepository: CustomerRepository
) : BaseViewModel() {
    val listPersonal: MutableStateFlow<MutableList<PersonalFunctionStatic>> = MutableStateFlow(
        mutableListOf(
            PersonalFunctionStatic.PERSON(),
            PersonalFunctionStatic.ACCOUNT,
            PersonalFunctionStatic.PERSONAL,
            PersonalFunctionStatic.TRANSACTION,
            PersonalFunctionStatic.PASSWORD,
            PersonalFunctionStatic.MANAGEMENT,
            PersonalFunctionStatic.REQUEST,
            PersonalFunctionStatic.HELP,
            PersonalFunctionStatic.LOGOUT
        )
    )

    fun getCustomerInfo() {
        customerRepository.getUserInfoByPhoneNumber(localSessionManager.getUsername())
            .bindLoading(this)
            .onSuccess { userResponse ->
                userResponse.userInfo.apply {
                    listPersonal.value = ArrayList(listPersonal.value).map {
                        if (it is PersonalFunctionStatic.PERSON) {
                            PersonalFunctionStatic.PERSON(username = username, avatarId = avatarId)
                        } else {
                            it
                        }
                    } as MutableList<PersonalFunctionStatic>
                }
            }
            .launchIn(viewModelScope)
    }

}
