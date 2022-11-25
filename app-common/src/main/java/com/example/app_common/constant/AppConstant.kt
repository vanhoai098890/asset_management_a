package com.example.app_common.constant

import androidx.viewpager2.widget.CompositePageTransformer

object AppConstant {
    const val PREF_NAME: String = "NRent_pref"

    /**
     * LEVEL of fragment using control back stack smooth
     */
    const val LEVEL_TOP = 0
    const val LEVEL_CONTAINER = 1
    const val LEVEL_TAB = 2
    const val EMPTY = ""

    const val KEY_AUTHORIZATION = "Authorization"
    const val VALUE_AUTHORIZATION_PREFIX = "Bearer"
    const val EMAIL_PATTERN = "\\w+@\\w+(\\.\\w){1,2}"
    const val CITIZEN_ID_PATTERN = "\\d{9,12}"
    const val PHONE_PATTERN = "0\\d{0,10}"
    const val ZERO = 0
    const val DEFAULT_INTERVAL = 1000L
    const val FORMAT_DATE = "yyyy-MM-dd"
    const val FORMAT_DATE_V2 = "yyyy-MM-dd'T'HH:mm"
    const val APPROVED = "APPROVED"
    const val PENDING = "PENDING"
    const val RETURNED = "RETURNED"
    const val CANCELED = "CANCELLED"
    const val LOADING_BITMAP = 1
    const val ERROR_BITMAP = 2
    const val SHOW_BITMAP = 3
    const val REGEX_BASE_64_IMAGE: String = "data:image/[a-z]+;base64,"
    const val SCAN_QR_RESULT = "SCAN_QR_RESULT"

    val COMPOSITE_VIEWPAGER = CompositePageTransformer().apply {
        addTransformer { page, position ->
            val r = 1 - kotlin.math.abs(position)
            page.scaleY = 0.65f + r * 0.35f
            page.scaleX = 0.65f + r * 0.35f
            page.alpha = 0.4f + r * 0.6f
        }
    }
}
