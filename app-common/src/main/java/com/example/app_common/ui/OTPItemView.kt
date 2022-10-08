package com.example.app_common.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.example.app_common.R
import com.example.app_common.extensions.dimensionDpToPixel

/**
 * Canow
 * <p>
 * Created by FPT.
 */
class OTPItemView : FrameLayout {
    companion object {
        const val ACTIVE = 1
        const val INACTIVE = 0
        const val ERROR = -1
        const val SUCCESS = 2

        private const val DEFAULT_BAR_HEIGHT = 2f
        private const val DEFAULT_OTP_TEXT_SIZE = 24f
        private const val DEFAULT_BAR_MARGIN = 2
    }

    private var textView: TextView? = null
    private var view: View? = null
    private var barActiveColor: Int = 0
    private var barInactiveColor: Int = 0
    private var barErrorColor: Int = 0
    private var barSuccessColor: Int = 0
    private var boxBackgroundColorActive: Int = 0
    private var boxBackgroundColorInactive: Int = 0
    private var boxBackgroundColorSuccess: Int = 0
    private var boxBackgroundColorError: Int = 0
    private var hideOTPDrawable: Int = 0
    private var defaultOTPDrawable: Int = 0
    private var hideOTP = false

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

    internal fun setText(value: String) {
        if (!hideOTP) {
            if (textView != null) {
                textView?.text = value
            }
        } else {
            textView?.text = ""
            if (value == "") {
                textView?.setBackgroundResource(defaultOTPDrawable)
            } else {
                textView?.setBackgroundResource(hideOTPDrawable)
            }
        }
    }

    internal fun setViewState(state: Int) {
        when (state) {
            ACTIVE -> {
                view?.setBackgroundColor(barActiveColor)
                this.setBackgroundResource(boxBackgroundColorActive)
            }
            INACTIVE -> {
                view?.setBackgroundColor(barInactiveColor)
                this.setBackgroundResource(boxBackgroundColorInactive)
            }
            ERROR -> {
                view?.setBackgroundColor(barErrorColor)
                this.setBackgroundResource(boxBackgroundColorError)
            }
            SUCCESS -> {
                view?.setBackgroundColor(barSuccessColor)
                this.setBackgroundResource(boxBackgroundColorSuccess)
            }
        }
    }

    /**
     * Region Private
     */
    @SuppressLint("CustomViewStyleable")
    private fun init(attrs: AttributeSet?) {
        val styles = context.obtainStyledAttributes(attrs, R.styleable.OTPEntryView)
        generateViews(styles)
        styles.recycle()
    }

    private fun generateViews(styles: TypedArray) {
        val defaultHeight = dimensionDpToPixel(DEFAULT_BAR_HEIGHT).toFloat()
        val defaultOtpTextSize = dimensionDpToPixel(DEFAULT_OTP_TEXT_SIZE).toFloat()
        val textColor = styles.getColor(
            R.styleable.OTPEntryView_android_textColor,
            ResourcesCompat.getColor(context.resources, R.color.black, null)
        )
        val barHeight = styles.getDimension(R.styleable.OTPEntryView_bar_height, defaultHeight)
        val barMargin = styles.getDimension(
            R.styleable.OTPEntryView_bar_margin,
            dimensionDpToPixel(0).toFloat()
        )
        var barMarginBottom = styles.getDimension(
            R.styleable.OTPEntryView_bar_margin_bottom,
            DEFAULT_BAR_MARGIN.toFloat()
        )
        var barMarginRight = styles.getDimension(
            R.styleable.OTPEntryView_bar_margin_right,
            DEFAULT_BAR_MARGIN.toFloat()
        )
        var barMarginLeft = styles.getDimension(
            R.styleable.OTPEntryView_bar_margin_left,
            DEFAULT_BAR_MARGIN.toFloat()
        )
        var barMarginTop = styles.getDimension(
            R.styleable.OTPEntryView_bar_margin_top,
            DEFAULT_BAR_MARGIN.toFloat()
        )
        hideOTP = styles.getBoolean(R.styleable.OTPEntryView_hide_otp, false)
        hideOTPDrawable =
            styles.getResourceId(
                R.styleable.OTPEntryView_hide_otp_drawable,
                defaultOTPDrawable
            )

        defaultOTPDrawable =
            ResourcesCompat.getColor(context.resources, android.R.color.transparent, null)

        val barEnabled = styles.getBoolean(R.styleable.OTPEntryView_bar_enabled, false)

        val otpTextSize =
            styles.getDimension(R.styleable.OTPEntryView_otp_text_size, defaultOtpTextSize)

        val otpTextTypeFace = styles.getString(R.styleable.OTPEntryView_text_typeface)
        val boxBackgroundColor = styles.getResourceId(
            R.styleable.OTPEntryView_otp_box_background,
            ResourcesCompat.getColor(context.resources, android.R.color.transparent, null)
        )
        boxBackgroundColorActive = styles.getResourceId(
            R.styleable.OTPEntryView_otp_box_background_active,
            boxBackgroundColor
        )
        boxBackgroundColorInactive = styles.getResourceId(
            R.styleable.OTPEntryView_otp_box_background_inactive,
            boxBackgroundColor
        )
        boxBackgroundColorSuccess = styles.getResourceId(
            R.styleable.OTPEntryView_otp_box_background_success,
            boxBackgroundColor
        )
        boxBackgroundColorError = styles.getResourceId(
            R.styleable.OTPEntryView_otp_box_background_error,
            boxBackgroundColor
        )
        barActiveColor = styles.getColor(
            R.styleable.OTPEntryView_bar_active_color,
            ResourcesCompat.getColor(context.resources, R.color.black, null)
        )
        barInactiveColor = styles.getColor(R.styleable.OTPEntryView_bar_inactive_color, Color.GRAY)
        barErrorColor = styles.getColor(
            R.styleable.OTPEntryView_bar_error_color,
            ResourcesCompat.getColor(context.resources, R.color.colorRed, null)
        )
        barSuccessColor = styles.getColor(
            R.styleable.OTPEntryView_bar_success_color,
            ResourcesCompat.getColor(context.resources, R.color.black, null)
        )

        this.setBackgroundResource(boxBackgroundColor)

        val textViewParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        textViewParams.gravity = Gravity.CENTER
        textView = TextView(context)
        textView?.gravity = Gravity.CENTER
        if (otpTextTypeFace != null) {
            try {
                val tf = Typeface.createFromAsset(context.assets, otpTextTypeFace)
                textView?.typeface = tf
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        textView?.setTextColor(textColor)
        textView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, otpTextSize)
        this.addView(textView, textViewParams)

        if (barEnabled) {
            val barViewParams = LayoutParams(LayoutParams.MATCH_PARENT, barHeight.toInt())
            barViewParams.gravity = Gravity.BOTTOM
            if (barMargin != 0f) {
                barMarginLeft = barMargin
                barMarginRight = barMargin
                barMarginBottom = barMargin
                barMarginTop = barMargin
            }
            barViewParams.leftMargin = barMarginLeft.toInt()
            barViewParams.rightMargin = barMarginRight.toInt()
            barViewParams.bottomMargin = barMarginBottom.toInt()
            barViewParams.topMargin = barMarginTop.toInt()
            view = View(context)
            this.addView(view, barViewParams)
        }
    }
}
