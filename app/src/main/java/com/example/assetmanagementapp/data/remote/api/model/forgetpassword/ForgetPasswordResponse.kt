package com.example.assetmanagementapp.data.remote.api.model.forgetpassword

import com.example.app_common.base.response.CommonResponse
import com.google.gson.annotations.SerializedName

data class ForgetPasswordResponse(
    @SerializedName("Data")
    val data: Any
) : CommonResponse()
