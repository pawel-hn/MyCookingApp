package pawel.hn.mycookingapp.repository

import androidx.room.withTransaction
import pawel.hn.mycookingapp.data.FirestoreData
import pawel.hn.mycookingapp.database.RecipesDatabase
import pawel.hn.mycookingapp.model.FavouriteRecipe
import pawel.hn.mycookingapp.utils.networkBoundResource
import javax.inject.Inject

class FirestoreRepository @Inject constructor(
    private val firestoreData: FirestoreData,
    private val db: RecipesDatabase
) {

    private val savedRecipesDao = db.savedRecipesDao
    val savedRecipesLiveData = firestoreData.savedRecipesLiveData


    fun getRecipes() = firestoreData.getSavedRecipes()

    fun getSavedRecipes() = networkBoundResource(
        query = {
            savedRecipesDao.getSavedRecipes()
        },
        fetch = {
            firestoreData.getSavedRecipes()
        },
        saveFetchResult = {
            db.withTransaction {
                savedRecipesDao.deleteAllSaveRecipes()
                savedRecipesDao.saveAllRecipes(it)
            }
        }
    )

    suspend fun saveRecipe(favouriteRecipe: FavouriteRecipe) {
        firestoreData.saveRecipeToFireStore(favouriteRecipe)
        savedRecipesDao.saveRecipe(favouriteRecipe)
    }

    suspend fun logOut() = firestoreData.logOut()


}

