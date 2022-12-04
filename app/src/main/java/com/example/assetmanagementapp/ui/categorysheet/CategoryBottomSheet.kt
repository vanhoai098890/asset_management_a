package com.example.assetmanagementapp.ui.categorysheet

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
class CategoryBottomSheet :
    BaseBottomSheetDialogFragment() {
    var departmentId: Int = 0
    var roomId: Int = 0
    var currentCategory: Int = 0
    var categoryOnClick: (TypeAsset) -> Unit = {}
    private lateinit var binding: FragmentBottomSheetCaregoryBinding
    private val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter().apply {
            currentSelectedPos = currentCategory
            onClick = { data ->
                if (data.id != currentCategory) {
                    categoryOnClick.invoke(data)
                    dismiss()
                }
            }
        }
    }
    private val viewModel: CategoryViewModel by viewModels()

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
        viewModel.currentState.departmentId = departmentId
        viewModel.currentState.roomId = roomId
        viewModel.getCategories()
    }

    private fun initObservers() {
        viewModel.store.apply {
            observe(
                owner = this@CategoryBottomSheet,
                selector = { state -> state.listCategory },
                observer = {
                    if (it.size > 0) {
                        categoryAdapter.submitList(it.mapIndexed { _, typeAsset ->
                            if (typeAsset.id == currentCategory) {
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
        fun newInstance() = CategoryBottomSheet()
    }
}
