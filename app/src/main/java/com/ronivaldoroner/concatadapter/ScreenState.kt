package com.ronivaldoroner.concatadapter

sealed class ScreenState {
    object Success : ScreenState()
    object Error : ScreenState()
    object Loading : ScreenState()
}
