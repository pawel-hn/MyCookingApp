package pawel.hn.mycookingapp.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import pawel.hn.mycookingapp.R

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        if (userAlreadyLoggedAndVerified()) {
            val intent = Intent(this, LoggedInActivity::class.java).apply {
                flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            startActivity(intent)

        }
        setContentView(R.layout.activity_main)
    }




    fun userAlreadyLoggedAndVerified(): Boolean {
        val firebaseAuth = FirebaseAuth.getInstance()
        return if (firebaseAuth.currentUser != null) {
            firebaseAuth.currentUser!!.isEmailVerified
        } else {
            false
        }
    }

}


