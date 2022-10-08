package com.example.assetmanagementapp.data.remote.api.datasource.sign_in

import com.example.app_common.base.response.CommonResponse
import com.example.assetmanagementapp.data.remote.api.model.signin.request.SignInRequestDto
import com.example.assetmanagementapp.data.remote.api.model.signin.response.SignInResponseDto
import com.example.assetmanagementapp.data.remote.api.model.signup.request.SignUpRequestDto

interface LoginDataSource {
    suspend fun signIn(signInRequestDto: SignInRequestDto): SignInResponseDto
    suspend fun signUp(signUpRequestDto: SignUpRequestDto): CommonResponse
}
