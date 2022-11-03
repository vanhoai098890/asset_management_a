package com.example.assetmanagementapp.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.example.assetmanagementapp.MainNoNavigationContainerFragment
import com.example.assetmanagementapp.common.BaseActivity
import com.example.assetmanagementapp.common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    override fun navigationContainer(): BaseFragment =
        SplashFragment().apply {
            arguments = intent.extras
        }

    override fun noNavigationContainer(): BaseFragment = MainNoNavigationContainerFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
    }
}
