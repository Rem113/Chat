package com.rem113.chat

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
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

    // FirebaseAuth
    private val mAuth = FirebaseAuth.getInstance()
    private val mUser = mAuth.currentUser

    private var hasClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_topic)
    }

    override fun onResume() {
        super.onResume()

        submit.setOnClickListener {
            if (!hasClicked) {
                hasClicked = true
                addTopic()
            }
        }
    }

    private fun addTopic() {
        val title = topic_title.text.toString()
        val date = Calendar.getInstance().time

        var topic: TopicReference

        mMessagesRef.add(mapOf()).addOnSuccessListener {
            documentReference ->

            topic = TopicReference(date, title,"Créé par ${mUser?.displayName}", documentReference.id, "")

            mCollectionRef.add(topic)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            task.result.update("id", task.result.id)
                            topic.id = task.result.id
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
