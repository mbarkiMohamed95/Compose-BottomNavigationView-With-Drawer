package com.example.testcompose.presentation.bottomnavigation.toprated

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.testcompose.presentation.home.HomeScreen

@Composable
fun TopRated(
    navController: NavController,
) {
    val topRatedViewModel = hiltViewModel<TopRatedViewModel>()
    HomeScreen(
        navController = navController,
        movies = topRatedViewModel.topRatedMovies
    )
}