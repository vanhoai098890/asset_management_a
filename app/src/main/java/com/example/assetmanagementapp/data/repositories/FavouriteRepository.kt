package com.example.assetmanagementapp.data.repositories

import com.example.app_common.extensions.safeFlow
import com.example.assetmanagementapp.data.remote.api.datasource.favourite.FavouriteDatasourceImpl
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavouriteRepository @Inject constructor(
    private val favouriteDatasourceImpl: FavouriteDatasourceImpl
) {
    fun getFavouriteDevices(phoneNumber: String, page: Int, size: Int) = safeFlow {
        favouriteDatasourceImpl.getFavouriteDevices(
            phoneNumber = phoneNumber,
            page = page,
            size = size
        )
    }

    fun saveDevices(phoneNumber: String, deviceId: Int, isSave: Boolean) = safeFlow {
        favouriteDatasourceImpl.saveDevices(
            phoneNumber = phoneNumber,
            devicesId = deviceId,
            isSave = isSave
        )
    }
}
