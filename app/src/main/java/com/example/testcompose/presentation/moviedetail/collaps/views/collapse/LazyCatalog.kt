package com.example.testcompose.presentation.moviedetail.collaps.views.collapse

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.testcompose.data.model.BaseModel
import com.example.testcompose.data.model.artist.Artist
import com.example.testcompose.data.model.moviedetail.MovieDetail
import com.example.testcompose.tools.DataState
import com.example.testcompose.tools.text.SubtitlePrimary
import com.example.testcompose.tools.text.SubtitleSecondary
import com.example.testcompose.R
import com.example.testcompose.data.datasource.remote.ApiURL
import com.example.testcompose.data.model.MovieItem
import com.example.testcompose.data.model.artist.Cast
import com.example.testcompose.presentation.res.*
import com.example.testcompose.routing.Screen
import com.example.testcompose.tools.hourMinutes


@Composable
fun LazyCatalog(
    movieDetail: MovieDetail,
    modifier: Modifier = Modifier,
    navController: NavController?,
    listState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    recommendedMovie: MutableState<DataState<BaseModel>?>,
    artist: MutableState<DataState<Artist>?>,
) {
    val list = listOf(movieDetail)
    val chunkedList = remember(list, 2) {
        list.chunked(2)
    }

    LazyColumn(
        state = listState, contentPadding = contentPadding, modifier = modifier
    ) {
        items(chunkedList) { chunk ->
            chunk.forEach {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 15.dp, end = 15.dp)
                ) {
                    Text(
                        text = movieDetail.title,
                        modifier = Modifier.padding(top = 10.dp),
                        color = FontColor,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.W700,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp, top = 10.dp)
                    ) {

                        Column(Modifier.weight(1f)) {
                            SubtitlePrimary(
                                text = movieDetail.original_language,
                            )
                            SubtitleSecondary(
                                text = stringResource(R.string.language)
                            )
                        }
                        Column(Modifier.weight(1f)) {
                            SubtitlePrimary(
                                text = movieDetail.vote_average.toString(),
                            )
                            SubtitleSecondary(
                                text = stringResource(R.string.rating)
                            )
                        }
                        Column(Modifier.weight(1f)) {
                            SubtitlePrimary(
                                text = movieDetail.runtime.hourMinutes()
                            )
                            SubtitleSecondary(
                                text = stringResource(R.string.duration)
                            )
                        }
                        Column(Modifier.weight(1f)) {
                            SubtitlePrimary(
                                text = movieDetail.release_date
                            )
                            SubtitleSecondary(
                                text = stringResource(R.string.release_date)
                            )
                        }
                    }
                    Text(
                        text = stringResource(R.string.description),
                        color = FontColor,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = movieDetail.overview,
                        color = SecondaryFontColor,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                    recommendedMovie.value?.let {
                        if (it is DataState.Success<BaseModel>) {
                            RecommendedMovie(navController, it.data.results)
                        }
                    }
                    artist.value?.let {
                        if (it is DataState.Success<Artist>) {
                            ArtistAndCrew(navController, it.data.cast)
                        }
                    }
                }
            }
        }


    }
}

@Composable
fun RecommendedMovie(navController: NavController?, recommendedMovie: List<MovieItem>) {
    Column(modifier = Modifier.padding(bottom = 10.dp)) {
        Text(
            text = stringResource(R.string.similar),
            color = FontColor,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold
        )
        LazyRow(modifier = Modifier.fillMaxHeight()) {
            items(recommendedMovie, itemContent = { item ->
                Column(
                    modifier = Modifier.padding(
                        start = 0.dp, end = 8.dp, top = 5.dp, bottom = 5.dp
                    )
                ) {
                    Image(painter = rememberImagePainter(ApiURL.IMAGE_URL.plus(item.posterPath)),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .height(190.dp)
                            .width(140.dp)
                            .cornerRadius10()
                            .clickable {
                                navController?.navigate(
                                    Screen.MovieDetail.route.plus(
                                        "/${item.id}"
                                    )
                                )
                            })
                }
            })
        }
    }
}

@Composable
fun ArtistAndCrew(navController: NavController?, cast: List<Cast>) {
    Column(modifier = Modifier.padding(bottom = 10.dp)) {
        Text(
            text = stringResource(R.string.cast),
            color = FontColor,
            fontSize = 17.sp,
            fontWeight = FontWeight.SemiBold
        )
        LazyRow(modifier = Modifier.fillMaxHeight()) {
            items(cast, itemContent = { item ->
                Column(
                    modifier = Modifier.padding(
                        start = 0.dp, end = 10.dp, top = 5.dp, bottom = 5.dp
                    ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(painter = rememberImagePainter(ApiURL.IMAGE_URL.plus(item.profilePath)),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .padding(bottom = 5.dp)
                            .height(80.dp)
                            .width(80.dp)
                            .cornerRadius40()
                            .clickable {
                                navController?.navigate(
                                    Screen.ArtistDetail.route.plus(
                                        "/${item.id}"
                                    )
                                )
                            })
                    SubtitleSecondary(text = item.name)
                }
            })
        }
    }
}