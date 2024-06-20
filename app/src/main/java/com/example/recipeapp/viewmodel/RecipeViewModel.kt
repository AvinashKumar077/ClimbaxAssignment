package com.example.recipeapp.viewmodel

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
                val fetchedRecipes = repository.getRecipes(page, pageSize)
                _recipes.value = fetchedRecipes
            } catch (e: Exception) {
                // Handle the error appropriately
            }
        }
    }

    fun nextPage(pageSize: Int) {
        currentPage++
        fetchRecipes(currentPage, pageSize)
    }

    fun previousPage(pageSize: Int) {
        if (currentPage > 1) {
            currentPage--
            fetchRecipes(currentPage, pageSize)
        }
    }

    fun toggleFavorite(recipe: Recipe) {
        repository.toggleFavorite(recipe.id)
        _favorites.value = repository.getFavoriteRecipes()
        _recipes.value = _recipes.value // Trigger recomposition
    }
}
