package com.example.assetmanagementapp.data.remote.api.model.signup.request

import com.example.app_common.constant.AppConstant

data class SignUpRequestDto(
    var customerName: String = AppConstant.EMPTY,
    var email: String = AppConstant.EMPTY,
    var cityId: Int = 0,
    var countryId: Int = 0,
    var majorId: Int = 0,
    var roleId: Int = 0,
    var phoneNumber: String = AppConstant.EMPTY,
    var password: String = AppConstant.EMPTY,
    var birthday: String = AppConstant.EMPTY,
    var address: String = AppConstant.EMPTY,
    var cmnd: String = AppConstant.EMPTY
)