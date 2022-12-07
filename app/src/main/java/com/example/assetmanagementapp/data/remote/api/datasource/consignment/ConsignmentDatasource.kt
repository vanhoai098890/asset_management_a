package com.example.assetmanagementapp.data.remote.api.datasource.consignment

import com.example.app_common.base.response.CommonResponse
import com.example.assetmanagementapp.data.remote.api.model.consignment.ConsignmentItem
import com.example.assetmanagementapp.data.remote.api.model.consignment.ConsignmentItemResponse
import com.example.assetmanagementapp.data.remote.api.model.consignment.ItemConsignmentRequest
import com.example.assetmanagementapp.data.remote.api.model.consignment.SearchListConsignmentRequest
import okhttp3.MultipartBody

interface ConsignmentDatasource {
    suspend fun getConsignment(itemConsignmentRequest: ItemConsignmentRequest): ConsignmentItemResponse
    suspend fun searchConsignment(searchListConsignmentRequest: SearchListConsignmentRequest): ConsignmentItemResponse
    suspend fun addConsignment(
        consignmentItem: ConsignmentItem,
        file: MultipartBody.Part
    ): CommonResponse

    suspend fun editConsignment(
        consignmentItem: ConsignmentItem,
        file: MultipartBody.Part
    ): CommonResponse

    suspend fun editConsignment(
        consignmentItem: ConsignmentItem
    ): CommonResponse
}
