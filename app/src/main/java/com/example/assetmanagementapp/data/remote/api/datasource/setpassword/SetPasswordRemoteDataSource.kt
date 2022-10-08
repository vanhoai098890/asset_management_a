package com.example.assetmanagementapp.data.remote.api.datasource.setpassword

import com.example.assetmanagementapp.data.remote.api.model.setpassword.SetPasswordResponseDto

interface SetPasswordRemoteDataSource {
    suspend fun getSetPasswordInfo(username: String, password: String): SetPasswordResponseDto
}
