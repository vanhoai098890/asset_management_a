package com.example.assetmanagementapp.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_common.extensions.onLoadMoreListener
import com.example.app_common.ui.snackbar.CustomSnackBar
import com.example.app_common.utils.ScreenUtils
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.data.remote.api.model.favourite.DeviceItem
import com.example.assetmanagementapp.databinding.FavouriteFragmentBinding
import com.example.assetmanagementapp.ui.detaildevice.DetailDeviceFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavouriteFragment : BaseFragment() {

    private val viewModel: FavouriteViewModel by viewModels()
    private lateinit var binding: FavouriteFragmentBinding
    private val roomAdapter: DeviceFavAdapter by lazy {
        DeviceFavAdapter().apply {
            onClick = { deviceItem ->
                showDialog(deviceItem)
            }
            onSaveItem = { deviceItem ->
                viewModel.saveDevices(deviceId = deviceItem.id, isSave = !deviceItem.isFavourite)
            }
        }
    }

    private val customSnackBar: CustomSnackBar by lazy {
        CustomSnackBar.make(
            parent = binding.layoutParent,
            message = ""
        ).apply {
            setContentMargin(
                activityContext = context,
                left = null,
                right = null,
                bottom = ScreenUtils.toPx(context, 16f)
            )
        }
    }

    private fun showDialog(deviceItem: DeviceItem) {
        addNoNavigationFragment(DetailDeviceFragment.newInstance(deviceId = deviceItem.id).apply {
            onBackPress = { deviceItem, isFavourite ->
                viewModel.dispatchFragmentResultFavourite(deviceItem, isFavourite)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FavouriteFragmentBinding.inflate(inflater, container, false)
        initViews()
        initEvents()
        return binding.root
    }

    private fun initViews() {
        binding.apply {
            rvFavouriteRoom.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
                adapter = roomAdapter
            }
            toolbarId.apply {
                tvCenter.text = context?.getString(R.string.v1_favourite_devices)
                backButton.visibility = View.INVISIBLE
                root.setBackgroundColor(resources.getColor(R.color.white, null))
            }
        }
    }

    private var loadMoreListener: RecyclerView.OnScrollListener? = null

    private fun initEvents() {
        viewModel.store.apply {
            observe(
                owner = this@FavouriteFragment,
                selector = { currentState -> currentState.stateListFavouriteRoom },
                observer = {
                    roomAdapter.submitList(it.toMutableList())
                    if (it.size == 10 && loadMoreListener == null) {
                        loadMoreListener = binding.rvFavouriteRoom.onLoadMoreListener {
                            viewModel.onLoadMore()
                        }
                    }
                    if (it.size < 10 && loadMoreListener != null) {
                        binding.rvFavouriteRoom.removeOnScrollListener(loadMoreListener!!)
                    }
                }
            )
            observe(
                owner = this@FavouriteFragment,
                selector = { currentState -> currentState.stateVisibleNotFoundItem },
                observer = {
                    if (it) {
                        binding.layoutNoResult.root.visibility = View.VISIBLE
                    } else {
                        binding.layoutNoResult.root.visibility = View.GONE
                    }
                }
            )
            observe(
                owner = this@FavouriteFragment,
                selector = { currentState -> currentState.stateIsShowSnackBar },
                observer = {
                    it?.apply { showCustomSnackBar(it) }
                }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavouriteDevices()
    }

    override fun onPause() {
        super.onPause()
        customSnackBar.dismiss()
    }

    private fun showCustomSnackBar(isFavourite: Boolean) {
        val message = getString(
            if (isFavourite) R.string.my_list_favorite_toast else R.string.my_list_un_favorite_toast
        )

        customSnackBar.setText(message)
        if (customSnackBar.isShown) {
            customSnackBar.refreshCounting()
        } else {
            customSnackBar.show()
        }
        viewModel.dispatchResetSnackBar()
    }
}
