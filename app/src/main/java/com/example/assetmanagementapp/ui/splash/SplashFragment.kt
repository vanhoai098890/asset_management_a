package com.example.assetmanagementapp.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.app_common.constant.AppConstant
import com.example.app_common.extensions.replaceFragment
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.common.BaseActivity
import com.example.assetmanagementapp.common.BaseFragment
import com.example.assetmanagementapp.ui.sign_in.SignInFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment() {
    private val viewModel: SplashViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setLevel(AppConstant.LEVEL_CONTAINER)
        view.postDelayed(
            {
                if (viewModel.isRememberLogin) {
                    recreateMainScreen()
                    return@postDelayed
                }
                (requireActivity() as? BaseActivity)?.replaceFragment(
                    SignInFragment().apply {
                        setLevel(AppConstant.LEVEL_TAB)
                    },
                    isAddBackStack = false
                )
            },
            1000L
        )
        super.onViewCreated(view, savedInstanceState)
    }
}