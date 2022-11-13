package com.example.assetmanagementapp.ui.searchresult

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_common.extensions.onLoadMoreListener
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.data.remote.api.model.favourite.DeviceItem
import com.example.assetmanagementapp.databinding.FragmentSearchResultBinding
import com.example.assetmanagementapp.ui.categorysheet.CategoryBottomSheet
import com.example.assetmanagementapp.ui.detaildevice.DetailDeviceFragment
import com.example.assetmanagementapp.ui.favourite.DeviceFavAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchResultFragment : BaseFragment() {

    private lateinit var binding: FragmentSearchResultBinding
    private val viewModel: SearchResultViewModel by viewModels()
    private val categoryBottomSheet: CategoryBottomSheet by lazy {
        CategoryBottomSheet().apply {
            categoryOnClick = {
                viewModel.dispatchCategorySelected(it)
            }
        }
    }
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
        binding = FragmentSearchResultBinding.inflate(layoutInflater)
        initData()
        initView()
        initObserver()
        return binding.root
    }

    private fun initView() {
        binding.apply {
            layoutToolbar.apply {
                backButton.setSafeOnClickListener {
                    handleBackPressed()
                }
                tvCenter.text = getString(R.string.v1_search)
            }
            layoutCategory.setSafeOnClickListener {
                categoryBottomSheet.apply {
                    currentCategory = viewModel.currentState.currentSelectedPos.id
                }.show(parentFragmentManager, null)
            }
            ivFilter.setSafeOnClickListener {
                viewModel.searchDevices(edtSearch.text.toString())
            }
        }
    }

    private fun initData() {
        binding.apply {
            arguments?.getString(SEARCH_STRING)?.apply {
                edtSearch.setText(this)
                viewModel.searchDevices(this)
            }
            rvDevice.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvDevice.adapter = deviceAdapter
        }
    }

    private var loadMoreListener: RecyclerView.OnScrollListener? = null

    private fun initObserver() {
        viewModel.store.apply {
            observe(
                owner = this@SearchResultFragment,
                selector = { state -> state.listSearchDevice },
                observer = { handleLoadDevice(it) })
            observe(
                owner = this@SearchResultFragment,
                selector = { state -> state.currentSelectedPos },
                observer = { binding.layoutCategory.text = it.typeName })
        }
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.loadingState().collect {
                    handleShowLoadingDialog(it)
                }
            }
        }
    }

    private fun handleLoadDevice(listDevice: MutableList<DeviceItem>) {
        deviceAdapter.submitList(listDevice)
        if (listDevice.size == 10) {
            loadMoreListener = binding.rvDevice.onLoadMoreListener {
                viewModel.onLoadMore(binding.edtSearch.text.toString())
            }
            binding.rvDevice.scrollToPosition(0)
        }
        if (listDevice.size < 10 && loadMoreListener != null) {
            binding.rvDevice.removeOnScrollListener(loadMoreListener!!)
        }
        if (listDevice.size == 0) {
            binding.layoutNoResult.root.visibility = View.VISIBLE
        } else {
            binding.layoutNoResult.root.visibility = View.GONE
        }
    }


    companion object {
        @JvmStatic
        fun newInstance(searchString: String) =
            SearchResultFragment().apply {
                arguments = Bundle().apply {
                    putString(SEARCH_STRING, searchString)
                }
            }

        const val SEARCH_STRING = "SEARCH_STRING"
    }
}
