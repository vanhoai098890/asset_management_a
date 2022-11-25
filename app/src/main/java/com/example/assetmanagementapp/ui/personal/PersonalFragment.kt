package com.example.assetmanagementapp.ui.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.databinding.FragmentPersonalBinding
import com.example.assetmanagementapp.ui.editprofile.EditProfileBottomSheet
import com.example.assetmanagementapp.ui.logout.LogoutDialog
import com.example.assetmanagementapp.ui.userinfo.UserInfoFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PersonalFragment : BaseFragment() {

    private lateinit var binding: FragmentPersonalBinding
    private val viewModel: PersonalViewModel by viewModels()
    private val adapter by lazy {
        PersonalAdapter().apply {
            onClickItem = {
                when (it) {
                    PersonalFunctionStatic.PERSONAL -> {
                        viewModel.statePersonalInfo.value?.let { userInfo ->
                            addNoNavigationFragment(UserInfoFragment.newInstance(userInfo))
                        }
                    }
                    PersonalFunctionStatic.LOGOUT -> {
                        logoutDialog.show(parentFragmentManager, null)
                    }
                    else -> {}
                }
            }
            onEditProfile = {
                showBottomSheet(EditProfileBottomSheet.newInstance().apply {
                    onBackPress = {
                        viewModel.getCustomerInfo()
                    }
                })
            }
        }
    }
    private val logoutDialog: LogoutDialog by lazy {
        LogoutDialog()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCustomerInfo()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPersonalBinding.inflate(layoutInflater)
        initData()
        initEvents()
        return binding.root
    }

    private fun initData() {
        binding.apply {
            rvPersonal.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter = this@PersonalFragment.adapter
            }
        }
    }

    private fun initEvents() {
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.listPersonal.collect {
                    adapter.submitList(it.toList())
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PersonalFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}