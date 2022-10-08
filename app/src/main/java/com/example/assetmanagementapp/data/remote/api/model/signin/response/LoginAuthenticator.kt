package com.example.assetmanagementapp.data.remote.api.model.signin.response

import com.example.assetmanagementapp.data.remote.api.model.customer.CustomerProperty
import com.google.gson.annotations.SerializedName

data class LoginAuthenticator(
    @SerializedName("id")
    val accountId: Int,
    @SerializedName("userId")
    val userId: Int,
    @SerializedName("token")
    var jwtToken: String,
    @SerializedName("type")
    var type: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("role")
    val role: Int,
    @SerializedName("customer")
    val customer: CustomerProperty
)
