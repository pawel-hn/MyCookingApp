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


@ExperimentalPagingApi
class RecipesMediator(
    private val recipesApi: RecipesApi,
    private val appDatabase: RecipesDatabase,
    private val queries: HashMap<String, String>,
) : RemoteMediator<Int, Recipe>() {


    override suspend fun load(loadType: LoadType, state: PagingState<Int, Recipe>): MediatorResult {

        val queryId = queries[QUERY_DIET] + queries[QUERY_MEAL]

        val loadKey = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val key = appDatabase.remoteKeysDao().remoteKeysId(queryId)
                if (key.nextKey == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                key.nextKey
            }
        }

        queries[QUERY_OFFSET] = loadKey.toString()
        queries[QUERY_NUMBER] = when (loadType) {
            LoadType.REFRESH -> state.config.initialLoadSize.toString()
            else -> state.config.pageSize.toString()
        }

        try {
            val response = recipesApi.getRecipes(queries)

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    appDatabase.recipesDao().clearRecipes()
                    appDatabase.remoteKeysDao().clearRemoteKeys()

                }

                val nextKey = loadKey + 20

                appDatabase.remoteKeysDao().insertKey(RemoteKeys(
                    queryId, nextKey
                ))
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
