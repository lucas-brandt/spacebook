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
import com.example.spacebook.feed.FeedAdapter
import com.example.spacebook.fromDependencies


class PostFragment : Fragment(), Toolbar.OnMenuItemClickListener {

    private val viewModel: PostViewModel by activityViewModels {
        fromDependencies { PostViewModel(api) }
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
        viewModel.getFeed(5) //harcode user 5 because maks/max/maksim told me to
    }

    private fun onStateChanged(state: PostViewModel.State) {
        when (state) {
            PostViewModel.State.Retrieving -> return
            is PostViewModel.State.Error -> {
                //do stuff with error
            }
            is PostViewModel.State.Success -> {
                val feedAdapter = FeedAdapter(state.result)
                binding.recyclerview.adapter = feedAdapter
                feedAdapter.onItemClick = {
                    when(it) {
                        is SpacebookApi.ActivityComment -> { }
                        is SpacebookApi.ActivityHighRating -> {}
                        is SpacebookApi.ActivityPost -> TODO()
                        is SpacebookApi.ActivityGithubMergedPr -> TODO()
                        is SpacebookApi.ActivityGithubPr -> TODO()
                        is SpacebookApi.ActivityGithubPush -> TODO()
                        is SpacebookApi.ActivityGithubRepo -> TODO()
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
