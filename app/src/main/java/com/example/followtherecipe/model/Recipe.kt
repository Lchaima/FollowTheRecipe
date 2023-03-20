package com.example.followtherecipe.model

import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL ="https://api.spoonacular.com/recipes/random?apiKey=6cadfbff81634eeea651fafbd4a3b889&number=5"

data class Recipe(
    var id : Int,
    var title: String,
    var summary : String,
    var image:String
)

interface RecipeApi{
    @GET("recipes")
    suspend fun getRecipes() : List<Recipe>

    companion object{
        var recipeService: RecipeApi? = null

        fun getInstance(): RecipeApi {
            if(recipeService === null) {
                recipeService=Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(RecipeApi::class.java)
            }
            return recipeService!!
        }
    }

}
