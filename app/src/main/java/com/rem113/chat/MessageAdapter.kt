package com.rem113.chat

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.text.format.DateUtils
import kotlinx.android.synthetic.main.message_item.view.*


class MessageAdapter(val datas: MutableList<Message>,
                     private val context: Context) :
        RecyclerView.Adapter<MessageAdapter.MessageHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder =
            MessageHolder(parent.inflate(R.layout.message_item))

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: MessageHolder, position: Int) {
        val temp = datas[position]
        holder.textMessage.text = temp.text
        holder.username.text = temp.username
        holder.date.text = DateUtils.getRelativeTimeSpanString(context, temp.date.time)
    }

    data class MessageHolder(private val view: View,
                             var textMessage: TextView = view.textMessage,
                             var username: TextView = view.username,
                             var date: TextView = view.date) :
            RecyclerView.ViewHolder(view)
}