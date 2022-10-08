package com.example.assetmanagementapp.data.remote.api.datasource.setpassword

import com.example.app_common.base.exception.apiCall
import com.example.assetmanagementapp.data.remote.ApiService
import com.example.assetmanagementapp.data.remote.api.model.setpassword.SetPasswordRequestDto
import com.example.assetmanagementapp.data.remote.api.model.setpassword.SetPasswordResponseDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SetPasswordRemoteDataSourceImpl @Inject constructor(private val apiService: ApiService) :
    SetPasswordRemoteDataSource {
    override suspend fun getSetPasswordInfo(
        username: String,
        password: String
    ): SetPasswordResponseDto = apiCall {
        apiService.getSetPasswordInfo(SetPasswordRequestDto(username, password))
    }
}
