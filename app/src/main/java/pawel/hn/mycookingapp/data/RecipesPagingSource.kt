package pawel.hn.mycookingapp.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import pawel.hn.mycookingapp.model.Recipe
import pawel.hn.mycookingapp.network.RecipesApi
import pawel.hn.mycookingapp.utils.*
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

class RecipesPagingSource(
    private val recipesApi: RecipesApi,
    private val queries: HashMap<String, String>
) : PagingSource<Int, Recipe>() {

    override fun getRefreshKey(state: PagingState<Int, Recipe>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(20)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(20)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Recipe> {

        val offset = params.key ?: 0

        Timber.d("load: ${queries[QUERY_MEAL]} PHN")

        queries[QUERY_OFFSET] = offset.toString()
        queries[QUERY_NUMBER] = params.loadSize.toString()

        Timber.d("offset: : $offset PHN")
        Timber.d("size to load: ${params.loadSize} PHN")
        return try {
            val response = recipesApi.getRecipes(queries)

            val recipes = response.recipes

            LoadResult.Page(
                data = recipes,
                prevKey = if (offset == 0) null else offset - 20,
                nextKey = if (recipes.isEmpty()) null else offset + 20
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}