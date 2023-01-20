package com.example.testcompose.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.testcompose.data.datasource.remote.paging.*
import com.example.testcompose.data.model.BaseModel
import com.example.testcompose.data.model.Genres
import com.example.testcompose.data.model.MovieItem
import com.example.testcompose.data.model.artist.Artist
import com.example.testcompose.data.model.artist.ArtistDetail
import com.example.testcompose.data.model.moviedetail.MovieDetail
import com.example.testcompose.tools.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface MovieRepository {
    suspend fun movieDetail(movieId: Int): Flow<DataState<MovieDetail>>
    suspend fun recommendedMovie(movieId: Int, page: Int): Flow<DataState<BaseModel>>
    suspend fun search(searchKey: String): Flow<DataState<BaseModel>>
    suspend fun genreList(): Flow<DataState<Genres>>
    suspend fun movieCredit(movieId: Int): Flow<DataState<Artist>>
    suspend fun artistDetail(personId: Int): Flow<DataState<ArtistDetail>>
    fun nowPlayingPagingDataSource(): Flow<PagingData<MovieItem>>
    fun popularPagingDataSource(): Flow<PagingData<MovieItem>>
    fun topRatedPagingDataSource(): Flow<PagingData<MovieItem>>
    fun upcomingPagingDataSource(): Flow<PagingData<MovieItem>>
    fun genrePagingDataSource(genreId: String): Flow<PagingData<MovieItem>>
}