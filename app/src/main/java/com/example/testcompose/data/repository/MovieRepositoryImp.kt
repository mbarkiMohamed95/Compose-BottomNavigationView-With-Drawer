package com.example.testcompose.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.testcompose.data.datasource.remote.paging.*
import com.example.testcompose.data.datasource.remote.ApiService
import com.example.testcompose.data.model.BaseModel
import com.example.testcompose.data.model.Genres
import com.example.testcompose.data.model.artist.Artist
import com.example.testcompose.data.model.artist.ArtistDetail
import com.example.testcompose.data.model.moviedetail.MovieDetail
import com.example.testcompose.tools.DataState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepositoryImp @Inject constructor(
    private val apiService: ApiService
) :MovieRepository{
    override suspend fun movieDetail(movieId: Int): Flow<DataState<MovieDetail>> = flow {
        emit(DataState.Loading)
        try {
            val searchResult = apiService.movieDetail(movieId)
            emit(DataState.Success(searchResult))

        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    override suspend fun recommendedMovie(movieId: Int, page: Int): Flow<DataState<BaseModel>> = flow {
        emit(DataState.Loading)
        try {
            val searchResult = apiService.recommendedMovie(movieId, page)
            emit(DataState.Success(searchResult))

        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }


    override  suspend fun search(searchKey: String): Flow<DataState<BaseModel>> = flow {
        emit(DataState.Loading)
        try {
            val searchResult = apiService.search(searchKey)
            emit(DataState.Success(searchResult))

        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    override  suspend fun genreList(): Flow<DataState<Genres>> = flow {
        emit(DataState.Loading)
        try {
            val genreResult = apiService.genreList()
            emit(DataState.Success(genreResult))

        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    override  suspend fun movieCredit(movieId: Int): Flow<DataState<Artist>> = flow {
        emit(DataState.Loading)
        try {
            val artistResult = apiService.movieCredit(movieId)
            emit(DataState.Success(artistResult))

        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    override   suspend fun artistDetail(personId: Int): Flow<DataState<ArtistDetail>> = flow {
        emit(DataState.Loading)
        try {
            val artistDetailResult = apiService.artistDetail(personId)
            emit(DataState.Success(artistDetailResult))

        } catch (e: Exception) {
            emit(DataState.Error(e))
        }
    }

    override  fun nowPlayingPagingDataSource() = Pager(
        pagingSourceFactory = { NowPlayingPagingDataSource(apiService) },
        config = PagingConfig(pageSize = 1)
    ).flow

    override   fun popularPagingDataSource() = Pager(
        pagingSourceFactory = { PopularPagingDataSource(apiService) },
        config = PagingConfig(pageSize = 1)
    ).flow

    override  fun topRatedPagingDataSource() = Pager(
        pagingSourceFactory = { TopRatedPagingDataSource(apiService) },
        config = PagingConfig(pageSize = 1)
    ).flow

    override  fun upcomingPagingDataSource() = Pager(
        pagingSourceFactory = { UpcomingPagingDataSource(apiService) },
        config = PagingConfig(pageSize = 1)
    ).flow

    override   fun genrePagingDataSource(genreId: String) = Pager(
        pagingSourceFactory = { GenrePagingDataSource(apiService, genreId) },
        config = PagingConfig(pageSize = 1)
    ).flow

}