package com.example.recipeapp.repository

import com.example.recipeapp.model.Recipe
import com.example.recipeapp.network.RecipeApiService
import javax.inject.Inject

class RecipeRepository @Inject constructor(
    private val apiService: RecipeApiService
) {
    private val favoriteRecipes = mutableSetOf<Int>()

    suspend fun getRecipes(page: Int, limit: Int): List<Recipe> {
        val response = apiService.getRecipes(page, limit)
        if (response.isSuccessful) {
            val recipes = response.body()?.recipes ?: emptyList()
            // Set favorite state based on stored favorites
            recipes.forEach { recipe ->
                recipe.isFavorite = favoriteRecipes.contains(recipe.id)
            }
            return recipes
        } else {
            throw Exception("Error fetching recipes")
        }
    }

    fun toggleFavorite(recipeId: Int) {
        if (favoriteRecipes.contains(recipeId)) {
            favoriteRecipes.remove(recipeId)
        } else {
            favoriteRecipes.add(recipeId)
        }
    }

    fun getFavoriteRecipes(): Set<Int> = favoriteRecipes
}
