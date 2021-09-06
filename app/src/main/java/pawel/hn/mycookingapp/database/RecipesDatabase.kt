package pawel.hn.mycookingapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import pawel.hn.mycookingapp.model.FavouriteRecipe


@Database(entities = [FavouriteRecipe::class], version = 1)
abstract class RecipesDatabase() : RoomDatabase() {


    abstract val savedRecipesDao: SavedRecipesDao

}