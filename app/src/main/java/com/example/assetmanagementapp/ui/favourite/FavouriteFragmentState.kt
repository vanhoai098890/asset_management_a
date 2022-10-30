package com.example.assetmanagementapp.ui.favourite

import com.example.assetmanagementapp.data.remote.api.model.customer.UserInfo
import com.example.assetmanagementapp.data.remote.api.model.favourite.DeviceItem

data class FavouriteFragmentState(
    var stateIsShowSnackBar: Boolean? = null,
    val stateCurrentUserInfo: UserInfo? = null,
    val stateListFavouriteRoom: List<DeviceItem> = mutableListOf(),
    val stateVisibleNotFoundItem: Boolean = false
)