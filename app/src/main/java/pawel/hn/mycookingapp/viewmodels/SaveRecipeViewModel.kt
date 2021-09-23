package pawel.hn.mycookingapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pawel.hn.mycookingapp.model.FavouriteRecipe
import pawel.hn.mycookingapp.repository.SavedRecipesRepository
import javax.inject.Inject

@HiltViewModel
class SaveRecipeViewModel @Inject constructor(
    private val savedRecipesRepository: SavedRecipesRepository,
) : ViewModel() {

    val cachedRecipes = savedRecipesRepository.getSavedRecipesFromLocal()
    val recipesResponse = savedRecipesRepository.savedRecipesLiveData

    fun getRecipesFromFireStore() {
        savedRecipesRepository.requestFavouriteRecipesFromFirestore()
    }

    fun saveRecipe(favouriteRecipe: FavouriteRecipe) {
        viewModelScope.launch(Dispatchers.IO) {
            savedRecipesRepository.saveRecipe(favouriteRecipe)
        }
    }

    fun deleteRecipe(favouriteRecipe: FavouriteRecipe) {
        viewModelScope.launch(Dispatchers.IO) {
            savedRecipesRepository.deleteSavedRecipe(favouriteRecipe)
        }
    }
}