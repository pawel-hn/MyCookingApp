package pawel.hn.mycookingapp.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pawel.hn.mycookingapp.model.FavouriteRecipe


@Dao
interface SavedRecipesDao {


    @Query("SELECT * FROM savedRecipes")
    fun getSavedRecipesFlow(): Flow<List<FavouriteRecipe>>

    @Query("SELECT * FROM savedRecipes")
     fun getSavedRecipesList(): List<FavouriteRecipe>

    @Query("DELETE FROM savedRecipes")
    suspend fun deleteAllSaveRecipes()

    @Delete(entity = FavouriteRecipe::class )
    suspend fun deleteRecipe(favouriteRecipe: FavouriteRecipe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllRecipes(favouriteRecipe: List<FavouriteRecipe>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRecipe(favouriteRecipe: FavouriteRecipe)








}