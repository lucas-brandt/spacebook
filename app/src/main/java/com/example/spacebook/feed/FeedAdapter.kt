package com.example.spacebook.feed

import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.spacebook.DateUtil
import com.example.spacebook.R
import com.example.spacebook.api.SpacebookApi
import com.example.spacebook.api.SpacebookApi.Feed

class FeedAdapter(private val list: List<Feed>) : RecyclerView.Adapter<FeedAdapter.ViewHolder>() {

    var onItemClick: ((Feed) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.feed_row_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        when(val currentFeedActivity = list[position]) {
            is SpacebookApi.ActivityPost -> {
                holder.date.text = DateUtil.formatInstant(currentFeedActivity.data.postedAt)
                holder.title.text = currentFeedActivity.data.title
                holder.newPost.visibility = VISIBLE
            }
            is SpacebookApi.ActivityComment -> {
                holder.date.text = DateUtil.formatInstant(currentFeedActivity.data.commentedAt)
                holder.title.text = "Commented on a post"
            }
            is SpacebookApi.ActivityHighRating -> {
                holder.title.text = "Passed 4 stars!"
                holder.date.text = DateUtil.formatInstant(currentFeedActivity.occurredAt)
            }
            is SpacebookApi.ActivityGithubMergedPr -> {
                val string = "Merged #${currentFeedActivity.data.pullRequestNumber}"
                holder.title.text = string
                //WHY DOESN'T THIS HAVE A BRANCH NAME?!
            }
            is SpacebookApi.ActivityGithubPr -> {
                val string = "Opened a new Pull Request #${currentFeedActivity.data.pullRequestNumber} for"
                holder.title.text = string
                holder.gitBranch.visibility = VISIBLE
                holder.gitBranch.text = currentFeedActivity.data.branch
            }
            is SpacebookApi.ActivityGithubPush -> {
                val string = "Pushed commits to"
                holder.title.text = string
                holder.gitBranch.visibility = VISIBLE
                holder.gitBranch.text = currentFeedActivity.data.branch
            }
            is SpacebookApi.ActivityGithubRepo -> {
                val string = "Created a new repository"
                holder.title.text = string
                holder.gitBranch.visibility = VISIBLE
                holder.gitBranch.text = currentFeedActivity.data.branch
            }
            SpacebookApi.Default -> return
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val date: TextView = itemView.findViewById(R.id.date)
        val newPost: TextView = itemView.findViewById(R.id.new_post)
        val title: TextView = itemView.findViewById(R.id.title)
        val gitBranch: TextView = itemView.findViewById(R.id.git_branch)

        init {
            itemView.setOnClickListener {
                onItemClick?.invoke(list[adapterPosition])
            }
        }
    }
}