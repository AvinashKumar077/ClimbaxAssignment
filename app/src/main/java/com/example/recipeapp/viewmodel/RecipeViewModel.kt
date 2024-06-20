package com.example.recipeapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.model.Recipe
import com.example.recipeapp.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> = _recipes

    private val _favorites = MutableLiveData<Set<Int>>(setOf())
    val favorites: LiveData<Set<Int>> = _favorites

    private var currentPage = 1

    fun fetchRecipes(page: Int, pageSize: Int) {
        viewModelScope.launch {
            try {
                Log.d("RecipeViewModel", "Fetching recipes for page: $page")
                val fetchedRecipes = repository.getRecipes(page, pageSize)
                _recipes.value = fetchedRecipes
                Log.d("RecipeViewModel", "Fetched ${fetchedRecipes.size} recipes")
                Log.d("RecipeViewModel", "Recipes: $fetchedRecipes")
            } catch (e: Exception) {
                Log.e("RecipeViewModel", "Error fetching recipes", e)
            }
        }
    }



    fun toggleFavorite(recipe: Recipe) {
        repository.toggleFavorite(recipe.id)
        _favorites.value = repository.getFavoriteRecipes()
    }

}
