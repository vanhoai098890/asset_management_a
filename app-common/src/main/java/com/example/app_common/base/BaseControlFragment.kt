package com.example.app_common.base

import android.os.SystemClock
import androidx.fragment.app.Fragment
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

abstract class BaseControlFragment : FragmentController() {
    var timeNow = 0L
    fun addNoNavigationFragment(
        fragment: Fragment,
        isEnableAnim: Boolean = true, tagNameBackStack: String? = null
    ) {
        (activity as? BaseControlActivity)?.addNoNavigationFragment(
            fragment,
            isEnableAnim,
            tagNameBackStack
        )
    }

    fun replaceNoNavigationFragment(
        fragment: Fragment, isAddBackStack: Boolean,
        isEnableAnim: Boolean = true, tagNameBackStack: String? = null
    ) {
        (activity as? BaseControlActivity)?.replaceNoNavigationFragment(
            fragment,
            isAddBackStack,
            isEnableAnim,
            tagNameBackStack
        )
    }

    fun showLoading() {
        (activity as? BaseControlActivity)?.showLoading()
    }

    fun hideLoading() {
        (activity as? BaseControlActivity)?.hideLoading()
    }

    fun handleShowLoadingDialog(isStateShow: Boolean) {
        if (isStateShow) {
            showLoading()
        } else {
            hideLoading()
        }
    }

    fun showBottomSheet(bottomSheet: BaseBottomSheetDialogFragment) {
        bottomSheet.show(parentFragmentManager, bottomSheet.javaClass.name)
    }

    fun callSafeAction(delayInterval: Long = 500, action: () -> Unit) {
        SystemClock.elapsedRealtime().run {
            if (this - timeNow < delayInterval) {
                return
            }
            timeNow = this
            action.invoke()
        }
    }
}
