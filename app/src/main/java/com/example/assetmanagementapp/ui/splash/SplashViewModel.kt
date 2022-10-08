package com.example.assetmanagementapp.ui.splash

import com.example.app_common.base.viewmodel.BaseViewModel
import com.example.assetmanagementapp.data.local.LoginSessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    loginSessionManager: LoginSessionManager
) : BaseViewModel() {
    val isRememberLogin = loginSessionManager.isRememberLogin()
}
