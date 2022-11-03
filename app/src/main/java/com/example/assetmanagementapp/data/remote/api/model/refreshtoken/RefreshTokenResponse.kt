package com.example.assetmanagementapp.data.remote.api.model.refreshtoken

import com.example.app_common.base.response.CommonResponse
import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(
    @SerializedName("data")
    val data: RefreshTokenData
) : CommonResponse()

data class RefreshTokenData(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String
)
