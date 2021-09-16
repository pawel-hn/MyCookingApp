package pawel.hn.mycookingapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import pawel.hn.mycookingapp.model.FavouriteRecipe
import pawel.hn.mycookingapp.model.Recipe


@Database(entities = [FavouriteRecipe::class, Recipe::class, RemoteKeys::class], version = 1,exportSchema = false)
abstract class RecipesDatabase() : RoomDatabase() {

    abstract fun savedRecipesDao(): SavedRecipesDao
    abstract fun recipesDao(): RecipesDao
    abstract fun remoteKeysDao(): RemoteKeysDao

}