package pawel.hn.mycookingapp.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import pawel.hn.mycookingapp.R
import pawel.hn.mycookingapp.databinding.FragmentAddFavouriteBinding
import pawel.hn.mycookingapp.model.FavouriteRecipe
import pawel.hn.mycookingapp.model.Recipe
import pawel.hn.mycookingapp.utils.FRAGMENT_RESULT_KEY
import pawel.hn.mycookingapp.utils.SAVE_RECIPE_KEY
import pawel.hn.mycookingapp.utils.SAVE_RECIPE_KEY_BUNDLE
import pawel.hn.mycookingapp.utils.SAVE_RECIPE_RESULT
import pawel.hn.mycookingapp.viewmodels.SaveRecipeViewModel


@AndroidEntryPoint
class SaveRecipeFragment : Fragment(R.layout.fragment_add_favourite) {

    lateinit var binding: FragmentAddFavouriteBinding
    private var recipe: Recipe? = null
    private var favouriteRecipe: FavouriteRecipe? =null
    private val saveRecipeViewModel: SaveRecipeViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recipe = SaveRecipeFragmentArgs.fromBundle(requireArguments()).recipe
        favouriteRecipe = SaveRecipeFragmentArgs.fromBundle(requireArguments()).favouriteRecipe

        binding = FragmentAddFavouriteBinding.bind(view)

        val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav?.isVisible = false

        setUi()
        setListeners()
    }

    private fun setUi(){
        binding.apply {
            textViewTitle.text = recipe?.title ?: favouriteRecipe?.title

            Glide.with(requireContext())
                .load(recipe?.image ?: favouriteRecipe?.image)
                .centerCrop()
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
                .into(imageViewRecipe)

            favouriteRecipe?.let {
                switchCooked.isChecked = it.cooked
                ratingBar.rating = it.rating.toFloat()
                editTextComments.setText(it.comment)
            }

            ratingBar.isEnabled = switchCooked.isChecked
        }
    }

    private fun setListeners() {
        binding.apply {

            switchCooked.setOnCheckedChangeListener { _, isChecked ->
                ratingBar.isEnabled = isChecked
            }

            buttonSave.setOnClickListener {
                val newFavouriteRecipe = createNewFavouriteRecipe(recipe, favouriteRecipe)
                saveRecipeViewModel.saveRecipe(newFavouriteRecipe)

                setFragmentResult(SAVE_RECIPE_KEY, bundleOf(SAVE_RECIPE_KEY_BUNDLE to SAVE_RECIPE_RESULT))

                findNavController().popBackStack()
            }

            buttonCancel.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun createNewFavouriteRecipe(
        recipe: Recipe?,
        favouriteRecipe: FavouriteRecipe?,
    ): FavouriteRecipe = if (recipe != null) {

        FavouriteRecipe(
            recipe.id,
            recipe.title,
            recipe.image,
            recipe.readyInMinutes.toString(),
            recipe.sourceUrl,
            recipe.vegetarian,
            binding.editTextComments.text.toString(),
            binding.switchCooked.isChecked,
            binding.ratingBar.rating.toString()
        )
    } else {
        FavouriteRecipe(
            favouriteRecipe!!.id,
            favouriteRecipe.title,
            favouriteRecipe.image,
            favouriteRecipe.readyInMinutes,
            favouriteRecipe.sourceUrl,
            favouriteRecipe.vegetarian,
            binding.editTextComments.text.toString(),
            binding.switchCooked.isChecked,
            binding.ratingBar.rating.toString()
        )
    }


}


