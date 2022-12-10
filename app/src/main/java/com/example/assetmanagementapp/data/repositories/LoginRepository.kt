package com.example.assetmanagementapp.data.repositories

import com.example.app_common.extensions.safeFlow
import com.example.assetmanagementapp.data.remote.api.datasource.sign_in.LoginDataSourceImpl
import com.example.assetmanagementapp.data.remote.api.model.signin.request.SignInRequestDto
import com.example.assetmanagementapp.data.remote.api.model.signup.request.SignUpRequestDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(private val signInDataSourceImpl: LoginDataSourceImpl) {
    fun signIn(signInRequestDto: SignInRequestDto) = safeFlow {
        signInDataSourceImpl.signIn(signInRequestDto)
    }

    fun signUp(signUpRequestDto: SignUpRequestDto) = safeFlow {
        signInDataSourceImpl.signUp(signUpRequestDto)
    }

    fun editUser(signUpRequestDto: SignUpRequestDto) = safeFlow {
        signInDataSourceImpl.editUser(signUpRequestDto)
    }
}
