package com.sai.mysms

import android.content.ContentResolver
import android.provider.Telephony
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peoplecert.interlocutor.database.SmsDao
import com.sai.mysms.model.SmsDetail
import com.sai.mysms.utils.DateFormatter
import com.sai.mysms.utils.EncryptionUtils
import com.sai.mysms.utils.optString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class SmsViewModel @Inject constructor(
    private val smsDao: SmsDao,
    private val encryptionUtils: EncryptionUtils
): ViewModel() {



    var _showProgressBar = MutableLiveData<Boolean>()
    var showProgressBar: LiveData<Boolean> = _showProgressBar


    fun getSenderSms(type:Int)=smsDao.getRecentSender(type)
    fun getReceiverSms(type:Int)=smsDao.getRecentSender(type)

    fun insetTOdo(smsDetail: SmsDetail){
        viewModelScope.launch(Dispatchers.IO) {
           val id= smsDao.insert(smsDetail)
        }
    }



    fun readSmsinBox(contentResolver: ContentResolver,creator: String) {
        _showProgressBar.value=true
        viewModelScope.launch(Dispatchers.IO) {
            val arrayList= arrayListOf<SmsDetail>()
            val calendar=Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH,-10)
            Log.e("-day",DateFormatter().formatDate(calendar.time))
            val cursor = contentResolver.query(
                Telephony.Sms.CONTENT_URI,
                null,
                "${Telephony.Sms.DATE}>? AND ${Telephony.Sms.CREATOR} !=? AND ${Telephony.Sms.BODY} like ?",
                arrayOf(calendar.timeInMillis.toString(),creator,"%SMSEncrDecr:%"),
                "${Telephony.Sms.DATE} desc "
            )
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val address = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                    val body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY))
                    val creator = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.CREATOR))
                    val date = cursor.getLong(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE))
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(Telephony.Sms._ID))
                    Log.e("tag","Sender: $address\nMessage: $body\n" +
                            "creator: $creator\n" +
                            "date: $date")
                    if(!body.optString("").isNullOrEmpty() && body.contains("SMSEncrDecr:",true)){
                        val encList=body.split(":")
                        if(!encList.isNullOrEmpty() && encList.get(1).optString("").isNotEmpty()){
                            val smsDetail=SmsDetail(id=id,message =encryptionUtils.decrypt(encList.get(1)), mobileNo = address, createdAt = Date(date), type = 1)
                            arrayList.add(smsDetail)
                        }
                    }
                } while (cursor.moveToNext())
                if(arrayList.isNotEmpty()) {
                    smsDao.insertAll(arrayList)
                }
            }
            cursor?.close()
            _showProgressBar.postValue(false)

        }
    }

    fun readSenderSms(contentResolver: ContentResolver, creator: String) {
        _showProgressBar.value=true
        viewModelScope.launch(Dispatchers.IO) {

            val arrayList= arrayListOf<SmsDetail>()
            val calendar=Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH,-10)
            Log.e("-day",DateFormatter().formatDate(calendar.time))
            val cursor = contentResolver.query(
                Telephony.Sms.CONTENT_URI,
                null,
                "${Telephony.Sms.DATE}>? AND ${Telephony.Sms.CREATOR}=? AND ${Telephony.Sms.BODY} like ?",
                arrayOf(calendar.timeInMillis.toString(),creator,"%SMSEncrDecr:%"),
                "${Telephony.Sms.DATE} desc "
            )
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val address = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                    val body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY))
                    val creator = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.CREATOR))
                    val date = cursor.getLong(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE))
                    val id = cursor.getLong(cursor.getColumnIndexOrThrow(Telephony.Sms._ID))
                    // smsList.add("Sender: $address\nMessage: $body")
                    Log.e("tag","Sender: $address\nMessage: $body\n" +
                            "creator: $creator\n" +
                            "date: $date")
                    if(!body.optString("").isNullOrEmpty() && body.contains("SMSEncrDecr:",true)){
                        val encList=body.split(":")
                        if(!encList.isNullOrEmpty() && encList.get(1).optString("").isNotEmpty()){
                            val smsDetail=SmsDetail(id=id,message =encryptionUtils.decrypt(encList.get(1)), mobileNo = address, createdAt = Date(date), type = 0)
                            arrayList.add(smsDetail)
                        }
                    }

                } while (cursor.moveToNext())
                if(arrayList.isNotEmpty()) {
                    smsDao.insertAll(arrayList)
                }
            }
            cursor?.close()
            _showProgressBar.postValue(false)
        }
    }


}