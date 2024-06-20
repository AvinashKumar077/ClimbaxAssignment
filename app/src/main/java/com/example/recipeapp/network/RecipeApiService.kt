package com.example.recipeapp.network

import com.example.recipeapp.model.RecipeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApiService {
    @GET("/recipes")
    suspend fun getRecipes(@Query("page") page: Int, @Query("limit") limit: Int): Response<RecipeResponse>
}
