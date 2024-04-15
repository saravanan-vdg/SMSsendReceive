package com.sai.mysms

import android.Manifest
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.peoplecert.interlocutor.base.BaseActivity
import com.sai.mysms.databinding.ActivityReceiverBinding
import com.sai.mysms.databinding.ActivitySenderBinding
import com.sai.mysms.model.SmsDetail
import com.sai.mysms.utils.DateFormatter
import com.sai.mysms.utils.EncryptionUtils
import com.sai.mysms.utils.hasPermissions
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class ReceiverActivity : BaseActivity<ActivityReceiverBinding>(ActivityReceiverBinding::inflate) {




    private val permissions= hashMapOf<String,String>(
        Pair(Manifest.permission.SEND_SMS,"send sms"),
        Pair(Manifest.permission.READ_SMS,"read sms"),
    )

    private var permissionGranted: Boolean=false
    private val encryptionUtils= EncryptionUtils()
    lateinit var viewModel: SmsViewModel
    lateinit var adapter: SmsViewAdapter

    private val permissionReqLauncher =registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perm ->
        val deniedPermission = arrayListOf<String>()
        perm.entries.forEach {
            if (!it.value) {
                permissions[it.key]?.let { it1 -> deniedPermission.add(it1) }
            }
        }
        permissionGranted = perm.entries.all {
            it.value == true
        }
        if(permissionGranted){
            readSms()
        }
    }

    fun readSms() {
        Log.e("tag","readsms")
        val contentResolver: ContentResolver = getContentResolver()
        viewModel.readSmsinBox(contentResolver,getApplicationContext().getPackageName())

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel= ViewModelProvider(this).get(SmsViewModel::class.java)
        permissionGranted = hasPermissions(this, permissions.keys.toTypedArray())
        adapter=SmsViewAdapter(arrayListOf(), DateFormatter())
        binding.receiverRecyclerView.adapter=adapter

        viewModel.getReceiverSms(1).observe(this){
            adapter.setItem(it)
        }
        viewModel.showProgressBar.observe(this){
            if (it) {
                showProgressDialog()
            } else {
                hideProgressDialog()
            }
        }

        if(!permissionGranted){
            Log.e("tag","launch")
            permissionReqLauncher.launch(permissions.keys.toTypedArray())
        }else{
            Log.e("tag","readsms")
            readSms()
        }

    }
}