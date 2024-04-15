package com.sai.mysms.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.peoplecert.interlocutor.database.SmsDao
import com.sai.mysms.model.SmsDetail


@Database(entities = [SmsDetail::class], version = 1, exportSchema = true)
@TypeConverters(DateTypeConverters::class)
abstract class SmsDatabase: RoomDatabase() {
    abstract fun smsDao():SmsDao

}