package pawel.hn.mycookingapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "savedRecipes")
data class FavouriteRecipe(
    @PrimaryKey val id: Int,
    val title: String,
    val image: String,
    val readyInMinutes: String,
    val sourceUrl: String,
    val vegetarian: Boolean,
    val comment: String,
    val cooked: Boolean,
    val rating: String

) : Parcelable