package pawel.hn.mycookingapp.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pawel.hn.mycookingapp.model.FavouriteRecipe


@Dao
interface SavedRecipesDao {


    @Query("SELECT * FROM savedRecipes")
    fun getSavedRecipes(): Flow<List<FavouriteRecipe>>

    @Query("DELETE FROM savedRecipes")
    suspend fun deleteAllSaveRecipes()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllRecipes(favouriteRecipe: List<FavouriteRecipe>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRecipe(favouriteRecipe: FavouriteRecipe)








}