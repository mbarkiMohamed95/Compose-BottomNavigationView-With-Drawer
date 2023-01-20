package com.example.testcompose.presentation.mainView.action

sealed class MainAction{
    object LoadGenreList: MainAction()
    data class ExecuteSearch(val key:String): MainAction()
}
