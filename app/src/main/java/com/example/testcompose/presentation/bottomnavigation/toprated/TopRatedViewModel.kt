package com.example.testcompose.presentation.bottomnavigation.toprated


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.testcompose.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TopRatedViewModel @Inject constructor(repo: MovieRepository) : ViewModel() {
    val topRatedMovies = repo.topRatedPagingDataSource().cachedIn(viewModelScope)
}