package com.example.assetmanagementapp.ui.searchmain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_common.extensions.onLoadMoreListener
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.data.remote.api.model.customer.UserInfo
import com.example.assetmanagementapp.databinding.FragmentSearchMainBinding
import com.example.assetmanagementapp.ui.detaildevice.DetailDeviceFragment
import com.example.assetmanagementapp.ui.searchmain.AssetTypeAdapter.Companion.LOADING_TYPE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchMainFragment : BaseFragment() {
    private val viewModel: SearchMainViewModel by viewModels()
    private var binding: FragmentSearchMainBinding? = null
    private val adapter: AssetTypeAdapter by lazy {
        AssetTypeAdapter().apply {
            onItemClick = { data ->
                addNoNavigationFragment(DetailDeviceFragment.newInstance(data.id))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchMainBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initEvents()
    }

    private fun initData() {
        binding?.apply {
            rvFirst.apply {
                layoutManager =
                    GridLayoutManager(
                        requireContext(),
                        2,
                        LinearLayoutManager.VERTICAL,
                        false
                    ).apply {
                        spanSizeLookup = object : SpanSizeLookup() {
                            override fun getSpanSize(position: Int): Int {
                                return if (this@SearchMainFragment.adapter.getItemViewType(position) == LOADING_TYPE) {
                                    2
                                } else {
                                    1
                                }
                            }
                        }
                    }
                adapter = this@SearchMainFragment.adapter
                setHasFixedSize(true)
            }
            parentLayout.onLoadMoreListener {
                viewModel.onLoadMore()
            }
            ivFilter.setSafeOnClickListener {

            }
        }
    }

    private fun initEvents() {
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.stateUserInfo.collect { handleShowUIWithUserInfo(it) }
            }
            launch {
                viewModel.listItemDeviceData.collect { adapter.submitList(it) }
            }
            launch {
                viewModel.stateShowSnackBar.collect { handleShowSnackBarError(it) }
            }
        }
    }

    private fun handleShowSnackBarError(isShow: Boolean) {

    }

    private fun handleShowUIWithUserInfo(userInfo: UserInfo?) {
        userInfo?.apply {
            binding?.apply {
                tvUserName.text =
                    resources.getString(R.string.tv_hi_account, userInfo.username)
            }
        } ?: kotlin.run {
            binding?.apply {
                tvUserName.text =
                    resources.getString(R.string.tv_hi_account, "Hoài Văn")
            }
        }
    }

    override fun onResume() {
        viewModel.getCustomerInfo()
        super.onResume()
    }
}
