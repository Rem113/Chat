package com.rem113.chat

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        private val TOPICS_KEY = "topics"
        private val DATE = "date"
        private val TITLE = "title"
        private val LAST = "last"
        private val MESSAGES = "messages"
    }

    // Firestore
    private val mDatabase = FirebaseFirestore.getInstance()
    private val mRef = mDatabase.collection(Companion.TOPICS_KEY).orderBy(Companion.DATE, Query.Direction.DESCENDING)
    private val mTopicsList = mutableListOf<TopicReference>()

    // FirebaseAuthUI
    private val mAuth = FirebaseAuth.getInstance()
    private var mUser = mAuth.currentUser

    private fun getTopics(topicList : MutableList<TopicReference>) {
        mRef.get().addOnCompleteListener { task ->

            topicList.clear()

            if (task.isSuccessful) {
                task.result.documents.mapTo(topicList) { it -> TopicReference(
                        it[Companion.DATE] as Date,
                        it[Companion.TITLE] as String,
                        it[Companion.LAST] as String,
                        it[Companion.MESSAGES] as String)
                }

                recyclerView.adapter.notifyDataSetChanged()
            }

            swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (mUser == null) {
            connect()
        }

        // Set up the swipe to refresh
        swipeRefreshLayout.setOnRefreshListener {
            getTopics(mTopicsList)
        }

        // Sets up the RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TopicReferenceAdapter(mTopicsList, this)
    }

    override fun onResume() {
        super.onResume()

        if (mUser != null) {
            getTopics(mTopicsList)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean {
        when (item.itemId) {
            R.id.add_topic -> {
                startActivity(Intent(this, AddTopic::class.java))
                return true
            }

            R.id.disconnect -> {
                mAuth.signOut()
                connect()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun connect() {
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }
}
