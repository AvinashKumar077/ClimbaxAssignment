package com.example.recipeapp.model

import com.example.recipeapp.model.Recipe


data class RecipeResponse(
    val recipes: List<Recipe>,
    val total: Int,
    val skip: Int,
    val limit: Int
)
