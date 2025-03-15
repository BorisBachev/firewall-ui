package com.ttt.fmi.firewall.list

import androidx.lifecycle.ViewModel
import com.ttt.fmi.firewall.device.Device
import com.ttt.fmi.firewall.device.DeviceService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DeviceListViewModel (

    deviceService: DeviceService

): ViewModel() {

    private val _showAddDialog = MutableStateFlow<Boolean>(false)
    val showAddDialog: StateFlow<Boolean> = _showAddDialog

    fun showAddDialog(){
        _showAddDialog.value = true
    }
    fun hideAddDialog(){
        _showAddDialog.value = false
    }

}