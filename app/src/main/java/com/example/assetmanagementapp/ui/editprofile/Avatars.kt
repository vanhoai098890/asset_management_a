package com.example.assetmanagementapp.ui.editprofile

data class Avatars(
    val numberImage: Int = 0,
    var isSelect: Boolean = false
)

data class StateEditProfile(
    val stateListAvatars: List<Avatars> = mutableListOf(),
    val stateNickName: String = "",
    val stateOnSuccess: Boolean = false
)
