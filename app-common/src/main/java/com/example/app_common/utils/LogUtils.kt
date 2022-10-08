package com.example.app_common.utils

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import com.example.app_common.BuildConfig
import com.example.app_common.R
import com.example.app_common.utils.bindingadapter.dpToPx
import com.example.app_common.utils.bindingadapter.setMargins

object LogUtils {
    private var DEBUG = BuildConfig.DEBUG

    private const val TRACE_METHOD = "trace"

    private const val START_LOG_METHOD = "startLogMethod"

    private const val END_LOG_METHOD = "endLogMethod"

    private const val CLASS_NAME_INDEX = 0

    private const val METHOD_NAME_INDEX = 1

    /*
     * set Debug Mode for all module.
     *
     * @param debugMode Boolean input value of debugMode.
     */
    fun setDebugMode(debugMode: Boolean) {
        DEBUG = debugMode
    }

    /*
     * Send an information log message.
     *
     * @param content The message you would like logged.
     */
    fun i(content: String) {
        if (DEBUG) {
            val msg = trace()
            if (msg != null) {
                i(
                    msg[CLASS_NAME_INDEX],
                    msg[METHOD_NAME_INDEX] + content
                )
            }
        }
    }

    private fun i(tag: String, content: String) {
        if (DEBUG) {
            Log.i(tag, content)
        }
    }

    /*
     * Send an error log message.
     *
     * @param content The message you would like logged.
     */
    fun e(content: String) {
        if (DEBUG) {
            val msg = trace()
            if (msg != null) {
                e(
                    msg[CLASS_NAME_INDEX],
                    msg[METHOD_NAME_INDEX] + content
                )
            }
        }
    }

    private fun e(tag: String, content: String) {
        if (DEBUG) {
            Log.e(tag, content)
        }
    }

    /*
     * Send an debug log message.
     *
     * @param content The message you would like logged.
     */
    fun d(content: String) {
        if (DEBUG) {
            val msg = trace()
            if (msg != null) {
                d(
                    msg[CLASS_NAME_INDEX],
                    "[ " + msg[METHOD_NAME_INDEX] + " ] " + content
                )
            }
        }
    }

    /*
     * Send an warning log message.
     *
     * @param content The message you would like logged.
     */
    fun w(content: String) {
        if (DEBUG) {
            val msg = trace()
            if (msg != null) {
                w(
                    msg[CLASS_NAME_INDEX],
                    "[ " + msg[METHOD_NAME_INDEX] + " ] " + content
                )
            }
        }
    }

    private fun d(tag: String, content: String) {
        if (DEBUG) {
            Log.d(tag, content)
        }
    }

    private fun w(tag: String, content: String) {
        if (DEBUG) {
            Log.w(tag, content)
        }
    }

    private fun trace(): Array<String>? {
        var index = 0
        val stackTraceElements = Thread.currentThread().stackTrace ?: return null
        for (i in stackTraceElements.indices) {
            val ste = stackTraceElements[i]
            if ((ste.className == LogUtils::class.java.name) &&
                (ste.methodName.contains(TRACE_METHOD))
            ) {
                // index for startEndMethodLog method
                index = i + 2
                if (index < stackTraceElements.size &&
                    stackTraceElements[index].methodName.contains(START_LOG_METHOD) ||
                    index < stackTraceElements.size &&
                    stackTraceElements[index].methodName.contains(END_LOG_METHOD)
                ) {
                    break
                }
                // index for d method
                index = i + 1
                break
            }
        }

        // index for method call d or startEndMethodLog method
        index++

        if ((stackTraceElements.size >= index) && (stackTraceElements[index] != null)) {
            return arrayOf(
                stackTraceElements[index].fileName,
                stackTraceElements[index].methodName +
                        "[" + stackTraceElements[index].lineNumber + "] "
            )
        }
        return null
    }

    fun layoutListener(childView: View, parentView: View, context: Context?) = object :
        ViewTreeObserver.OnGlobalLayoutListener {
        private val defaultKeyboardHeightDP = 100
        private val estimatedKeyboardDP = defaultKeyboardHeightDP + 48
        private val rect: Rect = Rect()
        val instance = this
        val globalLayoutListenerAgreeButton = object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                childView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                parentView.viewTreeObserver.addOnGlobalLayoutListener(instance)
            }
        }

        override fun onGlobalLayout() {
            parentView.viewTreeObserver.removeOnGlobalLayoutListener(instance)
            val estimatedKeyboardHeight = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                estimatedKeyboardDP.toFloat(),
                parentView.resources.displayMetrics
            ).toInt()
            parentView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = parentView.height
            val heightDiffKeyboard = screenHeight - rect.height()
            val isVisibleKeyboard = heightDiffKeyboard >= estimatedKeyboardHeight
            onVisibilityChangedKeyboard(
                visible = isVisibleKeyboard,
                button = childView,
                context = context,
                heightDiffKeyboard = heightDiffKeyboard
            )
            childView.viewTreeObserver.addOnGlobalLayoutListener(
                globalLayoutListenerAgreeButton
            )
        }
    }

    fun onVisibilityChangedKeyboard(
        visible: Boolean,
        button: View,
        context: Context?,
        heightDiffKeyboard: Int
    ) {
        if (visible) {
            button.setMargins(
                R.dimen.margin_16dp.dpToPx(context),
                R.dimen.margin_12dp.dpToPx(context),
                R.dimen.margin_16dp.dpToPx(context),
                heightDiffKeyboard + R.dimen.margin_12dp.dpToPx(context)
            )
        } else {
            button.setMargins(
                R.dimen.margin_16dp.dpToPx(context),
                R.dimen.margin_12dp.dpToPx(context),
                R.dimen.margin_16dp.dpToPx(context),
                R.dimen.margin_16dp.dpToPx(context)
            )
        }
    }
}
