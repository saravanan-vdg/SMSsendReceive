package com.peoplecert.interlocutor.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.peoplecert.interlocutor.utils.CustomProgressDialog

abstract class BaseActivity<viewBinding : ViewBinding>
    (private val bindingInflater: (inflater: LayoutInflater) -> viewBinding) : AppCompatActivity() {

    val binding: viewBinding by lazy { bindingInflater(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    private val progressDialog by lazy {
        CustomProgressDialog(this)
    }

    open fun showProgressDialog() {
        if (!progressDialog.isShowing) {
            progressDialog.show()
        }
    }

    open fun hideProgressDialog() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        hideProgressDialog()
        super.onDestroy()
    }

}