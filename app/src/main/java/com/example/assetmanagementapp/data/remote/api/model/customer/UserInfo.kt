package com.example.assetmanagementapp.data.remote.api.model.customer

import com.example.app_common.base.response.CommonResponse
import com.google.gson.annotations.SerializedName

data class UserInfoResponse(
    @SerializedName("data")
    val userInfo: UserInfo
) : CommonResponse()

data class UserInfo(
    val address: String,
    val birthday: String,
    val city: City,
    val cmnd: String,
    val country: Country,
    val email: String,
    val major: Major,
    val phoneNumber: String,
    val sex: String,
    val userId: Int,
    val username: String
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