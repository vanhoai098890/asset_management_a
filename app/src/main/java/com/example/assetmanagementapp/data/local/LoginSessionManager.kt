package com.example.assetmanagementapp.data.local

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.app_common.constant.AppConstant
import com.example.app_common.utils.LogUtils
import com.example.assetmanagementapp.data.remote.api.model.customer.CustomerProperty
import com.example.assetmanagementapp.data.remote.api.model.customer.UserInfoResponse
import com.example.assetmanagementapp.data.remote.api.model.refreshtoken.RefreshTokenData
import com.example.assetmanagementapp.data.remote.api.model.signin.response.LoginAuthenticator
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginSessionManager @Inject constructor(private val sharedPreferences: SharedPreferences) {
    companion object {
        private const val KEY_TOKEN_ACCESS = "key_token_access"
        private const val KEY_REMEMBER_CHECKED = "key_remember_checked"
        private const val KEY_USERNAME_LOGGED_IN = "key_username_logged_in"
        private const val KEY_PASSWORD_LOGGED_IN = "key_password_logged_in"
        private const val CUSTOMER_INFO = "CUSTOMER_INFO"
    }

    internal fun saveUserInfo(username: String, password: String) =
        sharedPreferences.edit().apply {
            putString(KEY_USERNAME_LOGGED_IN, username)
            putString(KEY_PASSWORD_LOGGED_IN, password)
            putBoolean(KEY_REMEMBER_CHECKED, true)
        }.commit()

    internal fun isRememberLogin(): Boolean =
        sharedPreferences.getBoolean(KEY_REMEMBER_CHECKED, false)

    internal fun clearUserInfo() =
        sharedPreferences.edit().apply {
            putString(KEY_USERNAME_LOGGED_IN, AppConstant.EMPTY)
            putString(KEY_PASSWORD_LOGGED_IN, AppConstant.EMPTY)
            putBoolean(KEY_REMEMBER_CHECKED, false)
        }.commit()

    internal fun getUsername(): String =
        sharedPreferences.getString(KEY_USERNAME_LOGGED_IN, AppConstant.EMPTY) ?: AppConstant.EMPTY

    internal fun getPassword(): String =
        sharedPreferences.getString(KEY_PASSWORD_LOGGED_IN, AppConstant.EMPTY) ?: AppConstant.EMPTY

    private fun getLoginAuthenticator(): LoginAuthenticator? {
        return try {
            val userInfoJson = sharedPreferences.getString(KEY_TOKEN_ACCESS, null)
            Gson().fromJson(userInfoJson, LoginAuthenticator::class.java)
        } catch (e: JsonParseException) {
            LogUtils.e(e.stackTraceToString())
            null
        } catch (e: JsonSyntaxException) {
            LogUtils.e(e.stackTraceToString())
            null
        }
    }

    internal fun updateNewToken(newToken: RefreshTokenData?) {
        newToken?.let {
            val oldToken = getLoginAuthenticator()
            oldToken?.jwtToken = newToken.accessToken
            saveLoginToken(oldToken)
        }
    }

    internal fun saveLoginToken(loginAuthenticator: LoginAuthenticator?) {
        loginAuthenticator?.let {
            val loginAuthenticatorJson = Gson().toJson(it)
            sharedPreferences.apply {
                edit(commit = true) {
                    putString(KEY_TOKEN_ACCESS, loginAuthenticatorJson).apply()
                }
            }
        }
    }

    internal fun getTokenAuthorizationRequest(): String? {
        return getLoginAuthenticator()?.jwtToken
    }

    internal fun clearLoginToken() {
        sharedPreferences.edit(commit = true) {
            putString(KEY_TOKEN_ACCESS, null)
            putString(CUSTOMER_INFO, null)
        }
    }

    internal fun getRefreshToken(): String? = getLoginAuthenticator()?.refreshToken

    internal fun saveCustomer(userInfoResponse: UserInfoResponse?): Unit {
        userInfoResponse?.let {
            val customer = Gson().toJson(it)
            sharedPreferences.apply {
                edit(commit = true) {
                    putString(CUSTOMER_INFO, customer).apply()
                }
            }
        }
    }

    internal fun getCustomerLocal(): UserInfoResponse? {
        sharedPreferences.getString(CUSTOMER_INFO, null)?.apply {
            return Gson().fromJson(this, UserInfoResponse::class.java)
        }
        return null
    }

    internal fun isLoggedIn(): Boolean = getTokenAuthorizationRequest()?.isNotBlank() ?: false
}
