package com.example.spacebook.post

import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.spacebook.R
import com.example.spacebook.api.SpacebookApi
import com.example.spacebook.api.SpacebookApi.Comment
import com.example.spacebook.api.SpacebookApi.Feed

class PostAdapter(private val list: List<Comment>) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    var onItemClick: ((Comment) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.comment_row_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentComment = list[position]

        holder.comment.text = currentComment.message
        holder.date.text = currentComment.commentedAt.toString()
        currentComment.postId
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val comment: TextView = itemView.findViewById(R.id.comment)
        val date: TextView = itemView.findViewById(R.id.date)
        val name: TextView = itemView.findViewById(R.id.name)

        init {
            itemView.findViewById<ImageView>(R.id.trash).setOnClickListener {
                onItemClick?.invoke(list[adapterPosition])
            }
        }
    }
}