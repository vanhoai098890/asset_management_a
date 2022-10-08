package com.example.assetmanagementapp.data.remote.api.model.setpassword

import com.example.app_common.base.response.CommonResponse
import com.example.assetmanagementapp.data.remote.api.model.signin.response.LoginAuthenticator
import com.google.gson.annotations.SerializedName

data class SetPasswordResponseDto(
    @SerializedName("Data")
    val loginAuthenticator: LoginAuthenticator
) : CommonResponse()
