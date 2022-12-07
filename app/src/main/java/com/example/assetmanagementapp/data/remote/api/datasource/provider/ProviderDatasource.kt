package com.example.assetmanagementapp.data.remote.api.datasource.provider

import com.example.assetmanagementapp.data.remote.api.model.provider.ProviderItemResponse

interface ProviderDatasource {
    suspend fun getAllProvider(): ProviderItemResponse
}
