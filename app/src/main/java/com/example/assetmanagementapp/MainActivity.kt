package com.example.assetmanagementapp

import android.os.Bundle
import com.example.app_common.R
import com.example.app_common.base.BaseControlFragment
import com.example.assetmanagementapp.common.BaseActivity
import com.example.assetmanagementapp.ui.navigationcontainer.MainNavigationContainerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    override fun navigationContainer(): BaseControlFragment = MainNavigationContainerFragment()

    override fun noNavigationContainer(): BaseControlFragment = MainNoNavigationContainerFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
