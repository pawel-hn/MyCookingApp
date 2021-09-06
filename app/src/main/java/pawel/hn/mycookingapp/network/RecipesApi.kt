package pawel.hn.mycookingapp.network

import pawel.hn.mycookingapp.model.FoodRecipe
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface RecipesApi {

    @GET("/recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queries: Map<String, String>
    ) : FoodRecipe





}