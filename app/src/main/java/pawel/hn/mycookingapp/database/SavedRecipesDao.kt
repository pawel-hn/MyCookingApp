package pawel.hn.mycookingapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import pawel.hn.mycookingapp.model.FavouriteRecipe

@Dao
interface SavedRecipesDao {

    @Query("SELECT * FROM savedRecipes ORDER BY title ASC")
    fun getSavedRecipes(): LiveData<List<FavouriteRecipe>>

    @Query("SELECT * FROM savedRecipes")
    suspend fun getSavedRecipesList(): List<FavouriteRecipe>

    @Query("DELETE FROM savedRecipes")
    suspend fun deleteAllSaveRecipes()

    @Delete(entity = FavouriteRecipe::class)
    suspend fun deleteRecipe(favouriteRecipe: FavouriteRecipe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllRecipes(favouriteRecipe: List<FavouriteRecipe>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRecipe(favouriteRecipe: FavouriteRecipe)


}