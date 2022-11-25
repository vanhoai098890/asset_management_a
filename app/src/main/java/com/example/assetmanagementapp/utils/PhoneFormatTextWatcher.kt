package com.example.assetmanagementapp.utils

import android.text.Editable
import com.example.app_common.utils.text_watcher.TextWatcherImpl
import java.text.MessageFormat

open class PhoneFormatTextWatcher : TextWatcherImpl() {
    companion object {
        private const val MAX_LENGTH_2_GROUP = 7
        private const val MAX_LENGTH_1_GROUP = 4
        private const val PATTERN_TWO_GROUP = "{0} {1}"
        private const val PATTERN_THREE_GROUP = "{0} {1} {2}"
        private const val SPACE = " "
        private val phoneMsgFmt2Groups = MessageFormat(PATTERN_TWO_GROUP)
        private val phoneMsgFmt3Groups = MessageFormat(PATTERN_THREE_GROUP)

        /**
         * Region Private
         */
        fun format(phoneNumber: String): String {
            var result = phoneNumber
            if (phoneNumber.length in (MAX_LENGTH_1_GROUP + 1)..MAX_LENGTH_2_GROUP) {
                result = phoneMsgFmt2Groups.format(getPhoneNumArrFollow2Groups(phoneNumber))
            } else if (phoneNumber.length > MAX_LENGTH_2_GROUP) {
                result = phoneMsgFmt3Groups.format(getPhoneNumArrFollow3Groups(phoneNumber))
            }
            return result
        }

        private fun getPhoneNumArrFollow2Groups(phoneRawString: String) = arrayOf(
            phoneRawString.substring(0, MAX_LENGTH_1_GROUP),
            phoneRawString.substring(MAX_LENGTH_1_GROUP, phoneRawString.length),
        )

        private fun getPhoneNumArrFollow3Groups(phoneRawString: String) = arrayOf(
            phoneRawString.substring(0, MAX_LENGTH_1_GROUP),
            phoneRawString.substring(
                MAX_LENGTH_1_GROUP,
                MAX_LENGTH_2_GROUP
            ),
            phoneRawString.substring(MAX_LENGTH_2_GROUP)
        )
    }

    protected var rawPhone: String = ""

    override fun afterTextChanged(s: Editable?) {
        rawPhone = s?.toString()?.replace(SPACE, "") ?: rawPhone
        if (rawPhone.isNotBlank()) {
            handleReplaceEditText()
        }
    }

    private fun handleReplaceEditText() {
        refView?.run {
            removeTextChangedListener(this@PhoneFormatTextWatcher)
            val newText = format(rawPhone)
            setText(newText)
            if (newText.length > 13) {
                setSelection(13)
            } else {
                setSelection(newText.length)
            }
            addTextChangedListener(this@PhoneFormatTextWatcher)
        }
    }
}
