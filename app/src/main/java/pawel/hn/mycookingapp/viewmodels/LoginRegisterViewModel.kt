package pawel.hn.mycookingapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pawel.hn.mycookingapp.utils.isValidEmail
import pawel.hn.mycookingapp.repository.LoginRegisterRepository
import javax.inject.Inject

@HiltViewModel
class LoginRegisterViewModel @Inject constructor(private val repository: LoginRegisterRepository) : ViewModel() {

    val fireBaseFlow = repository.firebaseEventsFlow
    var toastMessage = ""


    fun registerUser(email: String, password: String) = viewModelScope.launch {
        repository.registerUser(email, password)
    }

    fun loginUser(email: String, password: String) = viewModelScope.launch {
        repository.logInUser(email, password)
    }

    fun resetPassword(email: String) = viewModelScope.launch {
        repository.resetPassword(email)
    }


    fun invalidDataInput(email: String, repeatPassword: String, password: String): Boolean {
        return when {
            email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty() -> {
                toastMessage = "Fill all fields."
                true
            }
            !email.isValidEmail() -> {
                toastMessage = "Invalid email."
                true
            }
            password.length < 4 -> {
                toastMessage = "Password must be at least 4 signs."
                true
            }
            password != repeatPassword -> {
                toastMessage = "Confirmed password does not match."
                true
            }
            else -> false
        }
    }

}