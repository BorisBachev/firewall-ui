package com.ttt.fmi.firewall.auth.start

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ttt.fmi.firewall.AuthSharedPreferences
import com.ttt.fmi.firewall.device.DeviceService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.Response

class StartViewModel(

    private val authSharedPreferences: AuthSharedPreferences

): ViewModel(){

    private val _startState = MutableStateFlow<StartState>(StartState.None)
    val startState: StateFlow<StartState> = _startState

    private val _hasDevice = MutableStateFlow(false)
    val hasDevice: StateFlow<Boolean> = _hasDevice

    fun checkDevice(){

        _startState.value = StartState.Loading
        viewModelScope.launch {
            try {

                if(authSharedPreferences.hasDevices()){
                    _startState.value = StartState.Success
                    _hasDevice.value = true
                } else {
                    _startState.value = StartState.Success
                    _hasDevice.value = false
                }

            } catch (e: Exception) {
                _startState.value = StartState.Error("An error occurred")
                _hasDevice.value = false
            }

        }

    }

    fun clearState(){
        _startState.value = StartState.None
    }

}