package pawel.hn.mycookingapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import pawel.hn.mycookingapp.R
import pawel.hn.mycookingapp.databinding.ActivityLoginBinding

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (userAlreadyLoggedAndVerified()) {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            }
            startActivity(intent)
        }

        val binding = ActivityLoginBinding.inflate(LayoutInflater.from(this),
            window.decorView.rootView as ViewGroup, false)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_login_activity) as NavHostFragment

        val appBrConfiguration = AppBarConfiguration.Builder(
            R.id.loginFragment,
            R.id.registrationFragment,
            R.id.resetPasswordFragment
        ).build()

        val navController = navHostFragment.navController

        setupActionBarWithNavController(navController, appBrConfiguration)
    }

    private fun userAlreadyLoggedAndVerified(): Boolean {
        val firebaseAuth = FirebaseAuth.getInstance()
        return if (firebaseAuth.currentUser != null) {
            firebaseAuth.currentUser!!.isEmailVerified
        } else {
            false
        }
    }
}


