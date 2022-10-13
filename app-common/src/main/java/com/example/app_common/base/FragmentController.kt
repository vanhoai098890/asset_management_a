package com.example.app_common.base

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
import com.example.app_common.R
import com.example.app_common.constant.AppConstant
import com.example.app_common.extensions.replaceFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class FragmentController : Fragment() {

    companion object {
        private const val LEVEL_RATIO = 2
    }

    var level: Int = 0
        private set

    fun setLevel(level: Int) {
        this.level = level
    }

    private var callBackWhenBackPress: OnBackPressedCallback = object : OnBackPressedCallback(
        false
        /** true means that the callback is enabled */
    ) {
        override fun handleOnBackPressed() {
            handleBackPressed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // note that you could enable/disable the callback here as well by setting callback.isEnabled = true/false
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callBackWhenBackPress
        )
    }

    protected fun handleAddCallBack(isEnable: Boolean = true) {
        callBackWhenBackPress.isEnabled = isEnable
    }

    override fun onPause() {
        super.onPause()
        handleAddCallBack(false)
    }

    open fun handleDragLayoutInContainer(): Boolean {
        return false
    }

    /**
     * Using tagNameBackStack if you want to back stack to fragment destination
     */
    open fun handleBackPressed(tagNameBackStack: String? = null) {
        when (level) {
            AppConstant.LEVEL_TOP, AppConstant.LEVEL_CONTAINER -> return
            AppConstant.LEVEL_TAB -> {
                if (parentFragmentManager.backStackEntryCount > 0) {
                    popToBackStackName(tagNameBackStack)
                } else {
                    /**
                     * Exit app when click back for android
                     */
                    activity?.finish()
                }
            }
            else -> {
                if (level % LEVEL_RATIO == 0) {
                    // child fragment in viewpager
                    parentFragment?.childFragmentManager?.also {
                        if (it.backStackEntryCount > 0) {
                            // pop in child viewpager
                            popToBackStackName(tagNameBackStack)
                        } else {
                            // child in viewpager size == 0
                            // pop in parent
                            (parentFragment as? FragmentController)?.handleBackPressed()
                        }
                    }

                } else {
                    // container
                    (parentFragment as? FragmentController)?.handleBackPressed()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        handleAddCallBack(true)
    }

    fun replaceFragment(
        fragment: Fragment, isAddBackStack: Boolean,
        isEnableAnim: Boolean = true, tagNameBackStack: String? = null
    ) {
        val range = level - AppConstant.LEVEL_CONTAINER
        when (level) {
            // do not use fragment manager of activity
            AppConstant.LEVEL_TOP -> return
            AppConstant.LEVEL_CONTAINER -> {
                // add in main tab folow
                replaceInContainer(
                    fragment,
                    isAddBackStack,
                    isEnableAnim,
                    tagNameBackStack
                )
            }
            else -> {
                // get fragment manager of fragment when level = 1(Main top container)
                var parentFm: Fragment? = this
                for (index in 1..range) {
                    parentFm = parentFm?.parentFragment
                }
                // user fragment with level = 1 to add fragment
                (parentFm as? FragmentController)?.replaceInContainer(
                    fragment,
                    isAddBackStack,
                    isEnableAnim,
                    tagNameBackStack
                )
            }
        }
    }

    /**
     * add fragment in main tab follow
     */

    fun addFragmentNotMain(
        fragment: Fragment,
        isEnableAnim: Boolean = true,
        tagNameBackStack: String? = null
    ) {
        (requireActivity() as? BaseControlActivity)?.replaceFragment(
            fragment = fragment.apply {
                setLevel(AppConstant.LEVEL_TAB)
            },
            isAddBackStack = true,
            isEnableAnim = isEnableAnim,
            tagNameBackStack = tagNameBackStack
        )
    }

    fun addFragment(
        fragment: Fragment,
        isEnableAnim: Boolean = true,
        tagNameBackStack: String? = null
    ) {
        val range = level - AppConstant.LEVEL_CONTAINER
        when (level) {
            // do not use fragment manager of activity
            AppConstant.LEVEL_TOP -> return
            AppConstant.LEVEL_CONTAINER -> {
                // add in main tab folow
                addInContainer(
                    fragment,
                    isEnableAnim,
                    tagNameBackStack
                )
            }
            else -> {
                // get fragment manager of fragment when level = 1(Main top container)
                var parentFm: Fragment? = this
                for (index in 1..range) {
                    parentFm = parentFm?.parentFragment
                }
                // user fragment with level = 1 to add fragment
                (parentFm as? FragmentController)?.addInContainer(
                    fragment,
                    isEnableAnim,
                    tagNameBackStack
                )
            }
        }
    }

    /**
     * add fragment in viewpager
     */
    internal fun addInChildFragment(
        fragment: Fragment,
        isEnableAnim: Boolean = true,
        tagNameBackStack: String? = null
    ) {
        when {
            // do not use fragment manager of activity
            level == AppConstant.LEVEL_TOP -> return
            level == AppConstant.LEVEL_CONTAINER || level % LEVEL_RATIO != 0 -> {
                // in this come.android.myapplication.base: container level is a odd number
                addInContainer(
                    fragment,
                    isEnableAnim,
                    tagNameBackStack
                )
            }
            level == AppConstant.LEVEL_TAB || level % LEVEL_RATIO == 0 -> {
                // child in viewpager
                (parentFragment as? FragmentController)?.addInContainer(
                    fragment,
                    isEnableAnim,
                    tagNameBackStack
                )
            }
            else -> return
        }
    }

    /**
     * replace fragment in viewpager
     */
    internal fun replaceInChildFragment(
        fragment: Fragment, isAddBackStack: Boolean,
        isEnableAnim: Boolean = true, tagNameBackStack: String? = null
    ) {
        when {
            // do not use fragment manager of activity
            level == AppConstant.LEVEL_TOP -> return
            level == AppConstant.LEVEL_CONTAINER || level % LEVEL_RATIO != 0 -> {
                // in this come.android.myapplication.base: container level is a odd number
                replaceInContainer(
                    fragment,
                    isAddBackStack,
                    isEnableAnim,
                    tagNameBackStack
                )
            }
            level == AppConstant.LEVEL_TAB || level % LEVEL_RATIO == 0 -> {
                // child in viewpager
                (parentFragment as? FragmentController)?.replaceInContainer(
                    fragment,
                    isAddBackStack,
                    isEnableAnim,
                    tagNameBackStack
                )
            }
            else -> return
        }
    }

    fun replaceInContainer(
        fragment: Fragment, isAddBackStack: Boolean,
        isEnableAnim: Boolean = true, tagNameBackStack: String? = null
    ) {
        (fragment as? FragmentController)?.setLevel(level + 1)
        childFragmentManager.beginTransaction().apply {
            if (isEnableAnim) {
                setCustomAnimations(
                    R.anim.anim_slide_in_from_right, R.anim.anim_slide_out_to_left,
                    R.anim.anim_slide_enter_from_left, R.anim.anim_slide_out_to_right
                )
            }
            replace(R.id.flContainer, fragment, fragment.javaClass.simpleName)
            if (isAddBackStack) {
                addToBackStack(tagNameBackStack)
            }
            commit()
        }
    }

    internal fun addInContainer(
        fragment: Fragment,
        isEnableAnim: Boolean = true, tagNameBackStack: String? = null
    ) {
        (fragment as? FragmentController)?.setLevel(level + 1)
        childFragmentManager.beginTransaction().apply {
            if (isEnableAnim) {
                setCustomAnimations(
                    R.anim.anim_slide_in_from_right, R.anim.anim_slide_out_to_left,
                    R.anim.anim_slide_enter_from_left, R.anim.anim_slide_out_to_right
                )
            }
            add(R.id.flContainer, fragment, fragment.javaClass.simpleName)
            addToBackStack(tagNameBackStack)
            commit()
        }
    }

    private fun popToBackStackName(stackName: String?) {
        if (stackName.isNullOrBlank()) {
            parentFragmentManager.popBackStack()
        } else {
            parentFragmentManager.popBackStack(stackName, POP_BACK_STACK_INCLUSIVE)
        }
    }
}
