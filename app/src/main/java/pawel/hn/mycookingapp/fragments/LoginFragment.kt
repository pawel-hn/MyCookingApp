package pawel.hn.mycookingapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pawel.hn.mycookingapp.*
import pawel.hn.mycookingapp.activity.MainActivity
import pawel.hn.mycookingapp.databinding.FragmentLoginBinding
import pawel.hn.mycookingapp.repository.LoginRegisterRepository
import pawel.hn.mycookingapp.utils.*
import pawel.hn.mycookingapp.viewmodels.LoginRegisterViewModel

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginRegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setFragmentResultListener(LOGIN_FRAGMENT_RESULT_KEY) { _, bundle ->
            when (bundle.getString(REGISTER_RESULT_BUNDLE_KEY)) {
                REGISTER_RESULT -> showToast(requireContext(), "Verify e-mail to login")
                RESET_RESULT -> showToast(requireContext(), "Mail sent, check inbox.")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoginBinding.bind(view)
        subscribeToEvents()
        subscribeToListeners(binding)
    }

    private fun subscribeToEvents() {
        lifecycleScope.launch {
            viewModel.fireBaseFlow.collect { event ->
                when (event) {
                    is LoginRegisterRepository.FirebaseEvents.LoginSuccess -> {
                        hideProgressBar()
                        val intent = Intent(requireContext(), MainActivity::class.java).apply {
                            flags =
                                (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        }
                        startActivity(intent)
                    }
                    is LoginRegisterRepository.FirebaseEvents.Error -> {
                        hideProgressBar()
                        showToast(requireContext(), event.msg)
                    }
                    is LoginRegisterRepository.FirebaseEvents.Connecting -> {
                        showProgressBar()
                    }
                    else -> { }
                }
            }
        }
    }

    private fun subscribeToListeners(binding: FragmentLoginBinding) {
        binding.apply {

            buttonLogin.setOnClickListener {
                val email = editTextLoginUser.text.toString().trim()
                val password = editTextLoginPassword.text.toString().trim()

                if (email.isNotEmpty()) {
                    viewModel.loginUser(email, password)
                }
            }

            textViewCreateAccount.setOnClickListener {
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()
                )
            }

            textViewResetPassword.setOnClickListener {
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToResetPasswordFragment()
                )
            }
        }
    }

    private fun showProgressBar() {
        binding.progressBar.isVisible = true
    }

    private fun hideProgressBar() {
        binding.progressBar.isVisible = false
    }
}