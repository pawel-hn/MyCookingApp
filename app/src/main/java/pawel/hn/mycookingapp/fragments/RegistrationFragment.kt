package pawel.hn.mycookingapp.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import pawel.hn.mycookingapp.*
import pawel.hn.mycookingapp.databinding.FragmentRegistrationBinding
import pawel.hn.mycookingapp.repository.LoginRegisterRepository
import pawel.hn.mycookingapp.utils.FRAGMENT_RESULT_KEY
import pawel.hn.mycookingapp.utils.REGISTER_RESULT
import pawel.hn.mycookingapp.utils.REGISTER_RESULT_BUNDLE_KEY
import pawel.hn.mycookingapp.utils.showToast
import pawel.hn.mycookingapp.viewmodels.LoginRegisterViewModel


@InternalCoroutinesApi
@AndroidEntryPoint
class RegistrationFragment : Fragment(R.layout.fragment_registration) {

    private val viewModel: LoginRegisterViewModel by viewModels()
    private lateinit var binding: FragmentRegistrationBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegistrationBinding.bind(view)

        subscribeToListeners()
        subscribeToEvent()
    }


    private fun subscribeToListeners() {
        binding.apply {

            buttonRegister.setOnClickListener {
                val email = editTextRegisterEmail.text.toString().trim()
                val password = editTextRegisterPassword.text.toString().trim()
                val repeatPassword = editTextRegisterRepeatPassword.text.toString().trim()

                if (viewModel.invalidDataInput(email, repeatPassword, password)) {
                    showToast(requireContext(), viewModel.toastMessage)
                    return@setOnClickListener
                }

                viewModel.registerUser(email, password)

            }

            textViewLogin.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun subscribeToEvent() {
        lifecycleScope.launch {
            viewModel.fireBaseFlow.collect { event ->
                if (event is LoginRegisterRepository.FirebaseEvents.Error) {
                    hideProgressBar()
                    showToast(requireContext(), event.msg)
                } else if (event is LoginRegisterRepository.FirebaseEvents.VerificationMailSend) {
                    hideProgressBar()
                    setFragmentResult(
                        FRAGMENT_RESULT_KEY,
                        bundleOf(REGISTER_RESULT_BUNDLE_KEY to REGISTER_RESULT))
                    findNavController().popBackStack()
                } else if (event is LoginRegisterRepository.FirebaseEvents.Connecting) {
                    showProgressBar()
                }
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