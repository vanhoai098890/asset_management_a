package com.example.assetmanagementapp.ui.personal

import com.example.assetmanagementapp.R


sealed class PersonalFunctionStatic(
    val id: Int = 0,
    val drawableRes: Int = 0,
    val stringRes: Int = 0,
    val title: Int = 0,
    val isTopPerson: Boolean = false
) {
    class PERSON(val avatarId: Int = 0, val username: String = "") :
        PersonalFunctionStatic(isTopPerson = true, id = 1)

    object ACCOUNT : PersonalFunctionStatic(title = R.string.v1_account_setting, id = 2)
    object PERSONAL : PersonalFunctionStatic(
        drawableRes = R.drawable.ic_baseline_person_24,
        stringRes = R.string.v1_rent_request_information,
        id = 3
    )

    object PASSWORD : PersonalFunctionStatic(
        drawableRes = R.drawable.ic_password,
        stringRes = R.string.v1_security,
        id = 5
    )

    object MANAGEMENT : PersonalFunctionStatic(
        title = R.string.v1_management,
        id = 6
    )

    object HELP : PersonalFunctionStatic(
        drawableRes = R.drawable.ic_outline_info_24,
        stringRes = R.string.v1_help,
        id = 8
    )

    object LOGOUT : PersonalFunctionStatic(
        drawableRes = R.drawable.ic_log_out,
        stringRes = R.string.v1_logout,
        id = 9
    )
}
