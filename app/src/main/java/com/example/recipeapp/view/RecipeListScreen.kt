package com.example.recipeapp.view

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.recipeapp.model.Recipe
import com.example.recipeapp.viewmodel.RecipeViewModel

@Composable
fun RecipeListScreen(
    navController: NavController,
    recipes: List<Recipe>,
    favorites: Set<Int>,
    onFavoriteClick: (Recipe) -> Unit,
    viewModel: RecipeViewModel = hiltViewModel()
) {
    var page by remember { mutableStateOf(1) }

    LaunchedEffect(page) {
        viewModel.fetchRecipes(page, 20)
    }

    Column(modifier = Modifier.fillMaxHeight()) {
        RecipeList(recipes, favorites, onFavoriteClick) { recipeId ->
            Log.d("RecipeListScreen", "Recipe clicked: $recipeId")
            navController.navigate("RecipeDetail/$recipeId")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { if (page > 1) page--; viewModel.fetchRecipes(page, 20) }) {
                Text("Previous")
            }
            Button(onClick = { page++; viewModel.fetchRecipes(page, 20) }) {
                Text("Next")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}


