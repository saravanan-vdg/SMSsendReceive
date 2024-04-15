package com.sai.mysms.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity
data class SmsDetail(
    @PrimaryKey(autoGenerate = true)
    var id:Long=0,
    @ColumnInfo(name = "message")
    var message:String="",
    @ColumnInfo(name = "mobile_no")
    var mobileNo:String="",
    @ColumnInfo(name = "type")
    var type:Int=0,//0=send 1=receiver
    @ColumnInfo(name="created_at")
    val createdAt: Date = Date(System.currentTimeMillis()),
)