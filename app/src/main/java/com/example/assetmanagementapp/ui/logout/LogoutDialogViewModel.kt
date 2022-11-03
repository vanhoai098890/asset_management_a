package com.example.assetmanagementapp.ui.logout

import com.example.app_common.base.viewmodel.BaseViewModel
import com.example.assetmanagementapp.data.repositories.PersonalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LogoutDialogViewModel @Inject constructor(private val personalRepository: PersonalRepository) :
    BaseViewModel() {

    fun logoutAction() = personalRepository.logoutAction()
}
