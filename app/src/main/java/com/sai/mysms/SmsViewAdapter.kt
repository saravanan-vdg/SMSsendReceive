package com.sai.mysms

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sai.mysms.databinding.TextRowItemBinding
import com.sai.mysms.model.SmsDetail
import com.sai.mysms.utils.DateFormatter
import com.sai.mysms.utils.optString

class SmsViewAdapter(
    private val smsDataSet: ArrayList<SmsDetail>,
    private val daterFormatter: DateFormatter
) : RecyclerView.Adapter<SmsViewAdapter.SmsViewHolder>() {

    class SmsViewHolder(val binding: TextRowItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsViewHolder {
        val binding=TextRowItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SmsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return smsDataSet.size
    }

    override fun onBindViewHolder(holder: SmsViewHolder, position: Int) {
        val smsDetail = smsDataSet[position]
        Log.e("tag","ada${smsDetail.message}")
        holder.binding.message.text=smsDetail.message.optString("")
        holder.binding.data.text=daterFormatter.formatDate(smsDetail.createdAt)
        holder.binding.mobile.text=smsDetail.mobileNo.optString("")
    }

    fun setItem(it: List<SmsDetail>){
        smsDataSet.clear()
        smsDataSet.addAll(it)
        notifyDataSetChanged()
    }
}