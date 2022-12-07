package com.example.assetmanagementapp.ui.edtconsignment

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
class EditConsignmentViewModel @Inject constructor(
    private val consignmentRepository: ConsignmentRepository
) : BaseViewModelV2<EditConsignmentState>() {
    override fun initState() = EditConsignmentState()

    fun dispatchCategory(typeAsset: TypeAsset) {
        dispatchState(currentState.copy(currentCategory = typeAsset))
    }

    fun dispatchProvider(providerItem: ProviderItem) {
        dispatchState(currentState.copy(currentProvider = providerItem))
    }

    fun edtConsignment(
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
        if (currentState.currentCategory == null || currentState.currentProvider == null) return
        val requestFile =
            imageFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData(
            "file",
            imageFile.name,
            requestFile
        )
        val requestItem = ConsignmentItem(
            consignmentId = currentState.stateConsignmentItem?.consignmentId ?: 0,
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
        )

        if (currentState.currentUri == null) {
            consignmentRepository.editConsignment(
                consignmentItem = requestItem
            )
                .bindLoading(this)
                .onSuccess {
                    dispatchState(
                        currentState.copy(
                            stateShowSnackBarSuccess = true,
                            stateConsignmentItem = ConsignmentItem(
                                consignmentId = currentState.stateConsignmentItem?.consignmentId
                                    ?: 0,
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
                            )
                        )
                    )
                }
                .onError {
                    dispatchState(currentState.copy(stateShowSnackBarSuccess = false))
                }
                .launchIn(viewModelScope)
        } else {
            consignmentRepository.editConsignment(
                consignmentItem = requestItem,
                file = multipartBody
            )
                .bindLoading(this)
                .onSuccess {
                    dispatchState(
                        currentState.copy(
                            stateShowSnackBarSuccess = true,
                            stateConsignmentItem = ConsignmentItem(
                                consignmentId = currentState.stateConsignmentItem?.consignmentId
                                    ?: 0,
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
                                providerId = currentState.currentProvider?.providerId ?: 0,
                                uriImage = currentState.currentUri
                            )
                        )
                    )
                }
                .onError {
                    dispatchState(currentState.copy(stateShowSnackBarSuccess = false))
                }
                .launchIn(viewModelScope)
        }
    }

    fun dispatchResetSnackBar() {
        dispatchState(currentState.copy(stateShowSnackBarSuccess = null))
    }

    fun dispatchStateConsignmentItem(consignmentItem: ConsignmentItem) {
        dispatchState(
            currentState.copy(
                stateConsignmentItem = consignmentItem,
                currentCategory = TypeAsset(
                    id = consignmentItem.typeAssetId,
                    typeName = consignmentItem.typeAssetName
                ),
                currentProvider = ProviderItem(
                    providerId = consignmentItem.providerId,
                    name = consignmentItem.providerName
                )
            )
        )
    }
}

data class EditConsignmentState(
    val any: Any = "",
    val currentCategory: TypeAsset? = null,
    val currentProvider: ProviderItem? = null,
    var currentUri: Uri? = null,
    val stateAddSuccess: Boolean = false,
    val stateShowSnackBarSuccess: Boolean? = null,
    val stateConsignmentItem: ConsignmentItem? = null
)
