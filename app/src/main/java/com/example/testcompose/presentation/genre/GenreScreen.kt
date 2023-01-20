package com.piashcse.hilt_mvvm_compose_movie.ui.screens.genre

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.testcompose.presentation.genre.GenreViewModel
import com.example.testcompose.presentation.home.HomeScreen

@Composable
fun GenreScreen(
    navController: NavController,
    genreId: String
) {
    val genreViewModel = hiltViewModel<GenreViewModel>()
    HomeScreen(
        navController = navController,
        movies = genreViewModel.moviesByGenre(genreId)
    )
}