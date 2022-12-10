package com.example.assetmanagementapp.ui.typeassetsheet

import androidx.lifecycle.viewModelScope
import com.example.app_common.base.viewmodel.BaseViewModelV2
import com.example.app_common.extensions.bindLoading
import com.example.app_common.extensions.onError
import com.example.app_common.extensions.onSuccess
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAsset
import com.example.assetmanagementapp.data.repositories.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class TypeAssetViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository
) : BaseViewModelV2<TypeAssetState>() {
    override fun initState(): TypeAssetState {
        return TypeAssetState()
    }

    init {
        getCategories()
    }

    private fun getCategories() {
        deviceRepository.getListCategories().bindLoading(this).onSuccess {
            dispatchState(currentState.copy(listCategory = it.data))
        }.launchIn(viewModelScope)
    }

    fun editCategory(categoryId: Int, categoryName: String) {
        deviceRepository.editCategory(TypeAsset(id = categoryId, typeName = categoryName))
            .bindLoading(this).onSuccess {
                dispatchState(
                    currentState.copy(
                        stateSuccess = true,
                        listCategory = ArrayList(currentState.listCategory).map { typeAsset ->
                            if (typeAsset.id == categoryId) {
                                typeAsset.copy(typeName = categoryName)
                            } else {
                                typeAsset
                            }
                        })
                )
            }.onError {
                dispatchState(currentState.copy(stateSuccess = false))
            }.launchIn(viewModelScope)
    }

    fun addCategory(categoryName: String) {
        deviceRepository.addCategory(TypeAsset(id = 0, typeName = categoryName))
            .bindLoading(this).onSuccess {
                dispatchState(
                    currentState.copy(
                        stateSuccess = true,
                        listCategory = ArrayList(currentState.listCategory).apply {
                            add(it.data)
                        })
                )
            }.onError {
                dispatchState(currentState.copy(stateSuccess = false))
            }.launchIn(viewModelScope)
    }

    fun dispatchResetSnackBar() {
        dispatchState(currentState.copy(stateSuccess = null))
    }
}

data class TypeAssetState(
    val listCategory: List<TypeAsset> = listOf(),
    var departmentId: Int = 0,
    var roomId: Int = 0,
    var stateSuccess: Boolean? = null
)
