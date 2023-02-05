package com.example.spacebook.feed

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
import com.example.spacebook.databinding.FragmentFeedBinding
import com.example.spacebook.databinding.FragmentLoginBinding
import com.example.spacebook.fromDependencies
import com.example.spacebook.login.LoginViewModel


class FeedFragment : Fragment(), Toolbar.OnMenuItemClickListener {

    private val viewModel: FeedViewModel by activityViewModels {
        fromDependencies { FeedViewModel(api) }
    }

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private lateinit var prefs: SharedPreferences

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
    }

    private fun onStateChanged(state: FeedViewModel.State) {
        when (state) {
            FeedViewModel.State.Retrieving -> return
            is FeedViewModel.State.Error -> {
                //do stuff with error
            }
            is FeedViewModel.State.Success -> {
                //put the list into a recycler view
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
