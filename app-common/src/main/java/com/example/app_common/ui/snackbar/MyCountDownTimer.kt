package com.example.app_common.ui.snackbar

import android.os.CountDownTimer

open class MyCountDownTimer(
    millisInFuture: Long,
    countDownInterval: Long
) : CountDownTimer(millisInFuture, countDownInterval) {
    var isCounting = false

    override fun onTick(millisUntilFinished: Long) {
        isCounting = true
    }

    override fun onFinish() {
        isCounting = false
    }

    /**
     * Use this instead of start()
     */
    fun startOrRestart() {
        if(isCounting) {
            cancel()
        }
        start()
    }

    /**
     * Use this instead of cancel()
     */
    fun cancelCountdown() {
        isCounting = false
        cancel()
    }
}
