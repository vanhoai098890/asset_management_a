package com.example.assetmanagementapp.data.remote.api.model.department

import com.example.app_common.base.response.CommonResponse
import com.google.gson.annotations.SerializedName

data class DepartmentItemResponse(
    @SerializedName("data")
    val data: List<DepartmentItem>
) : CommonResponse()

data class AddDepartmentItemResponse(
    @SerializedName("data")
    val data: DepartmentItem
) : CommonResponse()

data class DepartmentItem(
    @SerializedName("departmentId")
    val departmentId: Int,
    @SerializedName("departmentName")
    val departmentName: String,
    @SerializedName("numberOfRooms")
    val numberOfRooms: Int,
    @SerializedName("numberOfAssets")
    val numberOfAssets: Int
)
