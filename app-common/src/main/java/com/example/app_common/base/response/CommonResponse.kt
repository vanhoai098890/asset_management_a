package com.example.app_common.base.response

import com.example.app_common.R
import com.google.gson.annotations.SerializedName

open class CommonResponse(
    @SerializedName("code")
    val code: String = "",
    @SerializedName("message")
    val message: String = "",
)

enum class ApiResponseCode(val code: String, val idStringResource: Int = 0) {
    SUCCESSFUL("200"),
    INVALID_PHONE_NUMBER("INVALID_PHONE_NUMBER"),
    UNAUTHORIZED("E401"),
    E504("E504", R.string.v1_wrong_field),
    E402("E402", R.string.v1_username_is_existed),
    E005("E005", R.string.common_message_error_e005),
    E007("E007"),
    E087("E087"),
    E008("E008", R.string.common_message_error_e008),
    E009("E009", R.string.common_message_error_e009),
    E500("E500", R.string.common_message_error_e500),
    E020("E020"),
    E021("E021", R.string.common_message_error_e021),
    E026("E026", R.string.common_message_error_e026),
    E027("E027", R.string.common_message_error_e027),
    E033("E033", R.string.common_message_error_e033),
    E034("E034", R.string.common_message_error_e034),
    E037("E037", R.string.common_message_error_e037),
    E006("E006", R.string.common_message_error_e003),
}
