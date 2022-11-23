package com.example.app_common.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.example.app_common.R
import com.example.app_common.extensions.dimensionDpToPixel
import java.util.regex.Pattern

class OTPEntryView : FrameLayout {
    companion object {
        private const val DEFAULT_LENGTH = 4
        private const val DEFAULT_HEIGHT = 48
        private const val DEFAULT_WIDTH = 48
        private const val DEFAULT_SPACE = -1
        private const val DEFAULT_SPACE_LEFT = 4
        private const val DEFAULT_SPACE_RIGHT = 4
        private const val DEFAULT_SPACE_TOP = 4
        private const val DEFAULT_SPACE_BOTTOM = 4

        private const val PATTERN = "[1234567890]*"
    }

    private var otpItemViews: MutableList<OTPItemView>? = null
    private var otpEntryChildView: OTPEntryChildView? = null
    private var length: Int = 0

    private val filter: InputFilter
        get() = InputFilter { source, start, end, _, _, _ ->
            for (i in start until end) {
                if (!Pattern.compile(PATTERN)
                        .matcher(source[i].toString())
                        .matches()
                ) {
                    return@InputFilter ""
                }
            }
            null
        }
    var otpFullyListener: (pinEntered: String) -> Unit = {}
    var onOTPChangeListener: () -> Unit = {}

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun setOnTouchListener(l: OnTouchListener) {
        super.setOnTouchListener(l)
        otpEntryChildView?.setOnTouchListener(l)
    }

    internal fun setOTP(s: CharSequence) {
        otpItemViews?.let { itemViews ->
            for (i in itemViews.indices) {
                if (i < s.length) {
                    itemViews[i].setText(s[i].toString())
                } else {
                    itemViews[i].setText("")
                }
            }
        }
    }

    /**
     * Call it if you want to show keyboard
     */
    fun requestFocusOTP() {
        otpEntryChildView?.requestFocus()
    }

    /**
     * Call it if you want to show error
     */
    fun showError() {
        otpItemViews?.let { itemViews ->
            for (itemView in itemViews) {
                itemView.setViewState(OTPItemView.ERROR)
            }
        }
    }

    /**
     * Region Private
     */
    private fun init(attrs: AttributeSet?) {
        val styles = context.obtainStyledAttributes(attrs, R.styleable.OTPEntryView)
        styleEditTexts(styles, attrs)
        styles.recycle()
    }

    private fun setOTP(otp: String) {
        otpEntryChildView?.setText(otp)
    }

    private fun styleEditTexts(styles: TypedArray, attrs: AttributeSet?) {
        length = styles.getInt(R.styleable.OTPEntryView_length, DEFAULT_LENGTH)
        generateViews(styles, attrs)
    }

    private fun generateViews(styles: TypedArray, attrs: AttributeSet?) {
        otpItemViews = ArrayList()
        if (length > 0) {
            val otp = styles.getString(R.styleable.OTPEntryView_otp)
            val width = styles.getDimension(
                R.styleable.OTPEntryView_width,
                dimensionDpToPixel(DEFAULT_WIDTH).toFloat()
            ).toInt()
            val height = styles.getDimension(
                R.styleable.OTPEntryView_height,
                dimensionDpToPixel(DEFAULT_HEIGHT).toFloat()
            ).toInt()
            val space = styles.getDimension(
                R.styleable.OTPEntryView_box_margin,
                dimensionDpToPixel(DEFAULT_SPACE).toFloat()
            ).toInt()
            val spaceLeft = styles.getDimension(
                R.styleable.OTPEntryView_box_margin_left,
                dimensionDpToPixel(DEFAULT_SPACE_LEFT).toFloat()
            ).toInt()
            val spaceRight = styles.getDimension(
                R.styleable.OTPEntryView_box_margin_right,
                dimensionDpToPixel(DEFAULT_SPACE_RIGHT).toFloat()
            ).toInt()
            val spaceTop = styles.getDimension(
                R.styleable.OTPEntryView_box_margin_top,
                dimensionDpToPixel(DEFAULT_SPACE_TOP).toFloat()
            ).toInt()
            val spaceBottom = styles.getDimension(
                R.styleable.OTPEntryView_box_margin_bottom,
                dimensionDpToPixel(DEFAULT_SPACE_BOTTOM).toFloat()
            ).toInt()
            val params = LinearLayout.LayoutParams(width, height)
            if (space > 0) {
                params.setMargins(space, space, space, space)
            } else {
                params.setMargins(spaceLeft, spaceTop, spaceRight, spaceBottom)
            }

            val editTextLayoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            editTextLayoutParams.gravity = Gravity.CENTER
            otpEntryChildView = OTPEntryChildView(context)
            otpEntryChildView?.filters = arrayOf(filter, InputFilter.LengthFilter(length))
            setTextWatcher(otpEntryChildView)
            addView(otpEntryChildView, editTextLayoutParams)

            val linearLayoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            val linearLayout = LinearLayout(context)

            addView(linearLayout, linearLayoutParams)

            for (i in 0 until length) {
                val itemView = OTPItemView(context, attrs)
                itemView.setViewState(OTPItemView.INACTIVE)
                linearLayout.addView(itemView, i, params)
                otpItemViews?.add(itemView)
            }
            if (otp != null) {
                setOTP(otp)
            } else {
                setOTP("")
            }
        } else {
            throw IllegalStateException("Please specify the length of the otp view")
        }
    }

    private fun setTextWatcher(otpEntryChildView: OTPEntryChildView?) {
        otpEntryChildView?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) =
                Unit

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length == length) {
                    otpFullyListener.invoke(s.toString())
                }
                setOTP(s)
                setFocus(s.length)
            }

            override fun afterTextChanged(s: Editable) {
                onOTPChangeListener.invoke()
            }
        })
    }

    /**
     * Set focus for next item view
     */
    private fun setFocus(length: Int) {
        otpItemViews?.let { itemViews ->
            for (i in itemViews.indices) {
                if (i == length) {
                    itemViews[i].setViewState(OTPItemView.ACTIVE)
                } else {
                    itemViews[i].setViewState(OTPItemView.INACTIVE)
                }
            }
            if (length == itemViews.size) {
                itemViews[itemViews.size - 1].setViewState(OTPItemView.ACTIVE)
            }
        }
    }
}
