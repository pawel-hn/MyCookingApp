package pawel.hn.mycookingapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pawel.hn.mycookingapp.R
import pawel.hn.mycookingapp.databinding.ActivityLoggedInBinding
import pawel.hn.mycookingapp.model.MealAndDietType
import pawel.hn.mycookingapp.repository.DataStoreRepository
import pawel.hn.mycookingapp.utils.DEFAULT_DIET_TYPE
import pawel.hn.mycookingapp.utils.DEFAULT_MEAL_TYPE
import javax.inject.Inject

@AndroidEntryPoint
class LoggedInActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController


    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    @Inject
    lateinit var dataStoreRepository: DataStoreRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityLoggedInBinding.inflate(LayoutInflater.from(this),
            window.decorView.rootView as ViewGroup, false)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_logged_in_activity) as NavHostFragment

        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration.Builder(
            R.id.recipesFragment,
            R.id.favouritesFragment
        ).build()

        binding.bottomNav.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp()
    }





}