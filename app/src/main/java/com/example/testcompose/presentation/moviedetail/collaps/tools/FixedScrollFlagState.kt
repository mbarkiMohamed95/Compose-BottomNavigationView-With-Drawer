package com.example.testcompose.presentation.moviedetail.collaps.tools

import com.example.testcompose.presentation.moviedetail.collaps.tools.ScrollFlagState

abstract class FixedScrollFlagState(heightRange: IntRange) : ScrollFlagState(heightRange) {

    final override val offset: Float = 0f

}