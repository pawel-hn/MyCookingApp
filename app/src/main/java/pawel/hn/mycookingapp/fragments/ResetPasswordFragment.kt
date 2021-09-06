package pawel.hn.mycookingapp.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pawel.hn.mycookingapp.*
import pawel.hn.mycookingapp.databinding.FragmentResetPasswordBinding
import pawel.hn.mycookingapp.repository.LoginRegisterRepository
import pawel.hn.mycookingapp.utils.FRAGMENT_RESULT_KEY
import pawel.hn.mycookingapp.utils.REGISTER_RESULT_BUNDLE_KEY
import pawel.hn.mycookingapp.utils.RESET_RESULT
import pawel.hn.mycookingapp.utils.showToast
import pawel.hn.mycookingapp.viewmodels.LoginRegisterViewModel

@AndroidEntryPoint
class ResetPasswordFragment : Fragment(R.layout.fragment_reset_password) {


    private lateinit var binding: FragmentResetPasswordBinding
    private val viewModel: LoginRegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentResetPasswordBinding.bind(view)

        subscribeToEvents()
        subscribeToListeners()
    }

    private fun subscribeToEvents() {
        lifecycleScope.launch {
            viewModel.fireBaseFlow.collect { event ->
                if (event is LoginRegisterRepository.FirebaseEvents.ResetPasswordSuccess) {
                    hideProgressBar()
                    setFragmentResult(
                        FRAGMENT_RESULT_KEY,
                        bundleOf(REGISTER_RESULT_BUNDLE_KEY to RESET_RESULT))
                    findNavController().popBackStack()
                }
                else if (event is LoginRegisterRepository.FirebaseEvents.Error) {
                    hideProgressBar()
                    showToast(requireContext(), event.msg)
                }
                else if (event is LoginRegisterRepository.FirebaseEvents.Connecting) {
                    showProgressBar()
                }
            }
        }
    }

    private fun subscribeToListeners() {
        binding.apply {
            buttonCancel.setOnClickListener {
                findNavController().popBackStack()
            }

            buttonSentMail.setOnClickListener {
                val email = editTextLoginUser.text.toString().trim()
                viewModel.resetPassword(email)
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