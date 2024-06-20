package com.example.recipeapp

import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.recipeapp.view.RecipeDetailScreen
import com.example.recipeapp.view.RecipeListScreen
import com.example.recipeapp.viewmodel.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(navController)
        }
        composable("RecipeDetail/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")?.toIntOrNull()
            if (recipeId != null) {
                RecipeDetailScreen(recipeId = recipeId)
            } else {
                Text("Invalid recipe ID")
            }
        }
    }
}


@Composable
fun MainScreen(navController: NavHostController) {
    val viewModel: RecipeViewModel = hiltViewModel()
    val recipes by viewModel.recipes.observeAsState(emptyList())
    val favorites by viewModel.favorites.observeAsState(emptySet())
    var selectedTab by remember { mutableStateOf(0) }
    var page by remember { mutableStateOf(1) }

    LaunchedEffect(page) {
        viewModel.fetchRecipes(page, 20)
    }

    val tabs = listOf("Discover", "Favorite")

    Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Top) {
        // Title "Recipes" centered and extra bold
        Text(
            text = "Recipes",
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        // Tabs
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title, fontSize = 18.sp, color = Color.Black) },
                    selected = selectedTab == index,
                    onClick = { selectedTab = index }
                )
            }
        }

        Column(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                0 -> RecipeListScreen(navController, recipes, favorites, viewModel::toggleFavorite)
                1 -> RecipeListScreen(navController, recipes.filter { favorites.contains(it.id) }, favorites, viewModel::toggleFavorite)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation buttons
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



