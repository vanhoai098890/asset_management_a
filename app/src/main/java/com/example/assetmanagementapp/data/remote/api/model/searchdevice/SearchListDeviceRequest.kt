package com.example.assetmanagementapp.data.remote.api.model.searchdevice

data class SearchListDeviceRequest(
    val searchString: String = "",
    val category: Int = 0,
    val page: Int = 0,
    val size: Int = 10
)