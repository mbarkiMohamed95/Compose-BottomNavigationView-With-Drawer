package com.example.testcompose.presentation.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testcompose.data.model.BaseModel
import com.example.testcompose.data.model.Genres
import com.example.testcompose.data.repository.MovieRepository
import com.example.testcompose.presentation.home.action.HomeAction
import com.example.testcompose.tools.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {
    private val _genres: MutableState<DataState<Genres>?> = mutableStateOf(null)
    val genres: State<DataState<Genres>?> get() = _genres
    private  val _searchData: MutableState<DataState<BaseModel>?> = mutableStateOf(null)
    val searchData: State<DataState<BaseModel>?> get() =_searchData

    fun handelActions(action: HomeAction) {
        when (action) {
            is HomeAction.LoadGenreList -> {
                genreList()
            }
            is HomeAction.ExecuteSearch -> {
                searchApi(action.key)
            }
        }
    }

    private fun genreList() {
        viewModelScope.launch {
            repository.genreList().onEach {
                _genres.value = it
            }.launchIn(viewModelScope)
        }
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    private fun searchApi(searchKey: String) {
        viewModelScope.launch {
            flowOf(searchKey).debounce(300)
                .filter {
                    it.trim().isEmpty().not()
                }
                .distinctUntilChanged()
                .flatMapLatest {
                    repository.search(it)
                }.collect {
                    if (it is DataState.Success) {
                        it.data
                        Timber.e(" data ${it.data.totalPages}")
                    }
                    _searchData.value = it
                }
        }
    }
}