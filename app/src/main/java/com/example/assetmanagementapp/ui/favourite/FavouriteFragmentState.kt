package com.example.assetmanagementapp.ui.favourite

import com.example.assetmanagementapp.data.remote.api.model.favourite.DeviceItem

data class FavouriteFragmentState(
    var stateIsShowSnackBar: Boolean? = null,
    var isEndOfList: Boolean = false,
    val phoneNumber: String? = null,
    val stateListFavouriteRoom: List<DeviceItem> = mutableListOf(),
    val stateVisibleNotFoundItem: Boolean = false,
    val stateLoadingListMain: Boolean = false
)