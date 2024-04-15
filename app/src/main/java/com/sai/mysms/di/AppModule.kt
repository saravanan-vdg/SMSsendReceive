package com.sai.mysms.di

import android.content.Context
import androidx.room.Room
import com.peoplecert.interlocutor.database.SmsDao
import com.sai.mysms.database.SmsDatabase
import com.sai.mysms.utils.EncryptionUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context):SmsDatabase{
        return Room.databaseBuilder(context = context,
            SmsDatabase::class.java,
            "smsDet_db").build()
    }

    @Provides
    @Singleton
    fun provideTodoDao(smsDatabase: SmsDatabase):SmsDao{
        return smsDatabase.smsDao()
    }


    @Provides
    @Singleton
    fun provideEncryptionUtils(): EncryptionUtils {
        return EncryptionUtils()
    }

}