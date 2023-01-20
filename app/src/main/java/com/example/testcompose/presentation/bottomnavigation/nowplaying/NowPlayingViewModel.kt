package com.example.testcompose.presentation.bottomnavigation.nowplaying


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.testcompose.data.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NowPlayingViewModel @Inject constructor(repo: MovieRepository) : ViewModel() {
    val popularMovies = repo.popularPagingDataSource().cachedIn(viewModelScope)
}