package pawel.hn.mycookingapp.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import pawel.hn.mycookingapp.R
import pawel.hn.mycookingapp.adapters.SavedRecipesAdapter
import pawel.hn.mycookingapp.databinding.FragmentSavedRecipesBinding
import pawel.hn.mycookingapp.model.FavouriteRecipe
import pawel.hn.mycookingapp.utils.*
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
        binding.apply {
            recyclerViewSaved.adapter = savedRecipesAdapter
            ItemTouchHelper(swipe).attachToRecyclerView(recyclerViewSaved)
        }

        setFragmentResultListener(SAVE_RECIPE_KEY) { _, bundle ->
            when(bundle.getString(SAVE_RECIPE_KEY_BUNDLE)) {
                SAVE_RECIPE_RESULT -> showToast(requireContext(), "Recipe updated")
            }
        }

        readCachedRecipes()
    }

    val swipe = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder,
        ): Boolean = false

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val recipe = savedRecipesAdapter.currentList[viewHolder.bindingAdapterPosition]
            savedRecipesViewModel.deleteRecipe(recipe)
        }
    }

    private fun readCachedRecipes() {
        savedRecipesViewModel.cachedRecipes.observe(viewLifecycleOwner) { cachedRecipes ->
            if(cachedRecipes.isNotEmpty()) {
                savedRecipesAdapter.submitList(cachedRecipes)
            } else {
                requestDataFromFireStore()
            }
        }
    }

    private fun requestDataFromFireStore() {
        savedRecipesViewModel.getRecipesFromFireStore()

        savedRecipesViewModel.recipesResponse.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBarSaved.visibility = View.VISIBLE
                }
                is Resource.Error -> {
                    binding.progressBarSaved.visibility = View.GONE
                    binding.buttonSavedRetry.visibility = View.VISIBLE

                    binding.buttonSavedRetry.setOnClickListener {
                        savedRecipesViewModel.getRecipesFromFireStore()
                    }
                }
                is Resource.Success -> {
                    binding.progressBarSaved.visibility = View.GONE
                    savedRecipesAdapter.submitList(resource.data)
                }
            }
        }
    }

    override fun onClickEdit(favouriteRecipe: FavouriteRecipe) {
        val action = SavedRecipesFragmentDirections.actionFavouritesFragmentToAddFavouriteDialog(
            favouriteRecipe = favouriteRecipe, label = "Edit recipe"
        )
        findNavController().navigate(action)
    }

    override fun onClickRecipe(url: String) {
        val action = SavedRecipesFragmentDirections.actionFavouritesFragmentToDetailsFragment(url)
        findNavController().navigate(action)
    }
}