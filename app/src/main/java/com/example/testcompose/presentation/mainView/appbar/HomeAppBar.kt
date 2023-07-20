package com.example.testcompose.presentation.mainView.appbar

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import com.example.testcompose.presentation.res.COLOR_PRIMARY


@Composable
fun HomeAppBar(title: String, openDrawer: () -> Unit, openFilters: () -> Unit) {
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
            IconButton(onClick = openFilters) {
               Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
           }
        }
    )
}
