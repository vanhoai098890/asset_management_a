package com.example.assetmanagementapp.data.remote.api.model.searchdevice

data class SearchListDeviceRequest(
    val searchString: String = "",
    val departmentId: Int = 0,
    val roomId: Int = 0,
    val category: Int = 0,
    val page: Int = 0,
    val size: Int = 10
)