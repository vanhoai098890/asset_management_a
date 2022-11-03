package com.example.assetmanagementapp.common

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.app_common.base.BaseControlActivity
import com.example.app_common.utils.eventbus.EventBus
import com.example.app_common.utils.eventbus.event_model.EventRequestLogin
import com.example.app_common.utils.eventbus.event_model.EventTimeOutApi
import com.example.assetmanagementapp.MainActivity
import com.example.assetmanagementapp.ui.splash.SplashActivity
import com.example.assetmanagementapp.ui.timeoutsession.TimeoutSessionDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseActivity : BaseControlActivity() {

    private val timeOutSessionDialog: TimeoutSessionDialog by lazy {
        TimeoutSessionDialog()
    }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launchWhenStarted {
            launch {
                EventBus.subject.collectLatest {
                    if (it is EventRequestLogin) {
                        timeOutSessionDialog.show(supportFragmentManager, null)
                    }
                }
            }
        }
    }
}
