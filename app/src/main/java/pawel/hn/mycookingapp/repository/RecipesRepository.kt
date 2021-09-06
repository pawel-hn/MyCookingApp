package pawel.hn.mycookingapp.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import dagger.hilt.android.scopes.ViewModelScoped
import pawel.hn.mycookingapp.data.RecipesPagingSource
import pawel.hn.mycookingapp.network.RecipesApi
import javax.inject.Inject

@ViewModelScoped
class RecipesRepository @Inject constructor(private val recipesApi: RecipesApi) {


    fun getRecipes(queries: HashMap<String, String>) =

        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false),
            pagingSourceFactory = {RecipesPagingSource(recipesApi,queries)}
        ).liveData



}