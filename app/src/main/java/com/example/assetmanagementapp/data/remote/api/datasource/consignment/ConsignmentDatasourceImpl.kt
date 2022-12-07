package com.example.assetmanagementapp.data.remote.api.datasource.consignment

import com.example.app_common.base.exception.apiCall
import com.example.app_common.base.response.CommonResponse
import com.example.assetmanagementapp.data.remote.ApiService
import com.example.assetmanagementapp.data.remote.api.model.consignment.ConsignmentItem
import com.example.assetmanagementapp.data.remote.api.model.consignment.ConsignmentItemResponse
import com.example.assetmanagementapp.data.remote.api.model.consignment.ItemConsignmentRequest
import com.example.assetmanagementapp.data.remote.api.model.consignment.SearchListConsignmentRequest
import okhttp3.MultipartBody
import javax.inject.Inject

class ConsignmentDatasourceImpl @Inject constructor(private val apiService: ApiService) :
    ConsignmentDatasource {

    override suspend fun getConsignment(itemConsignmentRequest: ItemConsignmentRequest): ConsignmentItemResponse {
        return apiCall {
            apiService.getConsignment(itemConsignmentRequest)
        }
    }

    override suspend fun searchConsignment(searchListConsignmentRequest: SearchListConsignmentRequest): ConsignmentItemResponse {
        return apiCall {
            apiService.searchConsignment(searchListConsignmentRequest)
        }
    }

    override suspend fun addConsignment(
        consignmentItem: ConsignmentItem,
        file: MultipartBody.Part
    ): CommonResponse {
        return apiCall {
            apiService.addConsignment(consignmentItem, file)
        }
    }

    override suspend fun editConsignment(
        consignmentItem: ConsignmentItem,
        file: MultipartBody.Part
    ): CommonResponse {
        return apiCall {
            apiService.editConsignment(consignmentItem, file)
        }
    }

    override suspend fun editConsignment(
        consignmentItem: ConsignmentItem
    ): CommonResponse {
        return apiCall {
            apiService.editConsignment(consignmentItem)
        }
    }

}
