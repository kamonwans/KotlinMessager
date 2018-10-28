package com.example.kamonwan_s.kotlinmessager

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.kamonwan_s.kotlinmessager.model.User
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.user_row_new_message.*

class ChatLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        //val username = intent.getStringExtra(NewMessageActivity.USER_KEY)
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = user.username

        val adapter = GroupAdapter<ViewHolder>()
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        recyclerViewChatLogMessage.adapter = adapter

    }
}

class ChatFromItem : Item<ViewHolder>(){
    override fun getLayout(): Int {
return R.layout.chat_from_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

    }

}
class ChatToItem : Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

    }

}
