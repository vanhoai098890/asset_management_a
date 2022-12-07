package com.example.assetmanagementapp.ui.statusbottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_common.base.BaseBottomSheetDialogFragment
import com.example.assetmanagementapp.data.remote.api.model.typeasset.TypeAsset
import com.example.assetmanagementapp.databinding.FragmentBottomSheetRoomBinding
import com.example.assetmanagementapp.ui.categorysheet.CategoryAdapter
import com.example.assetmanagementapp.ui.roombottomsheet.RoomViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatusBottomSheet :
    BaseBottomSheetDialogFragment() {
    var currentStatusName: String = ""
    var statusOnClick: (TypeAsset) -> Unit = {}
    private lateinit var binding: FragmentBottomSheetRoomBinding
    private val viewModel: RoomViewModel by viewModels()
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
        initData()
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

    private fun initData() {
    }

    private fun initObservers() {
        viewModel.store.apply {
            observe(
                owner = this@StatusBottomSheet,
                selector = { state -> state.listStatus },
                observer = {
                    if (it.isNotEmpty()) {
                        statusAdapter.submitList(it.mapIndexed { _, statusItem ->
                            if (currentStatusName == statusItem.typeName) {
                                statusItem.apply {
                                    isSelected = true
                                }
                            } else {
                                statusItem.apply {
                                    isSelected = false
                                }
                            }
                        })
                    }
                })
        }
    }

    companion object {
        fun newInstance() = StatusBottomSheet()
    }
}
