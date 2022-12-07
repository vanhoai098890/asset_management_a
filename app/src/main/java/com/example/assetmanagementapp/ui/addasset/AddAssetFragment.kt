package com.example.assetmanagementapp.ui.addasset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.app_common.extensions.setSafeOnClickListener
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.databinding.FragmentAddAssetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAssetFragment : BaseFragment() {

    private val viewModel: AddAssetViewModel by viewModels()
    private lateinit var binding: FragmentAddAssetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddAssetBinding.inflate(layoutInflater)
        initData()
        initView()
        initObserver()
        return binding.root
    }

    private fun initObserver() {

    }

    private fun initView() {
        binding.apply {
            layoutToolbar.apply {
                tvCenter.text = getString(R.string.add_asset)
                backButton.setSafeOnClickListener {
                    handleBackPressed()
                }
            }

        }
    }

    private fun initData() {

    }

}
