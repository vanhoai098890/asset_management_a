package com.example.assetmanagementapp

import com.example.app_common.base.BaseControlFragment
import com.example.assetmanagementapp.common.BaseActivity
import com.example.assetmanagementapp.ui.navigationcontainer.MainNavigationContainerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override fun navigationContainer(): BaseControlFragment = MainNavigationContainerFragment()

    override fun noNavigationContainer(): BaseControlFragment = MainNoNavigationContainerFragment()
}
