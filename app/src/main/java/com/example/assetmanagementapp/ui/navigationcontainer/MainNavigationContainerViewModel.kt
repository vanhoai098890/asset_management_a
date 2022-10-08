package com.example.assetmanagementapp.ui.navigationcontainer

import com.example.app_common.base.viewmodel.BaseViewModel
import com.example.assetmanagementapp.data.repositories.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainNavigationContainerViewModel @Inject constructor(
    private val loginRepository: LoginRepository
) : BaseViewModel() {
}
