package com.sai.mysms

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.peoplecert.interlocutor.base.BaseActivity
import com.sai.mysms.databinding.ActivitySenderBinding
import com.sai.mysms.model.SmsDetail
import com.sai.mysms.utils.DateFormatter
import com.sai.mysms.utils.EncryptionUtils
import com.sai.mysms.utils.hasPermissions
import com.sai.mysms.utils.hideSoftKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SenderActivity : BaseActivity<ActivitySenderBinding>(ActivitySenderBinding::inflate) {

    var SENT_ACTION = "SMS_SENT"
    var DELIVERY_ACTION = "SMS_DELIVERED"

    private var permissionGranted: Boolean=false
    private val encryptionUtils=EncryptionUtils()
    lateinit var viewModel: SmsViewModel
    lateinit var adapter: SmsViewAdapter



    private val permissions= hashMapOf<String,String>(
        Pair(Manifest.permission.SEND_SMS,"send sms"),
        Pair(Manifest.permission.READ_SMS,"read sms"),
    )

    private val permissionReqLauncher =registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perm ->
        val deniedPermission = arrayListOf<String>()
        perm.entries.forEach {
            Log.e("SMS ", "${it.value}")
            if (!it.value) {
                permissions[it.key]?.let { it1 -> deniedPermission.add(it1) }
            }
        }
        permissionGranted = perm.entries.all {
            it.value == true
        }
    }
    private val sendBroadcast= object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            hideProgressDialog()
            when (resultCode) {
                Activity.RESULT_OK -> {
                    binding.txtmobile.editText?.setText("")
                    binding.txtmessage.editText?.setText("")
                    Toast.makeText(this@SenderActivity, "SMS sent success!", Toast.LENGTH_SHORT)
                        .show()
                    readSenderSms()
                }
                SmsManager.RESULT_ERROR_NO_SERVICE ->
                        Toast.makeText(this@SenderActivity, "No active network to send SMS.", Toast.LENGTH_SHORT).show()
                SmsManager.RESULT_ERROR_RADIO_OFF,SmsManager.RESULT_ERROR_GENERIC_FAILURE,SmsManager.RESULT_ERROR_GENERIC_FAILURE ->
                        Toast.makeText(this@SenderActivity, "SMS not sent!", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private val deliveredBroadcast= object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.e("SMS ", "deliveredBroadcast")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel=ViewModelProvider(this).get(SmsViewModel::class.java)
        permissionGranted = hasPermissions(this, permissions.keys.toTypedArray())

        adapter=SmsViewAdapter(arrayListOf(), DateFormatter())
        binding.sendRecyclerView.adapter=adapter
        viewModel.getSenderSms(0).observe(this){
            Log.e("tag","observe${it.size}")
            adapter.setItem(it)
        }

        binding.sendButton.setOnClickListener {
            val phone= binding.txtmobile.editText?.text
            val message= binding.txtmessage.editText?.text
            binding.txtmobile.isErrorEnabled=false
            binding.txtmessage.isErrorEnabled=false
            var hasError=false
            if(phone.isNullOrEmpty()){
                binding.txtmobile.isErrorEnabled=true
                binding.txtmobile.error="Enter mobile number"
                hasError=true
            }
            if(message.isNullOrEmpty()){
                binding.txtmessage.isErrorEnabled=true
                binding.txtmessage.error="Enter message"
                hasError=true
            }
            if(hasError){
                return@setOnClickListener
            }else{
                hideSoftKeyboard(it)
                sendSms(phone.toString(),message.toString())
            }

        }

        if(!permissionGranted){
            Log.e("tag","launch")
            permissionReqLauncher.launch(permissions.keys.toTypedArray())
        }else{
            Log.e("tag","readsms")
            readSenderSms()
        }
    }

    private fun readSenderSms() {
        val contentResolver = getContentResolver()
        viewModel.readSenderSms(contentResolver,getApplicationContext().getPackageName())
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(sendBroadcast, IntentFilter(SENT_ACTION),RECEIVER_EXPORTED)
        registerReceiver(deliveredBroadcast,IntentFilter(DELIVERY_ACTION),RECEIVER_EXPORTED)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(sendBroadcast)
        unregisterReceiver(deliveredBroadcast)
    }


    private fun sendSms(phone: String, message: String) {
        showProgressDialog()
        if(!permissionGranted){
            permissionReqLauncher.launch(permissions.keys.toTypedArray())
            Log.e("tag","errrr")
            return
        }else{
            val encmssgs=encryptionUtils.encrypt(message)
            Log.e("tag","sms:${encmssgs}")
            Log.e("tag","sms:${encryptionUtils.decrypt(encmssgs)}")
            val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                applicationContext.getSystemService<SmsManager>(SmsManager::class.java)
            } else {
                SmsManager.getDefault()
            }
           // smsManager.sendTextMessageWithoutPersisting()
            val sentIntent = PendingIntent.getBroadcast(this, 100, Intent(SENT_ACTION),
                PendingIntent.FLAG_IMMUTABLE)
            val deliveryIntent = PendingIntent.getBroadcast(this, 200, Intent(DELIVERY_ACTION), PendingIntent.FLAG_IMMUTABLE)
            val smsDetail=SmsDetail(message=message, mobileNo = phone, type = 0)
            //viewModel.insetTOdo(smsDetail)
            val smsMessage="SMSEncrDecr:${encmssgs}"
           // smsManager.sendTextMessageWithoutPersisting(phone, null, message, sentIntent, deliveryIntent)
            smsManager.sendTextMessage(phone, null,smsMessage, sentIntent, deliveryIntent);
        }
    }
}