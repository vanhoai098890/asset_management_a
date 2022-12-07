package com.example.assetmanagementapp.ui.addasset

import com.example.app_common.base.viewmodel.BaseViewModelV2
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddAssetViewModel @Inject constructor(
) : BaseViewModelV2<AddAssetState>() {
    override fun initState() = AddAssetState()
}

data class AddAssetState(
    val any: Any = ""
)
