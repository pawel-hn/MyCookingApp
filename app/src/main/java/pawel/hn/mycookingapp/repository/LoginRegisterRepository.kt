package pawel.hn.mycookingapp.repository

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginRegisterRepository @Inject constructor() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val coroutine = CoroutineScope(Dispatchers.IO)
    private val firebaseEventsChannel = Channel<FirebaseEvents>()
    val firebaseEventsFlow = firebaseEventsChannel.receiveAsFlow()

    private fun connectingWithFireBase() {
        coroutine.launch {
            firebaseEventsChannel.send(FirebaseEvents.Connecting)
        }
    }

    fun registerUser(email: String, password: String) {
        connectingWithFireBase()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                sendMailVerification()
            }
            .addOnFailureListener { exception ->

                coroutine.launch {
                    firebaseEventsChannel.send(
                        FirebaseEvents.Error(exception.message!!)
                    )
                }
            }
    }

    private fun sendMailVerification() {
        connectingWithFireBase()
        firebaseAuth.currentUser?.sendEmailVerification()
            ?.addOnSuccessListener {
                coroutine.launch {
                    firebaseEventsChannel.send(FirebaseEvents.VerificationMailSend)
                }
            }
            ?.addOnFailureListener { exception ->
                coroutine.launch {
                    firebaseEventsChannel.send(
                        FirebaseEvents.Error(exception.message!!)
                    )
                }
            }
    }

    fun logInUser(email: String, password: String) {
        connectingWithFireBase()
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                coroutine.launch {
                    firebaseEventsChannel.send(FirebaseEvents.LoginSuccess)
                }
            }
            .addOnFailureListener { exception ->
                coroutine.launch {
                    firebaseEventsChannel.send(FirebaseEvents.Error(exception.message!!))
                }
            }
    }

    fun resetPassword(email: String) {
        connectingWithFireBase()
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                coroutine.launch {
                    firebaseEventsChannel.send(FirebaseEvents.ResetPasswordSuccess)
                }
            }
            .addOnFailureListener { exception ->
                coroutine.launch {
                    firebaseEventsChannel.send(FirebaseEvents.Error(exception.message!!))
                }
            }
    }

    sealed class FirebaseEvents {
        object Connecting : FirebaseEvents()
        object VerificationMailSend : FirebaseEvents()
        object LoginSuccess : FirebaseEvents()
        object ResetPasswordSuccess : FirebaseEvents()
        data class Error(val msg: String) : FirebaseEvents()
    }

}