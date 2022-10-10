package com.example.app_common.base.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class DaggerLayoutViewModel @Inject constructor() : ViewModel() {
    val stateOpenDragLayout = MutableStateFlow(false)

    fun handleCloseDragLayout(): Boolean {
        if (stateOpenDragLayout.value) {
            stateOpenDragLayout.value = false
            return true
        }
        return false
    }
}
