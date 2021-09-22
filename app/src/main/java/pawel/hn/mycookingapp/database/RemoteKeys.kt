package pawel.hn.mycookingapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remoteKeys")
data class RemoteKeys
    (
    @PrimaryKey
    val mealAndDiet: String,
    val nextKey: Int?
    )