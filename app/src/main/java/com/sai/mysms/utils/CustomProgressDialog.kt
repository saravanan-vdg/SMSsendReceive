package com.peoplecert.interlocutor.utils

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.sai.mysms.R

class CustomProgressDialog(context: Context) : Dialog(context, R.style.progressTheme) {

    init {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_progress)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }
}
