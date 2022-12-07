package com.example.assetmanagementapp.ui.typeassetsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_common.base.BaseBottomSheetDialogFragment
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAsset
import com.example.assetmanagementapp.databinding.FragmentBottomSheetCaregoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AssetTypeBottomSheet :
    BaseBottomSheetDialogFragment() {
    var currentCategory: Int = 0
    var currentNameCategory: String = ""
    var categoryOnClick: (TypeAsset) -> Unit = {}
    private lateinit var binding: FragmentBottomSheetCaregoryBinding
    private val viewModel: TypeAssetViewModel by viewModels()
    private val categoryAdapter: TypeAssetAdapter by lazy {
        TypeAssetAdapter().apply {
            currentSelectedPos = currentCategory
            onClick = { data ->
                if (data.id != currentCategory) {
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
                adapter = categoryAdapter
                setHasFixedSize(true)
            }
        }
    }

    private fun initData() {
    }

    private fun initObservers() {
        viewModel.store.apply {
            observe(
                owner = this@AssetTypeBottomSheet,
                selector = { state -> state.listCategory },
                observer = {
                    if (it.isNotEmpty()) {
                        categoryAdapter.submitList(it.mapIndexed { _, typeAsset ->
                            if (typeAsset.id == currentCategory || (currentCategory == 0 && typeAsset.typeName == currentNameCategory)) {
                                typeAsset.apply {
                                    isSelected = true
                                }
                            } else {
                                typeAsset.apply {
                                    isSelected = false
                                }
                            }

                        })
                    }
                })
        }
    }

    companion object {
        fun newInstance() = AssetTypeBottomSheet()
    }
}
