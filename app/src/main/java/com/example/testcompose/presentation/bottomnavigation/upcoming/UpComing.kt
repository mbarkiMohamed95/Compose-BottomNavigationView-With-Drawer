package com.example.testcompose.presentation.bottomnavigation.upcoming

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.testcompose.presentation.home.HomeScreen


@Composable
fun Upcoming(
    navController: NavController,
) {
    val upComingViewModel = hiltViewModel<UpComingViewModel>()
    HomeScreen(
        navController = navController,
        movies = upComingViewModel.upcomingMovies
    )
}