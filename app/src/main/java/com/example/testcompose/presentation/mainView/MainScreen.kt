package com.example.testcompose.presentation.mainView


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.testcompose.R
import com.example.testcompose.data.model.Genres
import com.example.testcompose.presentation.mainView.action.MainAction
import com.example.testcompose.presentation.mainView.appbar.SearchBar
import com.example.testcompose.routing.Screen
import com.example.testcompose.tools.networkconnection.ConnectionState
import com.example.testcompose.tools.networkconnection.connectivityState
import com.piashcse.hilt_mvvm_compose_movie.ui.component.appbar.AppBarWithArrow
import com.example.testcompose.presentation.mainView.appbar.HomeAppBar
import com.example.testcompose.presentation.mainView.bottomNavigationView.BottomNavigationUI
import com.example.testcompose.presentation.mainView.drawer.DrawerUI
import com.example.testcompose.presentation.mainView.search.SearchUI
import com.example.testcompose.presentation.res.FloatingActionBackground
import com.example.testcompose.presentation.res.progressBar.CircularIndeterminateProgressBar
import com.example.testcompose.routing.Navigation
import com.example.testcompose.tools.DataState
import com.example.testcompose.tools.pagingTool.pagingLoadingState
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    // using remember tool to handel the Composable view

    val mainViewModel = hiltViewModel<MainViewModel>()
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val isAppBarVisible = remember { mutableStateOf(true) }
    val searchProgressBar = remember { mutableStateOf(false) }
    val genreName = remember { mutableStateOf("") }
    // genre list for navigation drawer
    val genres = mainViewModel.genres.value
    // internet connection
    val connection by connectivityState()
    val isConnected = connection === ConnectionState.Available

    // genre api call for first time
    LaunchedEffect(true) {
        mainViewModel.handelActions(MainAction.LoadGenreList)
    }


    Scaffold(scaffoldState = scaffoldState, topBar = {
        when (currentRoute(navController)) {
            Screen.HomeNav.route, Screen.PopularNav.route, Screen.TopRatedNav.route, Screen.UpcomingNav.route, Screen.NavigationDrawer.route -> {
                if (isAppBarVisible.value) {

                    val appTitle: String =
                        if (currentRoute(navController) == Screen.NavigationDrawer.route) genreName.value
                        else stringResource(R.string.app_title)

                    HomeAppBar(title = appTitle, openDrawer = {
                        scope.launch {
                            scaffoldState.drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }, openFilters = {
                        isAppBarVisible.value = false
                    })
                } else {
                    SearchBar(isAppBarVisible){
                        mainViewModel.handelActions(MainAction.ExecuteSearch(it))
                    }
                }
            }
            else -> {
                AppBarWithArrow(navigationTitle(navController)) {
                    navController.popBackStack()
                }
            }
        }
    }, drawerContent = {
        // Drawer content
        if (genres is DataState.Success<Genres>) {
            DrawerUI(navController, genres.data.genres) {
                genreName.value = it
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            }
        }
    }, floatingActionButton = {
        when (currentRoute(navController)) {
            Screen.HomeNav.route, Screen.PopularNav.route, Screen.TopRatedNav.route, Screen.UpcomingNav.route -> {
                FloatingActionButton(
                    onClick = {
                        isAppBarVisible.value = false
                    }, backgroundColor = FloatingActionBackground
                ) {
                    Icon(Icons.Filled.Search, "", tint = Color.Red)
                }
            }
        }
    }, bottomBar = {
        when (currentRoute(navController)) {
            Screen.HomeNav.route, Screen.PopularNav.route, Screen.TopRatedNav.route, Screen.UpcomingNav.route -> {
                BottomNavigationUI(navController)
            }
        }
    }, snackbarHost = {
        if (isConnected.not()) {
            Snackbar(
                action = {}, modifier = Modifier.padding(8.dp)
            ) {
                Text(text = stringResource(R.string.there_is_no_internet))
            }
        }
    }){
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Navigation(navController, Modifier.padding(it))
            Column {
                CircularIndeterminateProgressBar(isDisplayed = searchProgressBar.value, 0.1f)
                if (isAppBarVisible.value.not()) {
                    SearchUI(navController, mainViewModel.searchData) {
                        isAppBarVisible.value = true
                    }
                }
            }
        }
        mainViewModel.searchData.pagingLoadingState {
            searchProgressBar.value = it
        }
    }
}

@Composable
fun navigationTitle(navController: NavController): String {
    return when (currentRoute(navController)) {
        Screen.MovieDetail.route -> stringResource(id = R.string.movie_detail)
        Screen.ArtistDetail.route -> stringResource(id = R.string.artist_detail)
        Screen.Login.route -> stringResource(id = R.string.login)
        else -> {
            ""
        }
    }
}


@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route?.substringBeforeLast("/")
}
