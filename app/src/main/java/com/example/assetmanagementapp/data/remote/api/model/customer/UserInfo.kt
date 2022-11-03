package com.example.assetmanagementapp.data.remote.api.model.customer

import com.example.app_common.base.response.CommonResponse
import com.google.gson.annotations.SerializedName

data class UserInfoResponse(
    @SerializedName("data")
    val userInfo: UserInfo
) : CommonResponse()

data class UserInfo(
    val address: String = "",
    val birthday: String = "",
    val city: City? = null,
    val cmnd: String = "",
    val country: Country? = null,
    val email: String = "",
    val major: Major? = null,
    val phoneNumber: String = "",
    val sex: String = "",
    val userId: Int = 0,
    val username: String = "hoai ho",
    val avatarId: Int = 10
)

data class Major(
    val majorId: Int,
    val nameMajor: String
)

data class Country(
    val countryId: Int,
    val nameCountry: String
)

data class City(
    val cityId: Int,
    val cityName: String
)