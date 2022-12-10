package com.example.assetmanagementapp.ui.category

import com.example.app_common.base.viewmodel.BaseViewModelV2
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
) : BaseViewModelV2<CategoryState>() {
    override fun initState() = CategoryState()

}

data class CategoryState(
    val any: Any = ""
)
