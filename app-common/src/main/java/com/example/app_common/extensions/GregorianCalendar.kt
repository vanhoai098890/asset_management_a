package com.example.app_common.extensions

import android.annotation.SuppressLint
import com.example.app_common.constant.AppConstant
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

fun GregorianCalendar.calcDatesWith(date: GregorianCalendar): Int {
    val firstDate = GregorianCalendar(
        this.get(Calendar.YEAR),
        this.get(Calendar.MONTH),
        this.get(Calendar.DATE)
    )
    val secondDate = GregorianCalendar(
        date.get(Calendar.YEAR),
        date.get(Calendar.MONTH),
        date.get(Calendar.DATE)
    )
    var result = 1
    while (firstDate.before(secondDate)) {
        result += 1
        firstDate.add(Calendar.DAY_OF_MONTH, 1);
    }
    return result
}

@SuppressLint("SimpleDateFormat")
fun GregorianCalendar.getFormatString(pattern: String = AppConstant.FORMAT_DATE): String {
    return SimpleDateFormat(pattern).format(this.time)
}

fun GregorianCalendar.copy(): GregorianCalendar {
    return GregorianCalendar(get(Calendar.YEAR), get(Calendar.MONTH), get(Calendar.DATE))
}

fun String.getGregorianCalendar(): GregorianCalendar {
    val dates = this.split('-')
    return GregorianCalendar(dates[0].toInt(), dates[1].toInt(), dates[2].toInt())
}

@SuppressLint("SimpleDateFormat")
fun Date.getFormatString(pattern: String = AppConstant.FORMAT_DATE): String {
    return SimpleDateFormat(pattern).format(this.time)
}
