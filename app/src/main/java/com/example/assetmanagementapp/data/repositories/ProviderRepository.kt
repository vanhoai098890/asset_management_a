package com.example.assetmanagementapp.data.repositories

import com.example.app_common.extensions.safeFlow
import com.example.assetmanagementapp.data.remote.api.datasource.provider.ProviderDatasourceImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProviderRepository @Inject constructor(
    private val providerDatasourceImpl: ProviderDatasourceImpl,
) {
    fun getAllProvider() = safeFlow {
        providerDatasourceImpl.getAllProvider()
    }
}
