package com.example.assetmanagementapp.data.remote.api.model.customer

import android.os.Parcelable
import com.example.app_common.base.response.CommonResponse
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class UserInfoResponse(
    @SerializedName("data")
    val userInfo: UserInfo
) : CommonResponse()

@Parcelize
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
) : Parcelable

@Parcelize
data class Major(
    val majorId: Int,
    val nameMajor: String
) : Parcelable

@Parcelize
data class Country(
    val countryId: Int,
    val nameCountry: String
) : Parcelable

@Parcelize
data class City(
    val cityId: Int,
    val cityName: String
) : Parcelable