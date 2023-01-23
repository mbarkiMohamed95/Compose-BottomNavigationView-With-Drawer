package com.example.testcompose.presentation.mainView.appbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.testcompose.presentation.res.COLOR_PRIMARY


@Composable
fun HomeAppBar(
    title: String,
    openDrawer: () -> Unit = {},
    visibilty: Boolean = true,
    openFilters: () -> Unit = {}
) {

    AnimatedVisibility(visible = visibilty) {
        TopAppBar(
            backgroundColor = COLOR_PRIMARY,
            title = {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6,
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    openDrawer()
                }) {
                    Icon(Icons.Default.Menu, "Menu")
                }
            },
            actions = {
//            IconButton(onClick = openFilters) {
//                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
//            }
            }
        )
    }

}
