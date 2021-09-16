package pawel.hn.mycookingapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.bumptech.glide.load.HttpException
import pawel.hn.mycookingapp.database.RemoteKeys
import pawel.hn.mycookingapp.database.RecipesDatabase
import pawel.hn.mycookingapp.model.Recipe
import pawel.hn.mycookingapp.network.RecipesApi
import pawel.hn.mycookingapp.utils.*
import java.io.IOException

const val ITEMS_LOAD = 20

@ExperimentalPagingApi
class RecipesMediator(
    private val recipesApi: RecipesApi,
    private val appDatabase: RecipesDatabase,
    private val queries: HashMap<String, String>,
) : RemoteMediator<Int, Recipe>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Recipe>): MediatorResult {

        val queryId = queries[QUERY_DIET] + queries[QUERY_MEAL]

        val page = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> appDatabase.remoteKeysDao().remoteKeysId(queryId).nextKey
        }

        queries[QUERY_OFFSET] = page.toString()
        queries[QUERY_NUMBER] = "20"

        try {
            val response = recipesApi.getRecipes(queries)

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    appDatabase.remoteKeysDao().clearRemoteKeys()
                    appDatabase.recipesDao().clearRecipes()
                }

                val nextKey = page + 20
                val keys = response.recipes.map {

                    RemoteKeys(query = queryId, nextKey = nextKey)
                }
                appDatabase.remoteKeysDao().insertAll(keys)
                appDatabase.recipesDao().insertAllRecipes(response.recipes)
            }
            return MediatorResult.Success(endOfPaginationReached = response.recipes.isEmpty())
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }
}
