package com.example.spacebook.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.spacebook.R
import com.example.spacebook.api.SpacebookApi
import com.example.spacebook.api.SpacebookApi.Post
import com.example.spacebook.databinding.FragmentFeedBinding
import com.example.spacebook.databinding.FragmentPostBinding
import com.example.spacebook.feed.FeedAdapter
import com.example.spacebook.fromDependencies


class PostFragment : Fragment() {

    private val viewModel: PostViewModel by activityViewModels {
        fromDependencies { PostViewModel(api) }
    }

    private var _binding: FragmentPostBinding? = null
    private val binding get() = _binding!!

    lateinit var currentPost: Post

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.postState.observe(viewLifecycleOwner, this::onPostStateChanged)
        viewModel.commentState.observe(viewLifecycleOwner, this::onCommentStateChanged)
        viewModel.deleteState.observe(viewLifecycleOwner, this::onDeleteStateChanged)

        if (viewModel.isPost) {
            viewModel.getComments(viewModel.getPostId())

            val post = (viewModel.feed.value as SpacebookApi.ActivityPost).data
            binding.title.text = post.title
            binding.author.text = "by ${post.author.name}"
            binding.rating.text = "Rating: ${post.author.rating}"
            binding.date.text = post.postedAt.toString()
            binding.body.text = post.body
            currentPost = post
        } else {
            val id = viewModel.getPostId()
            viewModel.getPost(id)
            viewModel.getComments(id)
        }
    }

    private fun onPostStateChanged(state: PostViewModel.PostState) {
        when (state) {
            PostViewModel.PostState.Retrieving -> return
            is PostViewModel.PostState.Error -> {
                //do stuff with error
            }
            is PostViewModel.PostState.Success -> {
                binding.title.text = state.result.title
                binding.author.text = "by ${state.result.author.name}"
                binding.rating.text = "Rating: ${state.result.author.rating}"
                binding.date.text = state.result.postedAt.toString()
                binding.body.text = state.result.body
                currentPost = state.result
            }
        }
    }

    private fun onCommentStateChanged(state: PostViewModel.CommentState) {
        when (state) {
            PostViewModel.CommentState.Retrieving -> return
            is PostViewModel.CommentState.Error -> {
                //do stuff with error
            }
            is PostViewModel.CommentState.Success -> {
                val postAdapter = PostAdapter(state.result)
                binding.recyclerview.adapter = postAdapter

                postAdapter.onItemClick = {
                    if (it.userId == 13 || currentPost.author.id == 13) {
                       viewModel.deleteComment(it.id)
                    } else {
                        Toast.makeText(context,"Can't delete someone else's comment", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun onDeleteStateChanged(state: PostViewModel.DeleteState) {
        when (state) {
            PostViewModel.DeleteState.Retrieving -> return
            is PostViewModel.DeleteState.Error -> {
                Toast.makeText(context,"Comment failed to delete: ${state.e}", Toast.LENGTH_SHORT).show()
            }
            is PostViewModel.DeleteState.Success -> {
                Toast.makeText(context,"Comment was successfully deleted", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
