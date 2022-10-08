package com.example.assetmanagementapp.ui.forgotpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.databinding.FragmentChangePasswordSuccessBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordSuccessFragment : BaseFragment() {
    private var binding: FragmentChangePasswordSuccessBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangePasswordSuccessBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initEvents()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun initEvents() {
        binding?.btnChangePasswordSuccessOk?.setSafeOnClickListener {
            recreateLoginScreen()
        }
    }
}
