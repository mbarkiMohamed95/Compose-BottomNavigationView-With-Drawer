package com.example.testcompose.tools.pagingTool

import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.example.testcompose.tools.DataState
import kotlin.time.Duration.Companion.minutes

fun <T : Any> State<DataState<T>?>.pagingLoadingState(isLoaded: (pagingState: Boolean) -> Unit) {
    when (this.value) {
        is DataState.Success<T> -> {
            isLoaded(false)
        }
        is DataState.Loading -> {
            isLoaded(true)
        }
        is DataState.Error -> {
            isLoaded(false)
        }
        else -> {

        }
    }
}


fun <T : Any> LazyGridScope.items(
    lazyPagingItems: LazyPagingItems<T>,
    itemContent: @Composable LazyGridItemScope.(value: T?) -> Unit
) {
    items(lazyPagingItems.itemCount) { index ->
        itemContent(lazyPagingItems[index])
    }
}

@Composable
fun <T : Any> LazyPagingItems<T>.pagingLoadingState(
    isLoaded: (pagingState: Boolean) -> Unit,
) {
    this.apply {
        when {
            // data is loading for first time
            loadState.refresh is LoadState.Loading -> {
                isLoaded(true)
            }
            // data is loading for second time or pagination
            loadState.append is LoadState.Loading -> {
                isLoaded(true)
            }
            loadState.refresh is LoadState.NotLoading -> {
                isLoaded(false)
            }
            loadState.append is LoadState.NotLoading -> {
                isLoaded(false)
            }
        }
    }
}

fun Int.hourMinutes(): String {
    return "${this.minutes.inWholeHours}h ${this % 60}m"
}