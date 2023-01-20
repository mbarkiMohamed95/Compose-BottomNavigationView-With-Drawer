package com.example.testcompose.presentation.home.action


sealed class HomeAction{
    object LoadGenreList: HomeAction()
    data class ExecuteSearch(val key:String): HomeAction()
}
