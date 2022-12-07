package com.example.assetmanagementapp.ui.providersheet

import androidx.lifecycle.viewModelScope
import com.example.app_common.base.viewmodel.BaseViewModelV2
import com.example.app_common.extensions.bindLoading
import com.example.app_common.extensions.onSuccess
import com.example.assetmanagementapp.data.remote.api.model.provider.ProviderItem
import com.example.assetmanagementapp.data.repositories.ProviderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class ProviderViewModel @Inject constructor(
    private val providerRepository: ProviderRepository
) : BaseViewModelV2<ProviderState>() {
    override fun initState(): ProviderState {
        return ProviderState()
    }

    init {
        getProvider()
    }

    private fun getProvider() {
        providerRepository.getAllProvider().bindLoading(this).onSuccess {
            dispatchState(currentState.copy(listProvider = it.data))
        }.launchIn(viewModelScope)
    }
}

data class ProviderState(
    val currentProvider: ProviderItem? = null,
    val listProvider: List<ProviderItem> = listOf()
)
