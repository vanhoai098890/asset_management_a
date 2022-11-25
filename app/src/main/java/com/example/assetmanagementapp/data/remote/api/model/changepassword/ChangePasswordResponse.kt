package com.example.assetmanagementapp.data.remote.api.model.changepassword

import com.example.app_common.base.response.CommonResponse
import com.google.gson.annotations.SerializedName

data class ChangePasswordResponse(
    @SerializedName("data")
    val data: Boolean
) : CommonResponse()
