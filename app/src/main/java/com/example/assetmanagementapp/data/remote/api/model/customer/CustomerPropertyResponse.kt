package com.example.assetmanagementapp.data.remote.api.model.customer

import com.example.app_common.base.response.CommonResponse
import com.google.gson.annotations.SerializedName

data class CustomerPropertyResponse(
    @SerializedName("data")
    val data: CustomerProperty
) : CommonResponse()

data class CustomerProperty(
    @SerializedName("customerId")
    val customerId: Int? = null,
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("customerName")
    val customerName: String? = null,
    @SerializedName("citizenId")
    val citizenId: String? = null,
    @SerializedName("birthday")
    var birthday: String? = null,
    @SerializedName("nationality")
    var nationality: String? = null,
    @SerializedName("phoneNumber")
    val phoneNumber: String? = null,
)
