package com.ttt.fmi.firewall.whitelist

import androidx.lifecycle.ViewModel
import com.ttt.fmi.firewall.device.Device
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedWhitelistViewModel() : ViewModel() {

    private val _foreignDevices = MutableStateFlow<List<Device>?>(null)
    val foreignDevices: StateFlow<List<Device>?> = _foreignDevices

    private val _selectedDevice = MutableStateFlow<Device?>(null)
    val selectedDevice: StateFlow<Device?> = _selectedDevice


    fun switchSelected(device: Device?) {
        _selectedDevice.value = device
    }

    fun blacklistCurrentDevice() {
        //
    }

}