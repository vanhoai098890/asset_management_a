package com.example.assetmanagementapp.ui.searchmain.chart

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import com.example.app_common.ui.tooltips.ToolTipsManager.Companion.parseText

class CustomTextView(context: Context, attrs: AttributeSet?) :
    androidx.appcompat.widget.AppCompatTextView(context, attrs) {
    var numberText: String = ""
    var number: Int = 0

    @SuppressLint("SetTextI18n")
    override fun setText(text: CharSequence?, type: BufferType?) {
        numberText = parseText(text.toString())
        super.setText(numberText, type)
    }

    @JvmName("setNumber1")
    fun setNumber(number: Int) {
        text = number.toString()
    }

}
