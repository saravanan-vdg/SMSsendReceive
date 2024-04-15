package com.sai.mysms

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.peoplecert.interlocutor.base.BaseActivity
import com.sai.mysms.databinding.ActivityDashBoardBinding
import com.sai.mysms.databinding.ActivityReceiverBinding
import com.sai.mysms.utils.hasPermissions

class DashBoardActivity : BaseActivity<ActivityDashBoardBinding>(ActivityDashBoardBinding::inflate) {

    private var permissionGranted: Boolean=false
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
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        permissionGranted = hasPermissions(this, permissions.keys.toTypedArray())

        binding.btnSender.setOnClickListener {
            val intent=Intent(this,SenderActivity::class.java)
            startActivity(intent)
        }
        binding.btnReceiver.setOnClickListener {
            val intent=Intent(this,ReceiverActivity::class.java)
            startActivity(intent)
        }

        if(!permissionGranted){
            Log.e("tag","launch")
            permissionReqLauncher.launch(permissions.keys.toTypedArray())
        }

    }
}