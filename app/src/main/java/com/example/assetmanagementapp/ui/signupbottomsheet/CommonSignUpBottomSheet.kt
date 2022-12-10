package com.example.assetmanagementapp.ui.signupbottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_common.base.BaseBottomSheetDialogFragment
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAsset
import com.example.assetmanagementapp.databinding.FragmentBottomSheetRoomBinding
import com.example.assetmanagementapp.ui.categorysheet.CategoryAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommonSignUpBottomSheet :
    BaseBottomSheetDialogFragment() {
    var currentStatusName: String = ""
    var statusOnClick: (TypeAsset) -> Unit = {}
    var listTypeAsset: List<TypeAsset> = listOf()
    private lateinit var binding: FragmentBottomSheetRoomBinding
    private val statusAdapter: CategoryAdapter by lazy {
        CategoryAdapter().apply {
            isShowNumberAsset = false
            onClick = { data ->
                if (data.typeName != currentStatusName) {
                    statusOnClick.invoke(data)
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
        binding = FragmentBottomSheetRoomBinding.inflate(inflater, container, false)
        initAction()
        initObservers()
        return binding.root
    }

    private fun initAction() {
        binding.apply {
            rvRoom.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = statusAdapter
                setHasFixedSize(true)
            }
        }
    }

    private fun initObservers() {
        statusAdapter.submitList(listTypeAsset.map { item ->
            if (currentStatusName == item.typeName) {
                item.apply {
                    isSelected = true
                }
            } else {
                item.apply {
                    isSelected = false
                }
            }
        })
    }

    companion object {
        fun newInstance() = CommonSignUpBottomSheet()
    }
}
