package com.example.followtherecipe.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.followtherecipe.model.Recipe
import com.example.followtherecipe.model.RecipeApi
import kotlinx.coroutines.launch

class RecipeViewModel : ViewModel() {

    var recipes = mutableStateListOf<Recipe>()
        private set

    init {
        getRecipesList()
    }

    private fun getRecipesList(){
        viewModelScope.launch {
            var recipeApi : RecipeApi? = null
            try {
                recipeApi = RecipeApi!!.getInstance()
                recipes.clear()
                recipes.addAll(recipeApi.getRecipes())
            }catch ( e : Exception){
                Log.d("RECIPEVIEWMODEL",e.message.toString())
            }
        }
    }

}