package com.example.app_common.utils.text_watcher

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

abstract class TextWatcherImpl : TextWatcher {

    var refView: EditText? = null

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    override fun afterTextChanged(s: Editable?) = Unit
}
