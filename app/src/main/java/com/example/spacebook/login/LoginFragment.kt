package com.example.spacebook.login

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.spacebook.R
import com.example.spacebook.databinding.FragmentLoginBinding
import com.example.spacebook.fromDependencies
import com.example.spacebook.login.LoginViewModel.State
import com.example.spacebook.login.LoginViewModel.State.*
import com.example.spacebook.login.LoginViewModel.State.Error.Reason.*

class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by activityViewModels {
        fromDependencies { LoginViewModel(api) }
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefs: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, this::onStateChanged)
        binding.LoginSubmit.setOnClickListener {
            val email = binding.LoginEmailField.editText!!.text.toString()
            val password = binding.LoginPasswordField.editText!!.text.toString()
            viewModel.login(email, password)
            dismissSoftKeyboard()
        }
        prefs = requireContext().applicationContext.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
    }

    private fun onStateChanged(state: State) {
        binding.LoginEmailField.error = null
        binding.LoginPasswordField.error = null
        binding.LoginSubmit.visibility = View.VISIBLE
        binding.LoginLoading.visibility = View.INVISIBLE
        binding.LoginErrorMessage.visibility = View.INVISIBLE
        when (state) {
            FillingOutForm -> return
            LoggingIn -> {
                binding.LoginSubmit.visibility = View.INVISIBLE
                binding.LoginLoading.visibility = View.VISIBLE
            }
            is Error -> when (state.reason) {
                INVALID_EMAIL -> binding.LoginEmailField.error = getString(R.string.invalid_email)
                INVALID_PASSWORD -> binding.LoginPasswordField.error =
                    getString(R.string.invalid_password)
                INCORRECT_PASSWORD -> binding.LoginPasswordField.error =
                    getString(R.string.incorrect_password)
                NETWORK_ERROR -> binding.LoginErrorMessage.visibility = View.VISIBLE
            }
            is Success -> {
                prefs.edit().putInt("userId", state.result.id).apply()
                findNavController().navigate(R.id.login_to_main)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun dismissSoftKeyboard() {
        (requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(requireView().windowToken, 0)
    }
}
