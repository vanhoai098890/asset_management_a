package com.example.assetmanagementapp.ui.navigationcontainer

import com.example.app_common.base.viewmodel.BaseViewModel
import com.example.assetmanagementapp.data.local.LoginSessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PagerContainerViewModel @Inject constructor(
    private val loginSessionManager: LoginSessionManager
) : BaseViewModel() {

    internal fun getToken() = loginSessionManager.getTokenAuthorizationRequest() ?: ""
}
