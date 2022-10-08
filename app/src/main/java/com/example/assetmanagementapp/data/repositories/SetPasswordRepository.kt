package com.example.assetmanagementapp.data.repositories

import com.example.app_common.extensions.onSuccess
import com.example.app_common.extensions.safeFlow
import com.example.assetmanagementapp.data.local.LoginSessionManager
import com.example.assetmanagementapp.data.remote.api.datasource.setpassword.SetPasswordRemoteDataSourceImpl
import javax.inject.Inject

class SetPasswordRepository @Inject constructor(
    private val dataSource: SetPasswordRemoteDataSourceImpl,
    private val sessionManager: LoginSessionManager
) {
    internal fun getSetPasswordInfo(username: String, password: String) = safeFlow {
        dataSource.getSetPasswordInfo(username, password)
    }.onSuccess {
        sessionManager.run {
            saveLoginToken(it.loginAuthenticator)
        }
    }

    internal fun clearTokenSignUp() {
        sessionManager.clearLoginToken()
    }
}
