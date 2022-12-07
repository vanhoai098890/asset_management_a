package com.example.assetmanagementapp.ui.consignmentmain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.app_common.extensions.onLoadMoreListener
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.databinding.FragmentConsignmentBinding
import com.example.assetmanagementapp.ui.addconsignment.AddConsignmentFragment
import com.example.assetmanagementapp.ui.consignment.ConsignmentDetailFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConsignmentFragment : BaseFragment() {

    private lateinit var binding: FragmentConsignmentBinding
    private val viewModel: ConsignmentViewModel by viewModels()
    private val consignmentAdapter: ConsignmentAdapter by lazy {
        ConsignmentAdapter().apply {
            onClick = {
                addNoNavigationFragment(ConsignmentDetailFragment.newInstance(it))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConsignmentBinding.inflate(layoutInflater)
        initData()
        initView()
        initObserver()
        return binding.root
    }

    private var loadMoreListener: RecyclerView.OnScrollListener? = null

    private fun initObserver() {
        viewModel.store.apply {
            observe(owner = this@ConsignmentFragment,
                selector = { state -> state.stateListConsignment },
                observer = {
                    if (it.size % 10 == 0 && loadMoreListener == null) {
                        loadMoreListener = binding.rvConsignment.onLoadMoreListener {
                            viewModel.onLoadMore()
                        }
                    }
                    if (it.size % 10 != 0 && loadMoreListener != null) {
                        binding.rvConsignment.removeOnScrollListener(loadMoreListener!!)
                    }
                    consignmentAdapter.submitList(it)
                    if (it.isEmpty()) {
                        binding.layoutNoResult.root.visibility = View.VISIBLE
                    } else {
                        binding.layoutNoResult.root.visibility = View.GONE
                    }
                })
        }
    }

    private fun initView() {
        binding.apply {
            layoutToolbar.apply {
                tvCenter.text = getString(R.string.consignment)
                backButton.setSafeOnClickListener {
                    handleBackPressed()
                }
            }
            rvConsignment.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = consignmentAdapter
            }
            btnAddConsignment.setSafeOnClickListener {
                addNoNavigationFragment(AddConsignmentFragment())
            }
            ivFilter.setSafeOnClickListener {
                viewModel.searchConsignment(edtSearch.text.toString())
            }
        }

    }

    private fun initData() {

    }
}
