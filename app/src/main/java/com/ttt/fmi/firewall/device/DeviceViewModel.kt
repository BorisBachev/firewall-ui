package com.ttt.fmi.firewall.device

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.ttt.fmi.firewall.AuthSharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DeviceViewModel (

    private val deviceService: DeviceService,
    private val authSharedPreferences: AuthSharedPreferences

): ViewModel() {

    private val _devices = MutableStateFlow<List<Device>?>(null)
    val devices: StateFlow<List<Device>?> = _devices

    private val _selectedDevice = MutableStateFlow<Device?>(null)
    val selectedDevice: StateFlow<Device?> = _selectedDevice

    fun removeDevice(device: Device?) {
        if (device == null) return

        authSharedPreferences.removeDevice(device.id)

        _devices.value = _devices.value?.filter { it.id != device.id }
    }
    fun addDevice(device: Device?) {
        if (device == null) return

        _devices.value = _devices.value?.plus(device) ?: listOf(device)
    }

    fun setSelectedDevice(device: Device?) {
        _selectedDevice.value = device
    }


}