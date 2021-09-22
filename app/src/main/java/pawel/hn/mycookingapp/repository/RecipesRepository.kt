package pawel.hn.mycookingapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import dagger.hilt.android.scopes.ViewModelScoped
import pawel.hn.mycookingapp.data.RecipesMediator
import pawel.hn.mycookingapp.data.RecipesPagingSource
import pawel.hn.mycookingapp.database.RecipesDatabase
import pawel.hn.mycookingapp.model.Recipe
import pawel.hn.mycookingapp.network.RecipesApi
import pawel.hn.mycookingapp.utils.RECIPES_LOAD
import timber.log.Timber
import javax.inject.Inject

@ExperimentalPagingApi
@ViewModelScoped
class RecipesRepository @Inject constructor(
    private val recipesApi: RecipesApi,
    private val appDatabase: RecipesDatabase,
) {

    fun getRecipesFromNetwork(queries: HashMap<String, String>) =
        Pager(
            config = PagingConfig(
                pageSize = RECIPES_LOAD,
                maxSize = 100,
                enablePlaceholders = false),
            pagingSourceFactory = { RecipesPagingSource(recipesApi, queries) }
        ).liveData


    fun getRecipesCached(queries: HashMap<String, String>): LiveData<PagingData<Recipe>> {
        Timber.d("PHN, repository called")

        return Pager(
            config = PagingConfig(
                pageSize = RECIPES_LOAD,
                enablePlaceholders = false,
                maxSize = 100,
            ),
            remoteMediator = RecipesMediator(recipesApi, appDatabase, queries),
            pagingSourceFactory = { appDatabase.recipesDao().getRecipesPagingSource() },
        ).liveData
    }
}