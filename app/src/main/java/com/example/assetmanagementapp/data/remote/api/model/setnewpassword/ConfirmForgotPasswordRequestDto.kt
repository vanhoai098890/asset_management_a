package com.example.assetmanagementapp.data.remote.api.model.setnewpassword

import com.google.gson.annotations.SerializedName

data class ConfirmForgotPasswordRequestDto(
    @SerializedName("phoneNumber")
    val username: String,
    @SerializedName("otp")
    val confirmationCode: String
)
