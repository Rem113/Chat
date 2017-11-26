package com.rem113.chat

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_topic_viewer.*
import java.util.*

class TopicViewer : AppCompatActivity() {

    companion object {
        private val TAG = "TopicViewer"
        private val MESSAGES_KEY = "messages"
        private val DATE = "date"
        private val TEXT = "text"
        private val USER = "username"
    }

    // Firestore
    private val mDatabase = FirebaseFirestore.getInstance()
    private var mMessagesRef = mDatabase.collection(MESSAGES_KEY)

    // FirebaseAuth
    private val mAuth = FirebaseAuth.getInstance()
    private val mUser = mAuth.currentUser

    // My variables
    private val mMessages = mutableListOf<Message>()
    private var topic: TopicReference = intent.getParcelableExtra("info")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic_viewer)

        title = topic.title

        send.setOnClickListener { sendMessage() }

        chatbox.layoutManager = LinearLayoutManager(this)
        chatbox.adapter = MessageAdapter(mMessages, this)

        mMessagesRef = mMessagesRef.document(topic.messages).collection(MESSAGES_KEY)

        mMessagesRef.orderBy(DATE).addSnapshotListener { query, e ->
            if (e != null) {
                Log.w(TAG, e)
                return@addSnapshotListener
            }

            query.documentChanges.filter { it.type == DocumentChange.Type.ADDED }
                    .mapTo(mMessages) { it ->
                        Message((it.document[DATE] as? Date) ?: Date(0),
                                (it.document[TEXT] as? String) ?: "",
                                (it.document[USER] as? String) ?: "")
                    }

            chatbox.adapter.notifyDataSetChanged()
        }
    }

    private fun sendMessage() {
        val messageContent = messageText.text.toString()

        mMessagesRef.add(Message(Calendar.getInstance().time as Date, messageContent, mUser?.displayName.toString()))

        messageText.setText("")
    }
}
