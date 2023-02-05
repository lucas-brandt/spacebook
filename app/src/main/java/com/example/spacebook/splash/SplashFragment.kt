package com.example.spacebook.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.spacebook.R
import com.example.spacebook.fromDependencies

class SplashFragment : Fragment() {

    private val viewModel: SplashViewModel by activityViewModels {
        fromDependencies { SplashViewModel(tokenManager) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_splash, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isLoggedIn.observe(viewLifecycleOwner) { isLoggedIn ->
            if (isLoggedIn) {
                findNavController().navigate(R.id.action_splash_to_main)
            } else {
                findNavController().navigate(R.id.splash_to_login)
            }
        }
    }
}
