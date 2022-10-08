package com.example.assetmanagementapp.data.remote.api.model.signin.request

import com.google.gson.annotations.SerializedName

data class SignInRequestDto(
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("password")
    val password: String
)
