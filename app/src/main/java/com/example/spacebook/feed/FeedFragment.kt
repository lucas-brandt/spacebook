package com.example.spacebook.feed

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.spacebook.R
import com.example.spacebook.api.SpacebookApi
import com.example.spacebook.databinding.FragmentFeedBinding
import com.example.spacebook.fromDependencies
import com.example.spacebook.post.PostViewModel


class FeedFragment : Fragment(), Toolbar.OnMenuItemClickListener {

    private val viewModel: FeedViewModel by activityViewModels {
        fromDependencies { FeedViewModel(api) }
    }

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner, this::onStateChanged)
        viewModel.getFeed(13) //harcode user 13 because maks/max/maksim told me to
    }

    private fun onStateChanged(state: FeedViewModel.State) {
        when (state) {
            FeedViewModel.State.Retrieving -> return
            is FeedViewModel.State.Error -> {
                //do stuff with error
            }
            is FeedViewModel.State.Success -> {
                val feedAdapter = FeedAdapter(state.result)
                binding.recyclerview.adapter = feedAdapter
                feedAdapter.onItemClick = {
                    when(it) {
                        is SpacebookApi.ActivityComment -> { findNavController().navigate(R.id.action_feed_to_post) }
                        is SpacebookApi.ActivityHighRating -> {}
                        is SpacebookApi.ActivityPost -> {}
                        is SpacebookApi.ActivityGithubMergedPr -> {}
                        is SpacebookApi.ActivityGithubPr -> {}
                        is SpacebookApi.ActivityGithubPush -> {}
                        is SpacebookApi.ActivityGithubRepo -> {}
                        SpacebookApi.Default -> {}
                    }
                }
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.logout -> {
                // TODO: log out
            }
            else -> return false
        }
        return true
    }
}
