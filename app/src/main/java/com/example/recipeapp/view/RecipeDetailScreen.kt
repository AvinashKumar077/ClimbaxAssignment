package com.example.recipeapp.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.recipeapp.viewmodel.RecipeViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.recipeapp.model.Recipe

@Composable
fun RecipeDetailScreen(recipeId: Int, viewModel: RecipeViewModel = hiltViewModel()) {
    val recipeState by viewModel.recipes.observeAsState()
    LaunchedEffect(true) {
        viewModel.fetchRecipes(recipeId, 20)
    }
    val recipe = recipeState?.find { it.id == recipeId }

    Log.d("RecipeDetailScreen", "Recipe ID: $recipeId")
    Log.d("RecipeDetailScreen", "Recipe: $recipe")

    recipe?.let {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = it.image),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = it.name,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${it.prepTimeMinutes + it.cookTimeMinutes} min",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
                Text(
                    text = "·",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
                Text(
                    text = "${it.caloriesPerServing} kcal",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                InfoBox(text = "Servings: ${it.servings}", backgroundColor = MaterialTheme.colorScheme.primary)
                InfoBox(text = "Difficulty: ${it.difficulty}", backgroundColor = MaterialTheme.colorScheme.primary)
                InfoBox(text = "Cuisine: ${it.cuisine}", backgroundColor = MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(16.dp))

            var selectedTabIndex by remember { mutableStateOf(0) }
            val tabs = listOf("Ingredients", "Directions")

            TabRow(selectedTabIndex = selectedTabIndex) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> IngredientsTab(it)
                1 -> DirectionsTab(it)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                it.tags.forEach { tag ->
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)),
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(
                            text = "#$tag",
                            modifier = Modifier.padding(8.dp),
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoBox(text: String, backgroundColor: Color) {
    Box(
        modifier = Modifier
            .background(color = backgroundColor, shape = RoundedCornerShape(12.dp))
            .padding(8.dp)
    ) {
        Text(text = text, fontSize = 16.sp, color = Color.White)
    }
}

@Composable
fun IngredientsTab(recipe: Recipe) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        recipe.ingredients.forEach { ingredient ->
            Text(text = ingredient, fontSize = 16.sp)
        }
    }
}

@Composable
fun DirectionsTab(recipe: Recipe) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        recipe.instructions.forEachIndexed { index, instruction ->
            Text(text = "${index + 1}. $instruction", fontSize = 16.sp)
        }
    }
}
