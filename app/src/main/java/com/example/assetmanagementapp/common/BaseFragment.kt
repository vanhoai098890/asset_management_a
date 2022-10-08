package com.example.assetmanagementapp.common

import com.example.app_common.base.BaseControlFragment

abstract class BaseFragment : BaseControlFragment() {
    fun recreateMainScreen() {
        (activity as? BaseActivity)?.recreateMainScreen()
    }
    fun recreateLoginScreen() {
        (activity as? BaseActivity)?.recreateLoginScreen()
    }
}
