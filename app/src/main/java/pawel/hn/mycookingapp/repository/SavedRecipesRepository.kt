package pawel.hn.mycookingapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pawel.hn.mycookingapp.database.SavedRecipesDao
import pawel.hn.mycookingapp.model.FavouriteRecipe
import pawel.hn.mycookingapp.utils.*
import timber.log.Timber
import javax.inject.Inject

class SavedRecipesRepository @Inject constructor(private val savedRecipesDao: SavedRecipesDao) {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val fireStore = Firebase.firestore
    private val fireUserId = firebaseAuth.currentUser!!.uid
    private val collectionRef = fireStore.collection(fireUserId)
    private val coroutine = CoroutineScope(Dispatchers.IO)

    val savedRecipesLiveData = MutableLiveData<Resource<List<FavouriteRecipe>>>()

    private fun saveRecipeToFireStore(favouriteRecipe: FavouriteRecipe) {

        val recipe = hashMapOf(
            FIELD_TITLE to favouriteRecipe.title,
            FIELD_IMAGE to favouriteRecipe.image,
            FIELD_SOURCE_URL to favouriteRecipe.sourceUrl,
            FIELD_RATING to favouriteRecipe.rating,
            FIELD_READY_IN_MINS to favouriteRecipe.readyInMinutes,
            FIELD_COOKED to favouriteRecipe.cooked,
            FIELD_COMMENT to favouriteRecipe.comment,
            FIELD_VEGE to favouriteRecipe.vegetarian
        )
        fireStore
            .collection(fireUserId)
            .document(favouriteRecipe.id.toString())
            .set(recipe)
            .addOnFailureListener {
                Timber.d(it, "PHN ${it.message}")
            }
    }

    fun requestFavouriteRecipesFromFirestore() {
        savedRecipesLiveData.value = Resource.Loading()
        val recipes = mutableListOf<FavouriteRecipe>()
        collectionRef.get()
            .addOnSuccessListener { collection ->
                Timber.d("PHN succ, ${collection.documents.size}")

                collection.documents.forEach { document ->
                    recipes.add(FavouriteRecipe(
                        document.id.toInt(),
                        document.getString(FIELD_TITLE) ?: "title",
                        document.getString(FIELD_IMAGE) ?: "",
                        document.getString(FIELD_READY_IN_MINS) ?: "",
                        document.getString(FIELD_SOURCE_URL) ?: "",
                        document.getBoolean(FIELD_VEGE) ?: false,
                        document.getString(FIELD_COMMENT) ?: "",
                        document.getBoolean(FIELD_COOKED) ?: false,
                        document.getString(FIELD_RATING) ?: "",
                    )
                    )
                }
                recipes.sortBy { it.title.first() }
                savedRecipesLiveData.value = Resource.Success(recipes)

                coroutine.launch {
                    offlineCacheRecipes(recipes)
                }
            }
            .addOnFailureListener {
                Timber.d(it, "PHN, ${it.message}")
                savedRecipesLiveData.value = Resource.Error(it)
            }
    }

    suspend fun getSavedRecipesList() = savedRecipesDao.getSavedRecipesList()

    suspend fun deleteSavedRecipe(favouriteRecipe: FavouriteRecipe) {
        collectionRef.document(favouriteRecipe.id.toString()).delete()
        savedRecipesDao.deleteRecipe(favouriteRecipe)
    }

    suspend fun logOut() {
        savedRecipesDao.deleteAllSaveRecipes()
        firebaseAuth.signOut()
    }

    private suspend fun offlineCacheRecipes(recipes: List<FavouriteRecipe>) {
        savedRecipesDao.saveAllRecipes(recipes)
    }

    suspend fun saveRecipe(favouriteRecipe: FavouriteRecipe) {
        saveRecipeToFireStore(favouriteRecipe)
        savedRecipesDao.saveRecipe(favouriteRecipe)
    }

    fun getSavedRecipesFromLocal(): LiveData<List<FavouriteRecipe>> =
        savedRecipesDao.getSavedRecipes()

}