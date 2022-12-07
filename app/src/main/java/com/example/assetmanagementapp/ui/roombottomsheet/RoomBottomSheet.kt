package com.example.assetmanagementapp.ui.roombottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_common.base.BaseBottomSheetDialogFragment
import com.example.assetmanagementapp.data.remote.api.model.room.RoomItem
import com.example.assetmanagementapp.databinding.FragmentBottomSheetRoomBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RoomBottomSheet :
    BaseBottomSheetDialogFragment() {
    var currentRoomName: String = ""
    var roomOnClick: (RoomItem) -> Unit = {}
    private lateinit var binding: FragmentBottomSheetRoomBinding
    private val viewModel: RoomViewModel by viewModels()
    private val roomAdapter: RoomBottomSheetAdapter by lazy {
        RoomBottomSheetAdapter().apply {
            onClick = { data ->
                if (data.roomName != currentRoomName) {
                    roomOnClick.invoke(data)
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
                adapter = roomAdapter
                setHasFixedSize(true)
            }
        }
    }

    private fun initData() {
    }

    private fun initObservers() {
        viewModel.store.apply {
            observe(
                owner = this@RoomBottomSheet,
                selector = { state -> state.listRoom },
                observer = {
                    if (it.isNotEmpty()) {
                        roomAdapter.submitList(it.mapIndexed { _, providerItem ->
                            if (currentRoomName == providerItem.roomName) {
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
        fun newInstance() = RoomBottomSheet()
    }
}
