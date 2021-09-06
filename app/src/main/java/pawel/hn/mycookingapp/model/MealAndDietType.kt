package pawel.hn.mycookingapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MealAndDietType(
    val mealType: String,
    val mealTypeId: Int,
    val dietType: String,
    val dietTypeId: Int,
) : Parcelable
