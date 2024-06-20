# RecipeApp

RecipeApp is an Android application that demonstrates the use of various modern Android development tools and practices, including Dagger Hilt for dependency injection, Retrofit for networking, Jetpack Compose for UI, and MVVM (Model-View-ViewModel) architecture for managing UI-related data.

## Video Demo :-    [Watch the video](https://www.youtube.com/watch?v=QQ3nI3k6uLM?feature=share)

## Features

- **Fetch recipes from an API**: The app fetches recipes from a dummy API.
- **View recipe details**: Users can view detailed information about each recipe.
- **Favorite recipes**: Users can mark recipes as favorite.
- **Pagination**: Users can navigate through pages of recipes.

## Architecture

The app follows the MVVM (Model-View-ViewModel) architecture pattern.

- **Model**: Represents the data and business logic of the app. The `Recipe` and `RecipeResponse` data classes are part of the Model.
- **View**: Displays the data to the user and sends user actions to the ViewModel. The `RecipeListScreen`, `RecipeDetailScreen`, and `RecipeItem` composables are part of the View.
- **ViewModel**: Acts as a bridge between the View and the Model. The `RecipeViewModel` is responsible for managing UI-related data in a lifecycle-conscious way.

## Libraries and Tools

### Dagger Hilt

[Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android) is used for dependency injection. It helps in managing the dependencies of the classes in a clean and efficient way.

- **`NetworkModule`**: Provides Retrofit and RecipeApiService instances.

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://dummyjson.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRecipeApiService(retrofit: Retrofit): RecipeApiService {
        return retrofit.create(RecipeApiService::class.java)
    }
}
```

### Retrofit

[Retrofit](https://square.github.io/retrofit/) is used for network requests. It is a type-safe HTTP client for Android and Java.

- **`RecipeApiService`**: Defines the API endpoints.

```kotlin
interface RecipeApiService {
    @GET("/recipes")
    suspend fun getRecipes(@Query("page") page: Int, @Query("limit") limit: Int): Response<RecipeResponse>
}
```

### Coroutines

[Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) are used for asynchronous programming. They help to manage background threads and reduce the amount of boilerplate code.

- The `getRecipes` function in the `RecipeRepository` uses coroutines to perform network requests asynchronously.

```kotlin
suspend fun getRecipes(page: Int, limit: Int): List<Recipe> {
    val response = apiService.getRecipes(page, limit)
    if (response.isSuccessful) {
        val recipes = response.body()?.recipes ?: emptyList()
        recipes.forEach { recipe ->
            recipe.isFavorite = favoriteRecipes.contains(recipe.id)
        }
        return recipes
    } else {
        throw Exception("Error fetching recipes")
    }
}
```

### Jetpack Compose

[Jetpack Compose](https://developer.android.com/jetpack/compose) is used for building the UI. It is a modern toolkit for building native Android UI.

- **`RecipeItem`**: Displays a single recipe item.

```kotlin
@Composable
fun RecipeItem(
    recipe: Recipe,
    isFavorite: Boolean,
    onFavoriteClick: (Recipe) -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = recipe.image),
                contentDescription = null,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(recipe.name, fontWeight = FontWeight.Bold)
                Text("Rating: ${recipe.rating}", fontWeight = FontWeight.Light)
                Text("Difficulty: ${recipe.difficulty}", fontWeight = FontWeight.Light)
                Text("Reviews: ${recipe.reviewCount}", fontWeight = FontWeight.Light)
            }
            IconButton(onClick = { onFavoriteClick(recipe) }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = null
                )
            }
        }
    }
}
```

### Navigation

[Jetpack Navigation](https://developer.android.com/guide/navigation) is used for navigating between different screens.

- **`NavHost`**: Defines the navigation graph for the app.

```kotlin
@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(navController)
        }
        composable(
            "recipeDetail/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.IntType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getInt("recipeId") ?: return@composable
            RecipeDetailScreen(recipeId)
        }
    }
}
```

## Running the App

1. Clone the repository.
2. Open the project in Android Studio.
3. Build and run the project on an Android device or emulator.

## Conclusion

This project demonstrates how to use Dagger Hilt for dependency injection, Retrofit for network requests, Coroutines for asynchronous programming, Jetpack Compose for UI, and MVVM architecture for managing UI-related data. These tools and practices help in building robust, maintainable, and scalable Android applications.
