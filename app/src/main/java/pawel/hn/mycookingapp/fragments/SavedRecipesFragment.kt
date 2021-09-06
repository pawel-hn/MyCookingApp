package pawel.hn.mycookingapp.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import pawel.hn.mycookingapp.R
import pawel.hn.mycookingapp.adapters.SavedRecipesAdapter
import pawel.hn.mycookingapp.databinding.FragmentSavedRecipesBinding
import pawel.hn.mycookingapp.model.FavouriteRecipe
import pawel.hn.mycookingapp.utils.Resource
import pawel.hn.mycookingapp.viewmodels.SaveRecipeViewModel

@AndroidEntryPoint
class SavedRecipesFragment : Fragment(R.layout.fragment_saved_recipes), SavedRecipesAdapter.SavedRecipesListener {

    lateinit var savedRecipesAdapter: SavedRecipesAdapter
    lateinit var binding: FragmentSavedRecipesBinding
    private val savedRecipesViewModel: SaveRecipeViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav?.isVisible = true

        savedRecipesAdapter = SavedRecipesAdapter(this)
        binding = FragmentSavedRecipesBinding.bind(view)
        binding.recyclerViewSaved.adapter = savedRecipesAdapter


        savedRecipesViewModel.getSavedRecipes()

        savedRecipesViewModel.savedRecipesLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBarSaved.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    binding.progressBarSaved.visibility = View.GONE
                    binding.buttonSavedRetry.visibility = View.VISIBLE

                    binding.buttonSavedRetry.setOnClickListener {
                        savedRecipesViewModel.getSavedRecipes()
                    }
                }
                is Resource.Success -> {
                    binding.progressBarSaved.visibility = View.GONE
                    savedRecipesAdapter.submitList(resource.data)
                }
            }
        }
    }


    override fun onClickRecipe(favouriteRecipe: FavouriteRecipe) {
        val action = SavedRecipesFragmentDirections.actionFavouritesFragmentToAddFavouriteDialog(
            favouriteRecipe = favouriteRecipe
        )
        findNavController().navigate(action)
    }
}