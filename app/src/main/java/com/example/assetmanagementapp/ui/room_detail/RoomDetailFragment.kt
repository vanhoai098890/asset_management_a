package com.example.assetmanagementapp.ui.room_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.data.remote.api.model.favourite.DeviceItem
import com.example.assetmanagementapp.databinding.FragmentRoomDetailBinding
import com.example.assetmanagementapp.ui.detaildevice.DetailDeviceFragment
import com.example.assetmanagementapp.ui.favourite.DeviceFavAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RoomDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentRoomDetailBinding
    private val viewModel: RoomDetailViewModel by viewModels()
    private val deviceAdapter: DeviceFavAdapter by lazy {
        DeviceFavAdapter().apply {
            isHideFavouriteButton = true
            onClick = { deviceItem ->
                showDialog(deviceItem)
            }
        }
    }

    private fun showDialog(deviceItem: DeviceItem) {
        addNoNavigationFragment(DetailDeviceFragment.newInstance(deviceId = deviceItem.id))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRoomDetailBinding.inflate(layoutInflater)
        initData()
        initView()
        initEvent()
        return binding.root
    }

    private fun initEvent() {
        viewModel.store.apply {
            observe(
                owner = this@RoomDetailFragment,
                selector = { state -> state.listSearchDevice },
                observer = {
                    if (it.isNotEmpty()) {
                        deviceAdapter.submitList(it)
                        binding.layoutNoResult.root.visibility = View.GONE
                        binding.rvAsset.visibility = View.VISIBLE
                    } else {
                        binding.layoutNoResult.root.visibility = View.VISIBLE
                        binding.rvAsset.visibility = View.GONE
                    }
                })
        }
        viewModel.loadingState().onEach {
            handleShowLoadingDialog(it)
        }.launchIn(lifecycleScope)
    }

    private fun initView() {
        binding.apply {
            toolbarId.apply {
                backButton.setSafeOnClickListener {
                    handleBackPressed()
                }
                tvCenter.text = getString(R.string.room_asset, arguments?.getString(ROOM_NAME))
            }
            rvAsset.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvAsset.adapter = deviceAdapter
        }
    }

    private fun initData() {
        arguments?.apply {
            viewModel.currentState.roomId = getInt(ROOM_ID)
            viewModel.getAssetByRoomId()
        }
    }

    companion object {
        private const val ROOM_NAME = "ROOM_NAME"
        private const val ROOM_ID = "ROOM_ID"
        fun newInstance(nameRoom: String, roomId: Int): RoomDetailFragment {
            val args = Bundle().apply {
                putString(ROOM_NAME, nameRoom)
                putInt(ROOM_ID, roomId)
            }
            val fragment = RoomDetailFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
