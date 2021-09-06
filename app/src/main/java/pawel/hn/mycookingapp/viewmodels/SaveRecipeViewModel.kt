package pawel.hn.mycookingapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pawel.hn.mycookingapp.model.FavouriteRecipe
import pawel.hn.mycookingapp.repository.FirestoreRepository
import javax.inject.Inject

@HiltViewModel
class SaveRecipeViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository) : ViewModel() {

    val savedRecipesLiveData = firestoreRepository.savedRecipesLiveData

    fun  getSavedRecipes() = firestoreRepository.getRecipes()


//    fun getSavedRecipes() {
//        viewModelScope.launch {
//            firestoreRepository.getSavedRecipes()
//        }
//    }

        fun saveRecipe(favouriteRecipe: FavouriteRecipe) {
            viewModelScope.launch {
                firestoreRepository.
                saveRecipe(favouriteRecipe)
            }
        }




}