package com.example.testcompose.presentation.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.example.testcompose.tools.pagingTool.items
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.rememberImagePainter
import com.example.testcompose.data.datasource.remote.ApiURL
import com.example.testcompose.data.model.MovieItem
import com.example.testcompose.presentation.mainView.currentRoute
import com.example.testcompose.presentation.res.COLOR_ACCENT_TRANS
import com.example.testcompose.presentation.res.DefaultBackgroundColor
import com.example.testcompose.presentation.res.cornerRadius10
import com.example.testcompose.presentation.res.progressBar.CircularIndeterminateProgressBar
import com.example.testcompose.routing.Screen
import com.example.testcompose.tools.dialog.ExitAlertDialog
import com.example.testcompose.tools.pagingTool.pagingLoadingState
import kotlinx.coroutines.flow.Flow

@Composable
fun HomeScreen(
    navController: NavController, movies: Flow<PagingData<MovieItem>>
) {
    val activity = (LocalContext.current as? Activity)
    val progressBar = remember { mutableStateOf(false) }
    val openDialog = remember { mutableStateOf(false) }
    val moviesItems: LazyPagingItems<MovieItem> = movies.collectAsLazyPagingItems()

    BackHandler(enabled = (currentRoute(navController) == Screen.HomeNav.route)) {
        // execute your custom logic here
        openDialog.value = true
    }
    Column(modifier = Modifier.background(DefaultBackgroundColor)) {
        CircularIndeterminateProgressBar(isDisplayed = progressBar.value, 0.4f)
        LazyVerticalGrid(columns = GridCells.Fixed(2),
            modifier = Modifier.padding(start = 5.dp, top = 5.dp, end = 5.dp),
            content = {
                items(moviesItems) { item ->
                    item?.let {
                        MovieItemView(item, navController)
                    }
                }
            })

    }
    if (openDialog.value) {
        ExitAlertDialog(navController, {
            openDialog.value = it
        }, {
            activity?.finish()
        })

    }
    moviesItems.pagingLoadingState {
        progressBar.value = it
    }
}


@Composable
fun MovieItemView(item: MovieItem, navController: NavController) {
    Card(modifier = Modifier.padding(5.dp)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(painter = rememberImagePainter(ApiURL.IMAGE_URL.plus(item.posterPath)),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(250.dp)
                    .cornerRadius10()
                    .clickable {
                        navController.navigate(Screen.MovieDetail.route.plus("/${item.id}"))
                    })
           // TopBar()
            BottomBar(item.title)
        }
    }

}
private const val BottomBarHeightFraction = 0.30f
private const val TopBarHeightFraction = BottomBarHeightFraction / 2
private val BarColor = Color(red = 0f, green = 0f, blue = 0f, alpha = 0.5f)


@Composable
 fun BoxScope.TopBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(TopBarHeightFraction/4)
            .background(BarColor)
            .align(Alignment.TopCenter)
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight(0.75f)
                .wrapContentWidth()
                .align(Alignment.CenterStart),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Rounded.Star, contentDescription = null)
            Icon(imageVector = Icons.Rounded.Star, contentDescription = null)
            Icon(imageVector = Icons.Rounded.Star, contentDescription = null)
        }

        Row(
            modifier = Modifier
                .fillMaxHeight(0.75f)
                .wrapContentWidth()
                .align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {  },
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .background(
                        color = LocalContentColor.current.copy(alpha = 0.0f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Rounded.Star,
                    contentDescription = null
                )
            }
            IconButton(
                onClick = {  },
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(1f)
                    .background(
                        color = LocalContentColor.current.copy(alpha = 0.0f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Rounded.AccountBox,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun BoxScope.BottomBar(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(BottomBarHeightFraction)
            .background(BarColor)
            .padding(5.dp)
            .align(Alignment.BottomCenter)
    ) {
        Text(
            text = text,
            color = Color.White,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
        )
    }
}




