package com.example.kamonwan_s.kotlinmessager.messages

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.kamonwan_s.kotlinmessager.R
import com.example.kamonwan_s.kotlinmessager.model.ChatMessage
import com.example.kamonwan_s.kotlinmessager.model.User
import com.example.kamonwan_s.kotlinmessager.view.ChatFromItem
import com.example.kamonwan_s.kotlinmessager.view.ChatToItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {

    companion object {
        val TAG = "ChatLog"
    }
    val adapter = GroupAdapter<ViewHolder>()
    var toUser:User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        recyclerViewChatLogMessage.adapter = adapter
        toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)

        supportActionBar?.title = toUser?.username

        listenForMessage()
        btnSend.setOnClickListener {
            Log.d(TAG,"Attempt to send message")
            performSendMessage()
        }

    }

    private fun listenForMessage() {
        val fromId =FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        ref.addChildEventListener(object :ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
               val chatMessage =  p0.getValue(ChatMessage::class.java)
                if (chatMessage != null){
                    Log.d(TAG,chatMessage?.text)
                    if (chatMessage.toId == FirebaseAuth.getInstance().uid){
                        val currentUser = LatestMessagesActivity.currentUser
                                ?: return
                        adapter.add(ChatFromItem(chatMessage.text, currentUser!!))
                    }else{
                        adapter.add(ChatToItem(chatMessage.text, toUser!!))
                    }
                }
                recyclerViewChatLogMessage.scrollToPosition(adapter.itemCount - 1)
            }
            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })
    }

    private fun performSendMessage() {
        val text = editMessage.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = user.uid

        if (fromId == null)return
        val referent = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
        val toReferent = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val chatMessage = ChatMessage(referent.key!!,text,fromId!!,toId,System.currentTimeMillis()/1000)
        referent.setValue(chatMessage)
                .addOnSuccessListener {
                    Log.d(TAG,"Saved our chat message : ${referent.key}")
                    editMessage.text.clear()
                    recyclerViewChatLogMessage.scrollToPosition(adapter.itemCount - 1)
                }
        toReferent.setValue(chatMessage)

        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
        latestMessageRef.setValue(chatMessage)

        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
        latestMessageToRef.setValue(chatMessage)

    }
}

