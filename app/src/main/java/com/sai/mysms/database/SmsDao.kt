package com.peoplecert.interlocutor.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sai.mysms.model.SmsDetail
import kotlinx.coroutines.flow.Flow
import java.util.ArrayList

@Dao
interface SmsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo:SmsDetail):Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(todo:SmsDetail)

    @Delete
    suspend fun delete(todo:SmsDetail)

    @Query("select * from SmsDetail")
    fun getALlSms(): Flow<List<SmsDetail>>

    @Query("select * from SmsDetail")
    fun getALlSmsLive(): LiveData<List<SmsDetail>>

    @Query("select * from SmsDetail where type=:type order by created_at desc")
    fun getRecentSender(type:Int): LiveData<List<SmsDetail>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(arrayList: ArrayList<SmsDetail>)


    /*  @Query("Select count() from UploadFileDetail where uploadStatus = 0 AND session_id=:id order by updated_at Desc")
    fun getPendingUploadSessionCount(id: Long?):LiveData<Long>*/


}