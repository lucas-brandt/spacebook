package com.example.spacebook.feed

import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spacebook.R
import com.example.spacebook.api.SpacebookApi
import com.example.spacebook.api.SpacebookApi.Feed
import com.example.spacebook.api.SpacebookApi.Type

class FeedAdapter(private val list: List<Feed>) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_row_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentFeedActivity = list[position]
        when(currentFeedActivity) {
            is SpacebookApi.ActivityPost -> {
                holder.date.text = currentFeedActivity.data.postedAt.toString()
                holder.title.text = currentFeedActivity.data.title
                holder.newPost.visibility = VISIBLE
                holder.comments.visibility = VISIBLE
            }
            is SpacebookApi.ActivityComment -> {
                holder.date.text = currentFeedActivity.data.commentedAt.toString()
                holder.title.text = currentFeedActivity.data.message
            }
            is SpacebookApi.ActivityGithub -> {
                //gross another when statement
            }
            is SpacebookApi.ActivityHighRating -> {
                holder.title.text = "Passed 4 stars!"
                holder.date.text = currentFeedActivity.occurredAt.toString()
            }
            SpacebookApi.Default -> return
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return list.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val date: TextView = itemView.findViewById(R.id.date)
        val newPost: TextView = itemView.findViewById(R.id.new_post)
        val title: TextView = itemView.findViewById(R.id.title)
        val comments: TextView = itemView.findViewById(R.id.comments)
        val gitBranch: TextView = itemView.findViewById(R.id.git_branch)
    }
}