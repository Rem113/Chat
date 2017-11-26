package com.rem113.chat

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_topic.*
import java.util.Calendar

class AddTopic : AppCompatActivity() {

    companion object {
        private val TOPICS_KEY = "topics"
        private val MESSAGES_KEY = "messages"
    }

    // Firestore
    private val mDatabase = FirebaseFirestore.getInstance()
    private val mCollectionRef = mDatabase.collection(Companion.TOPICS_KEY)
    private val mMessagesRef = mDatabase.collection(Companion.MESSAGES_KEY)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_topic)

        submit.setOnClickListener {
            addTopic()
        }
    }

    private fun addTopic() {
        val title = topic_title.text.toString()
        val date = Calendar.getInstance().time

        var topic: TopicReference

        mMessagesRef.add(mapOf()).addOnSuccessListener { documentReference ->

            topic = TopicReference(date, title,"Dernier message de $title", documentReference.id)

            mCollectionRef.add(topic)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, TopicViewer::class.java)
                            intent.putExtra("info", topic)
                            startActivity(intent)
                        }

                        if (task.exception != null)
                            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()

                        finish()
                    }
        }
    }
}
