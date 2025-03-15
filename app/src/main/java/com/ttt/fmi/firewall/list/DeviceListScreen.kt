package com.ttt.fmi.firewall.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ttt.fmi.firewall.device.Device
import com.ttt.fmi.firewall.device.DeviceViewModel

@Composable
fun DeviceListScreen(
    toDevice: (String) -> Unit,
    deviceViewModel: DeviceViewModel,
    deviceListViewModel: DeviceListViewModel
) {
    val devices by deviceViewModel.devices.collectAsState()
    val selectedDevice by deviceViewModel.selectedDevice.collectAsState()
    val showAddDialog by deviceListViewModel.showAddDialog.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Device List",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(devices?.size ?: 0) { index ->
                    val device = devices?.get(index)
                    if (device != null) {
                        DeviceItem(
                            device = device,
                            isSelected = device.id == selectedDevice?.id,
                            onInfo = { toDevice(device.id) },
                            onQuarantine = { },
                            onClick = { deviceViewModel.setSelectedDevice(device) }
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { deviceListViewModel.showAddDialog() },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
                Text("Add")
            }

            Button(
                onClick = {
                    deviceViewModel.removeDevice(selectedDevice)
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                enabled = selectedDevice != null
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Remove")
                Text("Remove")
            }
        }
    }

    if (showAddDialog) {
        AddDeviceDialog(
            onDismiss = { deviceListViewModel.hideAddDialog() },
            onAddDevice = { ip ->
                if (ip.isNotBlank()) {
                    val newDevice = Device(ip, true)
                    deviceViewModel.addDevice(newDevice)
                }
            }
        )
    }
}

@Composable
fun DeviceItem(device: Device, isSelected: Boolean,
               onInfo: (String) -> Unit, onQuarantine: () -> Unit,
               onClick: (Device) -> Unit) {


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick(device) },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = device.id,
                style = MaterialTheme.typography.bodyLarge
            )

            Button(
                onClick = {
                    onInfo(device.id)
                },
                modifier = Modifier.wrapContentWidth()
            ) {
                Text("Info")
            }

            Button(
                onClick = { onQuarantine }
            ) {
                Text("Quarantine")
            }
        }
    }
}

@Composable
fun AddDeviceDialog(onDismiss: () -> Unit, onAddDevice: (String) -> Unit) {
    var ipAddress by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Add Device",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                TextField(
                    value = ipAddress,
                    onValueChange = { newText ->
                        if (newText.matches(Regex("^([0-9]{1,3}\\.){0,3}[0-9]{0,3}$"))) {
                            ipAddress = newText
                            isError = false
                        } else {
                            isError = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = isError,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    placeholder = { Text("Enter IP Address") },
                    trailingIcon = {
                        if (isError) {
                            Icon(Icons.Default.Warning, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                Button(
                    onClick = {
                        if (ipAddress.matches(Regex("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"))) {
                            onAddDevice(ipAddress)
                            onDismiss()
                        } else {
                            isError = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Add Device")
                }
            }
        }
    }
}