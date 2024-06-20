package com.example.recipeapp.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.recipeapp.model.Recipe

@Composable
fun RecipeList(
    recipes: List<Recipe>,
    favorites: Set<Int>,
    onFavoriteClick: (Recipe) -> Unit,
    onRecipeClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(recipes) { recipe ->
            RecipeItem(recipe, favorites.contains(recipe.id), onFavoriteClick) {
                onRecipeClick(recipe.id)
            }
        }
    }
}

