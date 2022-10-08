package com.example.assetmanagementapp.data.repositories

import com.example.assetmanagementapp.data.local.LoginSessionManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonalRepository @Inject constructor(
    private val loginSessionManager: LoginSessionManager
) {
    internal fun logoutAction() {
        loginSessionManager.clearLoginToken()
    }
}
