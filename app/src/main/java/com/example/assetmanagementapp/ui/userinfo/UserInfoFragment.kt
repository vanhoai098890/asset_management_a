package com.example.assetmanagementapp.ui.userinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.data.remote.api.model.customer.UserInfo
import com.example.assetmanagementapp.databinding.FragmentUserInfoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserInfoFragment : BaseFragment() {

    private lateinit var binding: FragmentUserInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserInfoBinding.inflate(layoutInflater)
        binding.apply {
            data = arguments?.getParcelable(USER_INFO)
            lifecycleOwner = viewLifecycleOwner
        }
        initView()
        return binding.root
    }

    private fun initView() {
        binding.apply {
            layoutToolbar.apply {
                root.setBackgroundColor(resources.getColor(R.color.white, null))
                tvCenter.text = getString(R.string.user_information)
                backButton.setSafeOnClickListener {
                    handleBackPressed()
                }
            }
        }
    }

    companion object {

        private const val USER_INFO = "USER_INFO"

        fun newInstance(userInfo: UserInfo): UserInfoFragment {
            val args = Bundle().apply {
                putParcelable(USER_INFO, userInfo)
            }
            val fragment = UserInfoFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
