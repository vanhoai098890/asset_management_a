package com.example.assetmanagementapp.utils

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.content.res.Resources
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.example.app_common.constant.AppConstant
import com.example.app_common.extensions.dpToPixels
import com.example.app_common.utils.LogUtils
import com.example.assetmanagementapp.R
import com.example.assetmanagementapp.ui.sign_in.SignInViewModel
import com.example.assetmanagementapp.ui.sign_up.SignUpViewModel
import com.google.android.material.textfield.TextInputLayout


@BindingAdapter("android:text")
fun TextView.bindTextResource(stringResId: Int) {
    try {
        text = context.getString(stringResId)
    } catch (e: Resources.NotFoundException) {
        LogUtils.e(e.stackTraceToString())
    }
}

@BindingAdapter("displayErrorMessage")
fun TextView.displayErrorMessage(msg: Int) {
    visibility = when (msg) {
        SignInViewModel.REQUIRE_FIELD -> {
            text = context.getString(R.string.v1_require_field)
            View.VISIBLE
        }
        SignInViewModel.WRONG_FIELD -> {
            text = context.getString(R.string.v1_wrong_field)
            View.VISIBLE
        }
        SignInViewModel.ERROR_DATABASE -> {
            text = context.getString(R.string.v1_error_save_database)
            View.VISIBLE
        }
        else -> {
            text = AppConstant.EMPTY
            View.GONE
        }
    }
}

@BindingAdapter("displayErrorMessageSignUp")
fun TextView.displayErrorMessageSignUp(msg: Int) {
    visibility = when (msg) {
        SignUpViewModel.REQUIRE_FIELD -> {
            text = context.getString(R.string.v1_require_field_sign_up)
            View.VISIBLE
        }
        SignUpViewModel.WRONG_EMAIL -> {
            text = context.getString(R.string.v1_wrong_email)
            View.VISIBLE
        }
        SignUpViewModel.WRONG_CITIZEN -> {
            text = context.getString(R.string.v1_wrong_citizen_id)
            View.VISIBLE
        }
        SignUpViewModel.WRONG_PHONE -> {
            text = context.getString(R.string.v1_wrong_phone)
            View.VISIBLE
        }
        SignUpViewModel.USERNAME_EXISTED -> {
            text = context.getString(R.string.v1_username_is_existed)
            View.VISIBLE
        }
        SignUpViewModel.REQUIRE_FIELD_NOT_BLANK -> {
            text = context.getString(R.string.v1_require_field_not_blank)
            View.VISIBLE
        }
        else -> {
            text = AppConstant.EMPTY
            View.GONE
        }
    }
}

@BindingAdapter("bindTextUserName")
fun TextView.bindTextUserName(name: String?) {
    var flagUpper = true
    var tempText = ""
    name?.apply {
        forEachIndexed { _, name ->
            tempText = if (flagUpper) {
                "${tempText}${name.uppercaseChar()}"
            } else {
                "${tempText}${name}"
            }
            flagUpper = name == ' '
        }
    }
    text = tempText
}

@BindingAdapter("bindNation")
fun TextView.bindNation(name: String?) {
    var flagUpper = true
    var tempText = ""
    name?.apply {
        forEachIndexed { _, name ->
            tempText = if (flagUpper) {
                "${tempText}${name.uppercaseChar()}"
            } else {
                "${tempText}${name.lowercaseChar()}"
            }
            flagUpper = name == ' '
        }
    }
    text = tempText
}

@BindingAdapter("bindTextNationality")
fun TextView.bindTextNationality(nationality: String?) {
    text = if (nationality.isNullOrBlank()) {
        setTextColor(resources.getColor(R.color.gray, null))
        resources.getString(R.string.v1_nationality)
    } else {
        setTextColor(resources.getColor(R.color.blackRussian, null))
        nationality
    }
    bindNation(text.toString())
}

@BindingAdapter("bindHtmlText")
fun TextView.bindHtmlText(name: String?) {
    name?.apply {
        text =
            Html.fromHtml(name, FROM_HTML_MODE_LEGACY)
    }
}

@BindingAdapter("bindTextStatus")
fun TextView.bindTextStatus(status: String?) {
    status?.apply {
        when (status) {
            AppConstant.PENDING -> {
                text = resources.getString(R.string.v1_pending)
                setTextColor(resources.getColor(R.color.dimGray, null))
            }
            AppConstant.APPROVED -> {
                text = resources.getString(R.string.v1_approved)
                setTextColor(resources.getColor(R.color.green, null))
            }
            AppConstant.CANCELED -> {
                text = resources.getString(R.string.v1_canceled)
                setTextColor(resources.getColor(R.color.alizarinCrimson, null))
            }
            AppConstant.PAYING -> {
                text = resources.getString(R.string.v1_paying)
                setTextColor(resources.getColor(R.color.orange, null))
            }
        }
    }
}

@BindingAdapter("handleErrorMessage")
fun TextInputLayout.handleErrorMessage(msg: Int) {
    if (msg > 1) {
        requestFocus()
        hintTextColor =
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.alizarinCrimson))
        boxStrokeWidthFocused = dpToPixels(1)
        boxStrokeColor = ContextCompat.getColor(context, R.color.alizarinCrimson)
    } else {
        hintTextColor = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.black))
        boxStrokeWidthFocused = dpToPixels(1)
        boxStrokeColor = ContextCompat.getColor(context, R.color.black)
    }
}

@BindingAdapter("displayErrorMessageNe")
fun TextView.displayErrorMessageNe(msg: Int) {
    visibility = if (msg > 1) {
        text = context.getString(msg)
        View.VISIBLE
    } else {
        text = AppConstant.EMPTY
        View.GONE
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("setPhoneNumber")
fun TextView.setPhoneNumber(number: String?) {
    if (!number.isNullOrBlank()) {
        val temp = number.trim().replace(" ", "")
        if (temp.length > 7) {
            text = "${temp.substring(0, 4)} ${temp.substring(4, 7)} ${temp.substring(7)}"
        }
    }
}

fun Int.toTimeDisplay(): String = String.format("%02d:%02d", this / 60, this % 60)


fun String.toPhoneNumberDisplay(): String {
    var result = this
    try {
        result = PhoneFormatTextWatcher().format(this)
    } catch (e: Exception) {
        LogUtils.e(e.stackTraceToString())
    }
    return result
}
