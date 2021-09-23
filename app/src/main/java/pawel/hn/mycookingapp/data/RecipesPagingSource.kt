package pawel.hn.mycookingapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import pawel.hn.mycookingapp.model.Recipe
import pawel.hn.mycookingapp.network.RecipesApi
import pawel.hn.mycookingapp.utils.*
import retrofit2.HttpException
import java.io.IOException

class RecipesPagingSource(
    private val recipesApi: RecipesApi,
    private val queries: HashMap<String, String>
) : PagingSource<Int, Recipe>() {

    override fun getRefreshKey(state: PagingState<Int, Recipe>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(RECIPES_LOAD)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(RECIPES_LOAD)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Recipe> {
        val offset = params.key ?: 0

        queries[QUERY_OFFSET] = offset.toString()
        queries[QUERY_NUMBER] = params.loadSize.toString()

        return try {
            val response = recipesApi.getRecipes(queries)

            val recipes = response.recipes

            LoadResult.Page(
                data = recipes,
                prevKey = if (offset == 0) null else offset - RECIPES_LOAD,
                nextKey = if (recipes.isEmpty()) null else offset + RECIPES_LOAD
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}