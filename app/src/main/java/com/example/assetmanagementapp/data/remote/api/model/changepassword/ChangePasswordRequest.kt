package com.example.assetmanagementapp.data.remote.api.model.changepassword

data class ChangePasswordRequest(
    val phoneNumber: String,
    val currentPassword: String,
    val newPassword: String
)
