package pawel.hn.mycookingapp.model

import com.google.gson.annotations.SerializedName

data class FoodRecipe(
    @SerializedName("results")
    val recipes: List<Recipe>
)