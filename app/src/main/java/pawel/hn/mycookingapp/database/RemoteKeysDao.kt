package pawel.hn.mycookingapp.database

import androidx.room.*



@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<RemoteKeys>)

    @Query("SELECT * FROM remoteKeys WHERE `query` = :query")
    suspend fun remoteKeysId(query: String): RemoteKeys

    @Query("DELETE FROM remoteKeys")
    suspend fun clearRemoteKeys()
}