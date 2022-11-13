package com.example.assetmanagementapp.ui.categorysheet

import androidx.lifecycle.viewModelScope
import com.example.app_common.base.viewmodel.BaseViewModelV2
import com.example.app_common.extensions.bindLoading
import com.example.app_common.extensions.onSuccess
import com.example.assetmanagementapp.data.remote.api.model.favourite.DeviceItem
import com.example.assetmanagementapp.data.remote.api.model.searchdevice.SearchListDeviceRequest
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAsset
import com.example.assetmanagementapp.data.repositories.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    deviceRepository: DeviceRepository
) : BaseViewModelV2<CategoryState>() {
    override fun initState(): CategoryState {
        return CategoryState()
    }

    init {
        deviceRepository.getListCategories().bindLoading(this).onSuccess {
            dispatchState(currentState.copy(listCategory = ArrayList(currentState.listCategory).apply {
                add(TypeAsset(0, "All"))
                addAll(it.data)
            }))
        }.launchIn(viewModelScope)
    }
}

data class CategoryState(
    val listCategory: MutableList<TypeAsset> = mutableListOf()
)
