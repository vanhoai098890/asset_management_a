package com.example.assetmanagementapp.data.remote.api.model.setpassword

import com.google.gson.annotations.SerializedName

data class SetPasswordRequestDto(
    @SerializedName("username")
    val username:String,
    @SerializedName("password")
    val password:String
)
