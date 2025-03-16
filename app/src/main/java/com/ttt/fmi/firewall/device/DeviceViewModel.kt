package com.ttt.fmi.firewall.device

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ttt.fmi.firewall.AuthSharedPreferences
import com.ttt.fmi.firewall.device.api.DeviceService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeviceViewModel (

    private val deviceService: DeviceService,
    private val authSharedPreferences: AuthSharedPreferences

): ViewModel() {

    private val _devices = MutableStateFlow<List<Device>?>(null)
    val devices: StateFlow<List<Device>?> = _devices

    private val _logs = MutableStateFlow<List<Logs>?>(null)
    val logs: StateFlow<List<Logs>?> = _logs

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

    fun getDevices() {

        viewModelScope.launch {

            try {
                val response = deviceService.getDevices().body()
                _devices.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

    fun getDeviceLogs(deviceIp: String) {

        viewModelScope.launch {

            try {
                val response = deviceService.getDeviceLogs(deviceIp).body()
                _logs.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

    fun getLogs() {

        viewModelScope.launch {

            try {
                val response = deviceService.getLogs().body()
                _logs.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

    fun getDevice(deviceIp: String) {

        viewModelScope.launch {

            try {
                val response = deviceService.getDevice(deviceIp)
                _selectedDevice.value = response
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

}