package com.example.assetmanagementapp.ui.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.databinding.FragmentNotificationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotificationFragment : BaseFragment() {

    private lateinit var binding:FragmentNotificationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(layoutInflater)
        initData()
        initView()
        return binding.root
    }

    private fun initView() {
        binding.apply {
            toolbarId.apply {
                root.setBackgroundColor(resources.getColor(R.color.white, null))
                tvCenter.text = getString(R.string.notification)
                backButton.visibility = View.GONE
            }
        }
    }

    private fun initData() {

    }

}
