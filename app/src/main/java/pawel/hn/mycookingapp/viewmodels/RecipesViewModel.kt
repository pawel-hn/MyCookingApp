package pawel.hn.mycookingapp.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pawel.hn.mycookingapp.data.FirestoreData
import pawel.hn.mycookingapp.model.MealAndDietType
import pawel.hn.mycookingapp.repository.DataStoreRepository
import pawel.hn.mycookingapp.repository.FirestoreRepository
import pawel.hn.mycookingapp.repository.RecipesRepository
import pawel.hn.mycookingapp.utils.*
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
@InternalCoroutinesApi
class RecipesViewModel @Inject constructor(
    recipesRepository: RecipesRepository,
    private val firestoreRepository: FirestoreRepository,
   private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {

    private val queries = HashMap<String, String>()
    val mealAndDietTypeLiveData = dataStoreRepository.readMealAndDietType
    val recipesLiveData = recipesRepository.getRecipes(queries).cachedIn(viewModelScope)


    val savedRecipes = firestoreRepository.getRecipes()

    fun getQueries(mealAndDietType: MealAndDietType?) {
        var mealType = DEFAULT_MEAL_TYPE
        var dietType = DEFAULT_DIET_TYPE
        if(mealAndDietType == null) {

            viewModelScope.launch(Dispatchers.Main) {
                    mealAndDietTypeLiveData.collect {
                        mealType = it.mealType
                        dietType = it.dietType
                    }
            }
        } else {
             mealType = mealAndDietType.mealType
             dietType = mealAndDietType.dietType
        }
        Timber.d("queries: $mealType PHN")
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"
        queries[QUERY_MEAL] = mealType
        queries[QUERY_DIET] = dietType
    }

    fun saveRecipesTypes(
        mealAndDietType: MealAndDietType
    ) {
        viewModelScope.launch {
            dataStoreRepository.saveMealAndDietType(mealAndDietType)
        }
    }


     fun logOut() {
        viewModelScope.launch {
            firestoreRepository.logOut()
        }
    }

}