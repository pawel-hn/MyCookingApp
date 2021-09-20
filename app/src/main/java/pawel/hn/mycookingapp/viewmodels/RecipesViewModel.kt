package pawel.hn.mycookingapp.viewmodels

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import pawel.hn.mycookingapp.model.FavouriteRecipe
import pawel.hn.mycookingapp.model.MealAndDietType
import pawel.hn.mycookingapp.repository.DataStoreRepository
import pawel.hn.mycookingapp.repository.RecipesRepository
import pawel.hn.mycookingapp.repository.SavedRecipesRepository
import pawel.hn.mycookingapp.utils.*
import javax.inject.Inject
import kotlin.collections.set

@ExperimentalPagingApi
@HiltViewModel
@InternalCoroutinesApi
class RecipesViewModel @Inject constructor(
    private val recipesRepository: RecipesRepository,
    private val savedRecipesRepository: SavedRecipesRepository,
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {

    val mealAndDietTypeLiveData = dataStoreRepository.readMealAndDietType.asLiveData()

    val recipesObservable = Transformations.switchMap(mealAndDietTypeLiveData) {
        val queries = HashMap<String, String>()
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        queries[QUERY_MEAL] = it.mealType
        queries[QUERY_DIET] = it.dietType

        recipesRepository.getRecipesFromNetwork(queries).cachedIn(viewModelScope)
    }

    var list = emptyList<FavouriteRecipe>()

    fun getSavedRecipes() {
        list = savedRecipesRepository.getSavedRecipesList()
        if (list.isEmpty()) {
            getSavedRecipesFromFirestore()
            list = savedRecipesRepository.getSavedRecipesList()
        }
    }

    fun saveRecipesTypes(
        mealAndDietType: MealAndDietType,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(mealAndDietType)
        }
    }

    private fun getSavedRecipesFromFirestore() {
        savedRecipesRepository.getRecipesFromFirestore()
    }

    fun logOut() {
        viewModelScope.launch {
            savedRecipesRepository.logOut()
        }
    }
}