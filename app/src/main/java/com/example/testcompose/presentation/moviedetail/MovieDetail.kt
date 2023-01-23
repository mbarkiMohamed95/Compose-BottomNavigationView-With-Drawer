package com.example.testcompose.presentation.moviedetail


import androidx.compose.animation.core.FloatExponentialDecaySpec
import androidx.compose.animation.core.animateDecay
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.testcompose.data.model.moviedetail.MovieDetail
import com.example.testcompose.presentation.moviedetail.collaps.CollapsingToolbar
import com.example.testcompose.presentation.moviedetail.collaps.tools.FixedScrollFlagState
import com.example.testcompose.presentation.moviedetail.collaps.views.toolbar.ToolbarState
import com.example.testcompose.presentation.moviedetail.collaps.scrollflags.ScrollState
import com.example.testcompose.presentation.res.TestComposeTheme
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import com.example.testcompose.R
import com.example.testcompose.data.model.BaseModel
import com.example.testcompose.data.model.artist.Artist
import com.example.testcompose.presentation.moviedetail.collaps.views.collapse.LazyCatalog
import com.example.testcompose.presentation.res.progressBar.CircularIndeterminateProgressBar
import com.example.testcompose.tools.DataState

@Composable
fun MovieDetail(navController: NavController, movieId: Int) {
    val movieDetailViewModel = hiltViewModel<MovieDetailViewModel>()
    val progressBar = remember { mutableStateOf(false) }
    val movieDetail = movieDetailViewModel.movieDetail
    val recommendedMovie = movieDetailViewModel.recommendedMovie
    val artist = movieDetailViewModel.artist

    LaunchedEffect(true) {
        movieDetailViewModel.movieDetailApi(movieId)
        movieDetailViewModel.recommendedMovieApi(movieId, 1)
        movieDetailViewModel.movieCredit(movieId)
    }
    movieDetail.value?.let {
        if (it is DataState.Success<MovieDetail>) {
            Catalog(
                navController = navController,
                movieDetail = it.data,
                progressBar = progressBar.value,
                recommendedMovie = recommendedMovie,
                artist = artist,
                onPrivacyTipButtonClicked = {},
                onSettingsButtonClicked = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }


}

private val MinToolbarHeight = 96.dp
private val MaxToolbarHeight = 176.dp

@Composable
private fun rememberToolbarState(toolbarHeightRange: IntRange): ToolbarState {
    return rememberSaveable(saver = ScrollState.Saver) {
        ScrollState(toolbarHeightRange)
    }
}


@Composable
fun Catalog(
    navController: NavController,
    movieDetail: MovieDetail,
    recommendedMovie: MutableState<DataState<BaseModel>?>,
    artist: MutableState<DataState<Artist>?>,
    progressBar: Boolean,
    onPrivacyTipButtonClicked: () -> Unit = {},
    onSettingsButtonClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    CircularIndeterminateProgressBar(isDisplayed = progressBar, 0.4f)

    val toolbarHeightRange = with(LocalDensity.current) {
        MinToolbarHeight.roundToPx()..MaxToolbarHeight.roundToPx()
    }
    val toolbarState = rememberToolbarState(toolbarHeightRange)
    val listState = rememberLazyListState()

    val scope = rememberCoroutineScope()

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                toolbarState.scrollTopLimitReached =
                    listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
                toolbarState.scrollOffset = toolbarState.scrollOffset - available.y
                return Offset(0f, toolbarState.consumed)
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                if (available.y > 0) {
                    scope.launch {
                        animateDecay(
                            initialValue = toolbarState.height + toolbarState.offset,
                            initialVelocity = available.y,
                            animationSpec = FloatExponentialDecaySpec()
                        ) { value, velocity ->
                            toolbarState.scrollTopLimitReached =
                                listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
                            toolbarState.scrollOffset =
                                toolbarState.scrollOffset - (value - (toolbarState.height + toolbarState.offset))
                            if (toolbarState.scrollOffset == 0f) scope.coroutineContext.cancelChildren()
                        }
                    }
                }

                return super.onPostFling(consumed, available)
            }
        }
    }

    Box(modifier = modifier.nestedScroll(nestedScrollConnection)) {
        LazyCatalog(
            movieDetail,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { translationY = toolbarState.height + toolbarState.offset }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { scope.coroutineContext.cancelChildren() }
                    )
                },
            listState = listState,
            contentPadding = PaddingValues(bottom = if (toolbarState is FixedScrollFlagState) MinToolbarHeight else 0.dp),
            navController = navController,
            recommendedMovie = recommendedMovie,
            artist = artist
        )
        CollapsingToolbar(
            backgroundImage = movieDetail.poster_path,
            progress = toolbarState.progress,
            onPrivacyTipButtonClicked = onPrivacyTipButtonClicked,
            onSettingsButtonClicked = onSettingsButtonClicked,
            modifier = Modifier
                .fillMaxWidth()
                .height(with(LocalDensity.current) { toolbarState.height.toDp() })
                .graphicsLayer { translationY = toolbarState.offset }
        )
    }

}