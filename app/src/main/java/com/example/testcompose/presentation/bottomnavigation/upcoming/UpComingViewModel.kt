package com.example.testcompose.presentation.bottomnavigation.upcoming


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.testcompose.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UpComingViewModel @Inject constructor(repo: MovieRepository) : ViewModel() {
    val upcomingMovies = repo.upcomingPagingDataSource().cachedIn(viewModelScope)
}