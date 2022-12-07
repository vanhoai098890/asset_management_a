package com.example.assetmanagementapp.ui.consignmentmain

import androidx.lifecycle.viewModelScope
import com.example.app_common.base.viewmodel.BaseViewModelV2
import com.example.app_common.extensions.bindLoading
import com.example.app_common.extensions.onSuccess
import com.example.assetmanagementapp.data.remote.api.model.consignment.ConsignmentItem
import com.example.assetmanagementapp.data.remote.api.model.consignment.ItemConsignmentRequest
import com.example.assetmanagementapp.data.remote.api.model.consignment.SearchListConsignmentRequest
import com.example.assetmanagementapp.data.repositories.ConsignmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

@HiltViewModel
class ConsignmentViewModel @Inject constructor(
    private val consignmentRepository: ConsignmentRepository
) : BaseViewModelV2<ConsignmentState>() {
    override fun initState() = ConsignmentState()

    private var page = 0
    private val size = 10

    init {
        onLoadMore()
    }

    fun onLoadMore() {
        if (currentState.stateLoadingListMain || currentState.isEndOfList) return
        consignmentRepository.getConsignment(
            ItemConsignmentRequest(
                page = page,
                size = size
            )
        ).bindLoading(this).onStart {
            dispatchState(currentState.copy(stateLoadingListMain = true))
        }.onSuccess {
            if (it.data.size < 10) {
                currentState.isEndOfList = true
            }
            dispatchState(
                currentState.copy(
                    stateListConsignment = ArrayList(currentState.stateListConsignment).apply {
                        addAll(
                            it.data
                        )
                    }
                )
            )
            page += 1
        }.onCompletion {
            dispatchState(currentState.copy(stateLoadingListMain = false))
        }.launchIn(viewModelScope)
    }

    fun searchConsignment(searchString: String) {
        if (searchString.isBlank()) {
            currentState.isEndOfList = false
            page = 0
            currentState.stateListConsignment = listOf()
            onLoadMore()
        } else {
            consignmentRepository.searchConsignment(
                SearchListConsignmentRequest(searchString = searchString)
            ).bindLoading(this).onStart {
                dispatchState(currentState.copy(stateLoadingListMain = true))
            }.onSuccess {
                dispatchState(
                    currentState.copy(
                        stateListConsignment = it.data
                    )
                )
            }.onCompletion {
                dispatchState(currentState.copy(stateLoadingListMain = false))
            }.launchIn(viewModelScope)
        }
    }
}

data class ConsignmentState(
    var stateListConsignment: List<ConsignmentItem> = listOf(),
    var isEndOfList: Boolean = false,
    val stateVisibleNotFoundItem: Boolean = false,
    val stateLoadingListMain: Boolean = false
)
