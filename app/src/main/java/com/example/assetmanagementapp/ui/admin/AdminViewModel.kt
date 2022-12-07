package com.example.assetmanagementapp.ui.admin

import com.example.app_common.base.viewmodel.BaseViewModelV2
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
) : BaseViewModelV2<AdminState>() {
    override fun initState() = AdminState()
}

data class AdminState(
    val any: Any = ""
)
