package com.sai.mysms.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

class DateFormatter @Inject constructor() {

    @SuppressLint("SimpleDateFormat")
    private val formatter = SimpleDateFormat("d MMM yyyy HH:mm:ss")

    fun formatDate(data: Date): String {
        return formatter.format(data)
    }
}