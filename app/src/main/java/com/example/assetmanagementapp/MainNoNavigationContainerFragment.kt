package com.example.assetmanagementapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.assetmanagementapp.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainNoNavigationContainerFragment : BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_no_navigation_container, container, false)
    }

    override fun handleBackPressed(tagNameBackStack: String?) {
        super.handleBackPressed(tagNameBackStack)
    }
}
