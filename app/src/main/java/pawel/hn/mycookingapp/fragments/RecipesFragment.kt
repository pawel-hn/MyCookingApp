package pawel.hn.mycookingapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import pawel.hn.mycookingapp.R
import pawel.hn.mycookingapp.activity.LoginActivity
import pawel.hn.mycookingapp.adapters.RecipesAdapter
import pawel.hn.mycookingapp.adapters.RecipesLoadStateAdapter
import pawel.hn.mycookingapp.databinding.FragmentRecipesBinding
import pawel.hn.mycookingapp.model.Recipe
import pawel.hn.mycookingapp.utils.SAVE_RECIPE_KEY
import pawel.hn.mycookingapp.utils.SAVE_RECIPE_KEY_BUNDLE
import pawel.hn.mycookingapp.utils.SAVE_RECIPE_RESULT
import pawel.hn.mycookingapp.utils.showToast
import pawel.hn.mycookingapp.viewmodels.RecipesViewModel
import javax.inject.Inject

@ExperimentalPagingApi
@InternalCoroutinesApi
@AndroidEntryPoint
class RecipesFragment : Fragment(R.layout.fragment_recipes), RecipesAdapter.RecipesOnClickListener {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    private val recipesViewModel: RecipesViewModel by viewModels()
    private lateinit var binding: FragmentRecipesBinding
    private lateinit var recipesAdapter: RecipesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentRecipesBinding.bind(view).apply {
            textViewRecipesHeader.text =
                getString(R.string.logged_as, firebaseAuth.currentUser?.email)
        }

        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav?.isVisible = true

        setAdapter()
        subscribeToObservers()
        subscribeToListeners()
        setUiStates()
        setHasOptionsMenu(true)
    }

    private fun subscribeToObservers() {
            recipesViewModel.recipesObservable.observe(viewLifecycleOwner) {
                recipesAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
    }

    private fun subscribeToListeners() {

        setFragmentResultListener(SAVE_RECIPE_KEY) { _, bundle ->
            when(bundle.getString(SAVE_RECIPE_KEY_BUNDLE)) {
                SAVE_RECIPE_RESULT -> showToast(requireContext(), "Recipe added to favourites")
            }
        }

        binding.apply {
            buttonRecipesRetry.setOnClickListener {
                recipesAdapter.retry()
            }

            recipesFab.setOnClickListener {
                val action = RecipesFragmentDirections
                    .actionRecipesFragmentToRecipesBottomSheet()
                findNavController().navigate(action)
            }
        }
    }

    private fun setAdapter() {
        recipesViewModel.getSavedRecipes()

        recipesAdapter = RecipesAdapter(this, recipesViewModel.list)
        val loadStateAdapter = RecipesLoadStateAdapter { recipesAdapter.retry() }

        binding.apply {
            recipesRecyclerView.adapter = recipesAdapter.withLoadStateFooter(loadStateAdapter)

        }
    }

    private fun setUiStates() {

        recipesAdapter.addLoadStateListener { loadState ->
            binding.apply {

                recipesProgressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recipesRecyclerView.isVisible =loadState.source.refresh is LoadState.NotLoading
                buttonRecipesRetry.isVisible =loadState.source.refresh is LoadState.Error
                textViewRecipesError.isVisible =loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    recipesAdapter.itemCount < 1
                ) {
                    recipesRecyclerView.isVisible = false
                    textViewEmpty.isVisible = true
                } else {
                    textViewEmpty.isVisible = false
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_log_out -> {
                logOutPressed()
            }
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    override fun onClickRecipe(url: String) {
        val action = RecipesFragmentDirections.actionRecipesFragmentToDetailsFragment(url)
        findNavController().navigate(action)
    }

    override fun onClickAdd(recipe: Recipe) {
        val action = RecipesFragmentDirections
            .actionRecipesFragmentToAddFavouriteDialog(recipe = recipe, label = "Save recipe")
        findNavController().navigate(action)
    }

    private fun logOutPressed() {
        AlertDialog.Builder(requireContext())
            .setTitle("Log Out")
            .setMessage("Do you want to log out?")
            .setPositiveButton("Yes") { _, _ ->
                recipesViewModel.logOut()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }
            .setNegativeButton("No"){ dialog, _ ->
                dialog.dismiss()
            }.show()
    }
}