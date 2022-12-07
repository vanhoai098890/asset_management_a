package com.example.assetmanagementapp.ui.addconsignment

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.example.app_common.base.viewmodel.BaseViewModelV2
import com.example.app_common.extensions.bindLoading
import com.example.app_common.extensions.onError
import com.example.app_common.extensions.onSuccess
import com.example.assetmanagementapp.data.remote.api.model.consignment.ConsignmentItem
import com.example.assetmanagementapp.data.remote.api.model.provider.ProviderItem
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAsset
import com.example.assetmanagementapp.data.repositories.ConsignmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddConsignmentViewModel @Inject constructor(
    private val consignmentRepository: ConsignmentRepository
) : BaseViewModelV2<AddConsignmentState>() {
    override fun initState() = AddConsignmentState()

    fun dispatchCategory(typeAsset: TypeAsset) {
        dispatchState(currentState.copy(currentCategory = typeAsset))
    }

    fun dispatchProvider(providerItem: ProviderItem) {
        dispatchState(currentState.copy(currentProvider = providerItem))
    }

    fun addConsignment(
        consignmentName: String,
        assetName: String,
        numberOfAsset: Int,
        unitPrice: Int,
        brand: String,
        dateIn: String,
        dateManufacture: String,
        dateWarranty: String,
        description: String,
        imageFile: File
    ) {
        if (currentState.currentUri == null || currentState.currentCategory == null || currentState.currentProvider == null) return
        val requestFile =
            imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val multipartBody =
            MultipartBody.Part.createFormData("file", imageFile.name, requestFile)

        consignmentRepository.addConsignment(
            consignmentItem = ConsignmentItem(
                name = assetName,
                brand = brand,
                consignmentName = consignmentName,
                number = numberOfAsset,
                unitPrice = unitPrice.toDouble(),
                dateManufacture = dateManufacture,
                dateWarranty = dateWarranty,
                description = description,
                dateIn = dateIn,
                typeAssetId = currentState.currentCategory?.id ?: 0,
                providerId = currentState.currentProvider?.providerId ?: 0
            ),
            file = multipartBody
        )
            .bindLoading(this)
            .onSuccess {
                dispatchState(currentState.copy(stateShowSnackBarSuccess = true))
            }
            .onError {
                dispatchState(currentState.copy(stateShowSnackBarSuccess = false))
            }
            .launchIn(viewModelScope)
    }

    fun dispatchResetSnackBar() {
        dispatchState(currentState.copy(stateShowSnackBarSuccess = null))
    }
}

data class AddConsignmentState(
    val any: Any = "",
    val currentCategory: TypeAsset? = null,
    val currentProvider: ProviderItem? = null,
    var currentUri: Uri? = null,
    val stateAddSuccess: Boolean = false,
    val stateShowSnackBarSuccess: Boolean? = null
)
