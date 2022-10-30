package com.example.assetmanagementapp.data.remote.api.datasource.favourite

import com.example.app_common.base.exception.apiCall
import com.example.app_common.base.response.CommonResponse
import com.example.assetmanagementapp.data.remote.ApiService
import com.example.assetmanagementapp.data.remote.api.model.device.ListMainDeviceRequest
import com.example.assetmanagementapp.data.remote.api.model.favourite.DeviceItemResponse
import com.example.assetmanagementapp.data.remote.api.model.favourite.SaveDeviceRequest
import javax.inject.Inject

class FavouriteDatasourceImpl @Inject constructor(private val apiService: ApiService) :
    FavouriteDataSource {

    override suspend fun getFavouriteDevices(
        phoneNumber: String,
        page: Int,
        size: Int
    ): DeviceItemResponse {
        return apiCall {
            apiService.getFavouriteDevices(
                ListMainDeviceRequest(
                    phoneNumber = phoneNumber,
                    page = page,
                    size = size
                )
            )
        }
    }

    override suspend fun saveDevices(
        phoneNumber: String,
        devicesId: Int,
        isSave: Boolean
    ): CommonResponse {
        return apiCall {
            if (isSave) {
                apiService.saveDevice(
                    SaveDeviceRequest(
                        phoneNumber = phoneNumber,
                        assetId = devicesId
                    )
                )
            } else {
                apiService.unSaveDevice(
                    SaveDeviceRequest(
                        phoneNumber = phoneNumber,
                        assetId = devicesId
                    )
                )
            }

        }
    }
}
