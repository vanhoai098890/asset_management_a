package com.example.app_common.utils

import java.util.Locale

internal object UiUtils {
    val isRtl: Boolean
        get() = isRtl(Locale.getDefault())

    private fun isRtl(locale: Locale): Boolean {
        val directionality = Character.getDirectionality(locale.displayName[0]).toInt()
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT.toInt()
                || directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC.toInt()
    }
}
