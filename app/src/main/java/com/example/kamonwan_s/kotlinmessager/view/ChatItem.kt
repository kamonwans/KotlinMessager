package com.example.kamonwan_s.kotlinmessager.view

import com.example.kamonwan_s.kotlinmessager.R
import com.example.kamonwan_s.kotlinmessager.model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatFromItem(val text :String,val user: User) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tvMessageOther.text = text
        val uri = user.profileImageUrl
        val targetImageView = viewHolder.itemView.imageUserOther
        Picasso.get().load(uri).into(targetImageView)
    }

}

class ChatToItem(val text: String,val user: User) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tvMyMessage.text = text

        //load our user image into the star
        val uri = user.profileImageUrl
        val targetImageView = viewHolder.itemView.imageMyUser
        Picasso.get().load(uri).into(targetImageView)
    }

}
