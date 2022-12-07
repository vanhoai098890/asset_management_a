package com.example.assetmanagementapp.data.remote.api.datasource.provider

import com.example.app_common.base.exception.apiCall
import com.example.assetmanagementapp.data.remote.ApiService
import com.example.assetmanagementapp.data.remote.api.model.provider.ProviderItemResponse
import javax.inject.Inject

class ProviderDatasourceImpl @Inject constructor(private val apiService: ApiService) :
    ProviderDatasource {
    override suspend fun getAllProvider(): ProviderItemResponse {
        return apiCall {
            apiService.getProviders()
        }
    }
}
