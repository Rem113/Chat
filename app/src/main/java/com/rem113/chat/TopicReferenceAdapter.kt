package com.rem113.chat

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.topic_item.view.*


class TopicReferenceAdapter(val data : MutableList<TopicReference>, private val context : Context) :
        RecyclerView.Adapter<TopicReferenceAdapter.TopicHolder>() {
    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: TopicHolder, position: Int) {
        val temp = data[position]
        holder.mTitle.text = temp.title
        holder.mLast.text = temp.last
        holder.mDate.text = temp.date.toString()
        holder.view.setOnClickListener {
            val intent = Intent(context, TopicViewer::class.java)
            intent.putExtra("info", temp)
            startActivity(context, intent, null)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = TopicHolder(parent.inflate(R.layout.topic_item))


    data class TopicHolder(val view : View,
                           val mTitle : TextView = view.topic_name,
                           val mLast : TextView = view.last_message,
                           val mDate : TextView = view.date) : RecyclerView.ViewHolder(view)
}