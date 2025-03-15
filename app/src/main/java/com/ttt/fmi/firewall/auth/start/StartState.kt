package com.ttt.fmi.firewall.auth.start

sealed class StartState {
    object None: StartState()
    object Loading: StartState()
    object Success: StartState()
    data class Error(val message: String): StartState()
}