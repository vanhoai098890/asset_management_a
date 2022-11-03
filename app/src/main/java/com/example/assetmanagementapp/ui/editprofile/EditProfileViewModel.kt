package com.example.assetmanagementapp.ui.editprofile

import androidx.lifecycle.viewModelScope
import com.example.app_common.base.viewmodel.BaseViewModelV2
import com.example.app_common.extensions.bindLoading
import com.example.app_common.extensions.onSuccess
import com.example.assetmanagementapp.data.local.LoginSessionManager
import com.example.assetmanagementapp.data.remote.api.model.customer.ProfileRequest
import com.example.assetmanagementapp.data.repositories.CustomerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val loginSessionManager: LoginSessionManager,
    private val userRepository: CustomerRepository
) :
    BaseViewModelV2<StateEditProfile>() {
    override fun initState(): StateEditProfile = StateEditProfile()

    init {
        userRepository.getUserInfoByPhoneNumber(loginSessionManager.getUsername()).bindLoading(this)
            .onSuccess { userInfo ->
                dispatchState(
                    currentState.copy(
                        stateListAvatars = (0..14).map {
                            if (it == userInfo.userInfo.avatarId) {
                                Avatars(numberImage = it, isSelect = true)
                            } else {
                                Avatars(numberImage = it, isSelect = false)
                            }
                        },
                        stateNickName = userInfo.userInfo.username
                    )
                )
            }.launchIn(viewModelScope)
    }

    fun updateProfile(username: String, avatarId: Int) {
        userRepository.updateProfile(
            ProfileRequest(
                phoneNumber = loginSessionManager.getUsername(),
                userName = username,
                avatarId = avatarId
            )
        ).bindLoading(this).onSuccess {
            dispatchState(currentState.copy(stateOnSuccess = true))
        }.onCompletion {
            dispatchState(currentState.copy(stateOnSuccess = false))
        }.launchIn(viewModelScope)
    }
}
