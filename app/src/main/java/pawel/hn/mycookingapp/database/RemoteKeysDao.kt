package pawel.hn.mycookingapp.database

import androidx.room.*



@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(remoteKey: RemoteKeys)

    @Query("SELECT * FROM remoteKeys WHERE mealAndDiet = :query")
    suspend fun remoteKeysId(query: String): RemoteKeys

    @Query("DELETE FROM remoteKeys")
    suspend fun clearRemoteKeys()
}