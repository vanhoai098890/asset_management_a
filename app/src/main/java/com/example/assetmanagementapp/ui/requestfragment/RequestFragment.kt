package com.example.assetmanagementapp.ui.requestfragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.databinding.FragmentRequestBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RequestFragment : BaseFragment() {
    private lateinit var binding: FragmentRequestBinding
    private val viewModel: RequestViewModel by viewModels()
    private val adapter: RequestAdapter by lazy {
        RequestAdapter().apply {
            onClickItem = {}
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRequestBinding.inflate(layoutInflater)
        return binding.root
    }
}
