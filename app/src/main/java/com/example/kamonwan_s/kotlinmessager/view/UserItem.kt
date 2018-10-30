package com.example.kamonwan_s.kotlinmessager.view

import com.example.kamonwan_s.kotlinmessager.R
import com.example.kamonwan_s.kotlinmessager.model.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class UserItem(val user: User) : Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.tvUsername.text = user.username

        Picasso.get().load(user
                .profileImageUrl).into(viewHolder.itemView.imageUser)
    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}