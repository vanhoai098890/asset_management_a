package com.example.assetmanagementapp.common

import android.content.Intent
import com.example.app_common.base.BaseControlActivity
import com.example.assetmanagementapp.MainActivity
import com.example.assetmanagementapp.ui.splash.SplashActivity

abstract class BaseActivity : BaseControlActivity() {
    internal fun recreateMainScreen() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

    internal fun recreateLoginScreen() {
        startActivity(Intent(this, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }
}
