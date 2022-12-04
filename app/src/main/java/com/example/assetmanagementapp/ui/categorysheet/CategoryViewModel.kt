package com.example.assetmanagementapp.ui.categorysheet

import androidx.lifecycle.viewModelScope
import com.example.app_common.base.viewmodel.BaseViewModelV2
import com.example.app_common.extensions.bindLoading
import com.example.app_common.extensions.onSuccess
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAsset
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAssetRequest
import com.example.assetmanagementapp.data.repositories.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository
) : BaseViewModelV2<CategoryState>() {
    override fun initState(): CategoryState {
        return CategoryState()
    }

    fun getCategories(){
        deviceRepository.getListCategories(
            TypeAssetRequest(
                departmentId = currentState.departmentId,
                roomId = currentState.roomId
            )
        ).bindLoading(this).onSuccess {
            dispatchState(currentState.copy(listCategory = ArrayList(currentState.listCategory).apply {
                add(TypeAsset(0, "All", numberOfAssets = it.data.sumOf { it.numberOfAssets }))
                addAll(it.data)
            }))
        }.launchIn(viewModelScope)
    }
}

data class CategoryState(
    val listCategory: MutableList<TypeAsset> = mutableListOf(),
    var departmentId: Int = 0,
    var roomId: Int = 0
)
