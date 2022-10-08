package com.example.assetmanagementapp.data.remote.api.model.signin.response

import com.example.app_common.base.response.CommonResponse
import com.google.gson.annotations.SerializedName

data class SignInResponseDto(
    @SerializedName("data")
    val loginAuthenticator: LoginAuthenticator
) : CommonResponse()
