package com.example.testcompose.presentation.bottomnavigation.nowplaying

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.testcompose.presentation.home.HomeScreen

@Composable
fun NowPlaying(
    navController: NavController,
) {
    val nowPlayViewModel = hiltViewModel<NowPlayingViewModel>()
    HomeScreen(
        navController = navController,
        movies = nowPlayViewModel.popularMovies
    )
}