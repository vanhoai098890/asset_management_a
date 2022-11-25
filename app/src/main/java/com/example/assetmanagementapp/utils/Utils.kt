package com.example.assetmanagementapp.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.app_common.constant.AppConstant.EMAIL_INFO
import com.example.app_common.constant.AppConstant.MAIL_TO
import com.example.app_common.utils.LogUtils
import com.example.assetmanagementapp.R

object Utils {
    fun callHotLine(context: Context?, phone: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:${phone}")
        context?.startActivity(intent)
    }

    fun sendEmail(context: Context?, customerId: String) {
        try {
            context?.let {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse(MAIL_TO)
                intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(EMAIL_INFO))
                intent.putExtra(
                    Intent.EXTRA_SUBJECT,
                    context.getString(R.string.contact_support_mail_subject, customerId)
                )
                intent.putExtra(
                    Intent.EXTRA_TEXT, ""
                )
                context.startActivity(intent)
            }
        } catch (ex: ActivityNotFoundException) {
            LogUtils.e(ex.message ?: "")
        }
    }
}
