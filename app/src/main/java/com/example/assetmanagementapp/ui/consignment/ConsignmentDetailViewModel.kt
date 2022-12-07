package com.example.assetmanagementapp.ui.consignment

import com.example.app_common.base.viewmodel.BaseViewModelV2
import com.example.assetmanagementapp.data.remote.api.model.consignment.ConsignmentItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConsignmentDetailViewModel @Inject constructor(
) : BaseViewModelV2<ConsignmentDetailState>() {
    override fun initState() = ConsignmentDetailState()

    fun dispatchStateConsignment(consignmentItem: ConsignmentItem) {
        dispatchState(currentState.copy(stateConsignment = consignmentItem))
    }

}

data class ConsignmentDetailState(
    val stateConsignment: ConsignmentItem? = null,
    val stateVisibleMask: Boolean = false
)
