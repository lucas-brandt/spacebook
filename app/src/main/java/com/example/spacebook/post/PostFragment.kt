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
    }

    private fun onStateChanged(state: PostViewModel.State) {
        when (state) {
            PostViewModel.State.Retrieving -> return
            is PostViewModel.State.Error -> {
                //do stuff with error
            }
            is PostViewModel.State.Success -> {

            }
        }
    }
}
