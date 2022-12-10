package com.example.assetmanagementapp.ui.usermanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.databinding.FragmentUserManagementBinding
import com.example.assetmanagementapp.ui.sign_up.SignUpFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserManagementFragment : BaseFragment() {

    private lateinit var binding: FragmentUserManagementBinding
    private val viewModel: UserManagementViewModel by viewModels()
    private val userAdapter: UserManagementAdapter by lazy {
        UserManagementAdapter().apply {
            onItemClick = {
                addNoNavigationFragment(SignUpFragment.newInstance(it).apply {
                    handleOnBackPress = {
                        initAgainData()
                    }
                })
            }
        }
    }

    private fun initAgainData() {
        viewModel.searchUser(binding.edtSearch.text.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserManagementBinding.inflate(layoutInflater)
        initData()
        initView()
        initObserver()
        return binding.root
    }

    private fun initData() {

    }

    private fun initView() {
        binding.apply {
            layoutToolbar.apply {
                tvCenter.text = getString(R.string.user_management)
                backButton.setSafeOnClickListener {
                    handleBackPressed()
                }
            }
            rvUser.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = userAdapter
            }
            btnAddUser.setSafeOnClickListener {
                addNoNavigationFragment(SignUpFragment().apply {
                    handleOnBackPress = {
                        initAgainData()
                    }
                })
            }
            ivFilter.setSafeOnClickListener {
                viewModel.searchUser(edtSearch.text.toString())
            }
        }
    }

    private fun initObserver() {
        viewModel.store.apply {
            observe(
                owner = this@UserManagementFragment,
                selector = { state -> state.listUserInfo },
                observer = { listUser ->
                    if (listUser.isNotEmpty()) {
                        userAdapter.submitList(listUser)
                        binding.layoutNoResult.root.visibility = View.GONE
                    } else {
                        binding.layoutNoResult.root.visibility = View.VISIBLE
                    }
                })
        }
    }
}
