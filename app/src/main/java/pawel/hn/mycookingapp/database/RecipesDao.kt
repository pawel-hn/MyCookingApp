package pawel.hn.mycookingapp.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pawel.hn.mycookingapp.model.Recipe


@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = Recipe::class)
    suspend fun insertAllRecipes(recipes: List<Recipe>)

    @Query("SELECT * FROM foodRecipe ORDER BY id ASC")
    fun getRecipesPagingSource(): PagingSource<Int, Recipe>

    @Query("DELETE FROM foodRecipe")
    suspend fun clearRecipes()

}