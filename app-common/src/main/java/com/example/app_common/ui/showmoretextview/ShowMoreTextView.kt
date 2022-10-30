package com.example.app_common.ui.showmoretextview

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.SpannedString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.widget.AppCompatTextView
import com.example.app_common.R
import com.example.app_common.utils.LogUtils

class ShowMoreTextView : AppCompatTextView {

    companion object {
        const val SHOW_MORE = "Show more"
        const val SHOW_LESS = "Show less"
        const val DEFAULT_SHOWING_LINE = 1
    }

    var onClickShowMore: View.() -> Unit = {}
    var onClickShowLess: View.() -> Unit = {}

    private val shortTextNoSeeMoreStringBuilder = SpannableStringBuilder()
    private val shortTextSeeMoreStringBuilder = SpannableStringBuilder()

    constructor(context: Context) : super(context)

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        val typedArray =
            context.theme.obtainStyledAttributes(attributeSet, R.styleable.ShowMoreTextView, 0, 0)
        try {
            showingLine = typedArray.getInt(R.styleable.ShowMoreTextView_showingLine, 1)
            showMore =
                typedArray.getString(R.styleable.ShowMoreTextView_showMoreText) ?: "Show more"
            showLess =
                typedArray.getString(R.styleable.ShowMoreTextView_showLessText) ?: "Show less"
            showLessTextColor =
                typedArray.getColor(R.styleable.ShowMoreTextView_showMoreTextColor, Color.RED)
            showMoreTextColor =
                typedArray.getColor(R.styleable.ShowMoreTextView_showLessTextColor, Color.RED)
        } catch (e: Exception) {
            LogUtils.e(e.stackTraceToString())
        }
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    )

    private var showingLine = DEFAULT_SHOWING_LINE

    private var showMore = SHOW_MORE
    private var showLess = SHOW_LESS

    private var showMoreTextColor = Color.RED
    private var showLessTextColor = Color.RED

    private val tab = " "

    private var mainSpannedString: SpannedString? = null

    private var isAlreadySet = false
    private var isCollapse = true

    init {
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                text = text.trim()
                if (showingLine >= lineCount) return
                try {
                    while (text.substring(text.length - 1) == "\n") {
                        text = text.substring(0, text.length - 1)
                    }
                } catch (e: Exception) {
                    LogUtils.e(e.toString())
                }
                if (showingLine >= lineCount) return
                showMoreButton()
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun showMoreButton() {
        try {
            if (!isAlreadySet) {
                mainSpannedString = SpannedString(text)
                isAlreadySet = true
            }
            var start = 0
            var end: Int
            if (shortTextSeeMoreStringBuilder.isEmpty()) {
                for (i in 0 until showingLine) {
                    end = layout.getLineEnd(i)
                    mainSpannedString?.subSequence(start, end)?.apply {
                        shortTextNoSeeMoreStringBuilder.append(this)
                    }
                    start = end
                }
                var specialSpace = 0
                do {
                    shortTextSeeMoreStringBuilder.clear()
                    shortTextSeeMoreStringBuilder.append(
                        shortTextNoSeeMoreStringBuilder.subSequence(
                            0, shortTextNoSeeMoreStringBuilder.length
                                    - (specialSpace)
                        )
                    )
                    if (shortTextSeeMoreStringBuilder[shortTextSeeMoreStringBuilder.length - 1].toString() == tab) {
                        shortTextSeeMoreStringBuilder.append(showMore)
                    } else {
                        shortTextSeeMoreStringBuilder.append("$tab$showMore")
                    }
                    text = shortTextSeeMoreStringBuilder
                    specialSpace++
                } while (lineCount > showingLine)
                displayShortTextSeeMore()
            } else {
                text = shortTextSeeMoreStringBuilder
                displayShortTextSeeMore()
            }
        } catch (e: java.lang.Exception) {
            LogUtils.e(e.stackTraceToString())
        }
    }

    private fun displayShortTextSeeMore() {
        post {
            isCollapse = true
            setShowMoreColoringAndClickable()
        }
    }

    private fun setShowMoreColoringAndClickable() {
        val spannableString = SpannableString(text)
        spannableString.setSpan(
            object : ClickableSpan() {
                override fun updateDrawState(paint: TextPaint) {
                    super.updateDrawState(paint)
                    highlightColor = Color.TRANSPARENT
                }

                override fun onClick(view: View) {
                    maxLines = Int.MAX_VALUE
                    text = mainSpannedString
                    isCollapse = false
                    try {
                        showLessButton()
                    } catch (e: java.lang.Exception) {
                        LogUtils.e(e.stackTraceToString())
                    }
                    onClickShowMore()
                }
            }, text.length - showMore.length, text.length, 0
        )
        spannableString.setSpan(
            ForegroundColorSpan(showMoreTextColor),
            text.length - showMore.length,
            text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            text.length - showMore.length,
            text.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        movementMethod = LinkMovementMethod.getInstance()
        text = spannableString
    }

    private fun showLessButton() {
        val spannableString: SpannableStringBuilder = if (text.substring(text.length - 1) == "\n") {
            SpannableStringBuilder(
                this.text.subSequence(
                    0,
                    this.text.length - 1
                )
            ).append("$tab$showLess")
        } else {
            SpannableStringBuilder(text).append("$tab$showLess")
        }
        if (spannableString.lines().last().length < showLess.length) {
            val oldSpannable =
                spannableString.subSequence(0, spannableString.length - showLess.length)
            spannableString.clear()
            spannableString.append(oldSpannable).append("\n").append(showLess)
        }
        spannableString.setSpan(
            object : ClickableSpan() {
                override fun updateDrawState(pain: TextPaint) {
                    super.updateDrawState(pain)
                    highlightColor = Color.TRANSPARENT
                }

                override fun onClick(view: View) {
                    maxLines = showingLine
                    showMoreButton()
                    onClickShowLess()
                }
            }, spannableString.length - showLess.length, spannableString.length, 0
        )
        spannableString.setSpan(
            ForegroundColorSpan(showLessTextColor),
            spannableString.length - (1 + showLess.length),
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            spannableString.length - (1 + showLess.length),
            spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        movementMethod = LinkMovementMethod.getInstance()
        text = spannableString
    }
}
