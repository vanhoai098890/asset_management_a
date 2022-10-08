package com.example.assetmanagementapp.data.remote.api.datasource.sign_in

import com.example.app_common.base.exception.apiCall
import com.example.app_common.base.response.CommonResponse
import com.example.assetmanagementapp.data.remote.ApiService
import com.example.assetmanagementapp.data.remote.api.model.signin.request.SignInRequestDto
import com.example.assetmanagementapp.data.remote.api.model.signin.response.SignInResponseDto
import com.example.assetmanagementapp.data.remote.api.model.signup.request.SignUpRequestDto
import javax.inject.Inject

class LoginDataSourceImpl @Inject constructor(private val apiService: ApiService) :
    LoginDataSource {
    override suspend fun signIn(signInRequestDto: SignInRequestDto): SignInResponseDto {
        return apiCall {
            apiService.signIn(signInRequestDto)
        }
    }

    override suspend fun signUp(signUpRequestDto: SignUpRequestDto): CommonResponse {
        return apiCall {
            apiService.signUp(signUpRequestDto)
        }
    }
}
