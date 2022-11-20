package com.example.assetmanagementapp

import com.example.app_common.base.viewmodel.BaseViewModelV2
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModelV2<MainActivityState>() {
    override fun initState(): MainActivityState = MainActivityState()

    fun dispatchClickHome(isClicked: Boolean) {
        dispatchState(
            currentState.copy(
                stateClickedHome = isClicked
            )
        )
    }

    fun dispatchClickFav(isClicked: Boolean) {
        dispatchState(
            currentState.copy(
                stateCLickedFav = isClicked
            )
        )
    }
}

data class MainActivityState(
    val stateClickedHome: Boolean = false,
    val stateCLickedFav: Boolean = false
)
