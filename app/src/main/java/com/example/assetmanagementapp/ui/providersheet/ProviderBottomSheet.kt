package com.example.assetmanagementapp.ui.providersheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_common.base.BaseBottomSheetDialogFragment
import com.example.assetmanagementapp.data.remote.api.model.provider.ProviderItem
import com.example.assetmanagementapp.databinding.FragmentBottomSheetCaregoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProviderBottomSheet :
    BaseBottomSheetDialogFragment() {
    var currentProvider: Int = 0
    var currentProviderName: String = ""
    var categoryOnClick: (ProviderItem) -> Unit = {}
    private lateinit var binding: FragmentBottomSheetCaregoryBinding
    private val viewModel: ProviderViewModel by viewModels()
    private val providerAdapter: ProviderAdapter by lazy {
        ProviderAdapter().apply {
            currentSelectedPos = currentProvider
            onClick = { data ->
                if (data.providerId != currentProvider) {
                    categoryOnClick.invoke(data)
                    dismiss()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomSheetCaregoryBinding.inflate(inflater, container, false)
        initData()
        initAction()
        initObservers()
        return binding.root
    }

    private fun initAction() {
        binding.apply {
            rvCategory.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = providerAdapter
                setHasFixedSize(true)
            }
        }
    }

    private fun initData() {
    }

    private fun initObservers() {
        viewModel.store.apply {
            observe(
                owner = this@ProviderBottomSheet,
                selector = { state -> state.listProvider },
                observer = {
                    if (it.isNotEmpty()) {
                        providerAdapter.submitList(it.mapIndexed { _, providerItem ->
                            if (providerItem.providerId == currentProvider || (currentProvider == 0) && currentProviderName == providerItem.name) {
                                providerItem.apply {
                                    isSelected = true
                                }
                            } else {
                                providerItem.apply {
                                    isSelected = false
                                }
                            }
                        })
                    }
                })
        }
    }

    companion object {
        fun newInstance() = ProviderBottomSheet()
    }
}
