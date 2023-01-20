package com.example.testcompose.routing

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.testcompose.R
import com.example.testcompose.presentation.artistdetail.ArtistDetail
import com.example.testcompose.presentation.bottomnavigation.nowplaying.NowPlaying
import com.example.testcompose.presentation.bottomnavigation.popular.Popular
import com.example.testcompose.presentation.bottomnavigation.toprated.TopRated
import com.example.testcompose.presentation.bottomnavigation.upcoming.Upcoming
import com.example.testcompose.presentation.moviedetail.MovieDetail
import com.piashcse.hilt_mvvm_compose_movie.ui.screens.genre.GenreScreen

/**
 *create the navHost composable from the declared Screen
 */
@Composable
fun Navigation(
    navController: NavHostController, modifier: Modifier
) {
    NavHost(navController, startDestination = Screen.HomeNav.route, modifier) {

        composable(Screen.HomeNav.route) {
            NowPlaying(
                navController = navController
            )
        }

        composable(Screen.PopularNav.route) {
            Popular(
                navController = navController
            )
        }
        composable(Screen.TopRatedNav.route) {
            TopRated(
                navController = navController
            )
        }
        composable(Screen.UpcomingNav.route) {
            Upcoming(
                navController = navController
            )
        }
        /**
         * Genre Screen
         */
        composable(
            Screen.NavigationDrawer.route.plus(Screen.NavigationDrawer.objectPath),
            arguments = listOf(navArgument(Screen.NavigationDrawer.objectName) {
                type = NavType.StringType
            })
        ) { backStack ->
            val genreId = backStack.arguments?.getString(Screen.NavigationDrawer.objectName)
            genreId?.let {
                GenreScreen(
                    navController = navController, genreId
                )
            }
        }

        composable(
            Screen.MovieDetail.route.plus(Screen.MovieDetail.objectPath),
            arguments = listOf(navArgument(Screen.MovieDetail.objectName) {
                type = NavType.IntType
            })
        ) {
            label = stringResource(R.string.movie_detail)
            val movieId = it.arguments?.getInt(Screen.MovieDetail.objectName)
            if (movieId != null) {
                MovieDetail(
                    navController = navController, movieId
                )
            }

        }
        composable(
            Screen.ArtistDetail.route.plus(Screen.ArtistDetail.objectPath),
            arguments = listOf(navArgument(Screen.ArtistDetail.objectName) {
                type = NavType.IntType
            })
        ) {
            label = stringResource(R.string.artist_detail)
            val artistId = it.arguments?.getInt(Screen.ArtistDetail.objectName)
            if (artistId != null) {
                ArtistDetail(
                    artistId
                )
            }
        }
    }

}