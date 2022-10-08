package com.example.assetmanagementapp.data.remote.api.model.resetpassword

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequestDto(
    @SerializedName("phoneNumber")
    val phoneNumber: String,
    @SerializedName("password")
    val password: String
)
