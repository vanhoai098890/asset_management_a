package com.example.assetmanagementapp.data.repositories

import com.example.app_common.extensions.safeFlow
import com.example.assetmanagementapp.data.remote.api.datasource.consignment.ConsignmentDatasourceImpl
import com.example.assetmanagementapp.data.remote.api.model.consignment.ConsignmentItem
import com.example.assetmanagementapp.data.remote.api.model.consignment.ItemConsignmentRequest
import com.example.assetmanagementapp.data.remote.api.model.consignment.SearchListConsignmentRequest
import okhttp3.MultipartBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConsignmentRepository @Inject constructor(
    private val consignmentDatasourceImpl: ConsignmentDatasourceImpl,
) {

    fun getConsignment(itemConsignmentRequest: ItemConsignmentRequest) = safeFlow {
        consignmentDatasourceImpl.getConsignment(itemConsignmentRequest)
    }

    fun searchConsignment(searchListConsignmentRequest: SearchListConsignmentRequest) = safeFlow {
        consignmentDatasourceImpl.searchConsignment(searchListConsignmentRequest)
    }

    fun addConsignment(consignmentItem: ConsignmentItem, file: MultipartBody.Part) = safeFlow {
        consignmentDatasourceImpl.addConsignment(consignmentItem, file)
    }

    fun editConsignment(consignmentItem: ConsignmentItem, file: MultipartBody.Part) = safeFlow {
        consignmentDatasourceImpl.editConsignment(consignmentItem, file)
    }

    fun editConsignment(consignmentItem: ConsignmentItem) = safeFlow {
        consignmentDatasourceImpl.editConsignment(consignmentItem)
    }
}
