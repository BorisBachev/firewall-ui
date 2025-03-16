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
import androidx.compose.material3.Switch
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
import com.ttt.fmi.firewall.device.CreateDevice
import com.ttt.fmi.firewall.device.Device
import com.ttt.fmi.firewall.device.DeviceViewModel

@Composable
fun DeviceListScreen(
    toDevice: (Int) -> Unit,
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
                            onQuarantine = { deviceViewModel.quarantineDevice(device) },
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
            onAddDevice = { device ->
                    deviceViewModel.newDevice(device)
            }
        )
    }
}

@Composable
fun DeviceItem(device: Device, isSelected: Boolean,
               onInfo: (Int) -> Unit, onQuarantine: () -> Unit,
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
                text = device.ip,
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
fun AddDeviceDialog(onDismiss: () -> Unit, onAddDevice: (CreateDevice) -> Unit) {
    var name by remember { mutableStateOf("") }
    var ip by remember { mutableStateOf("") }
    var macAddress by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("") }
    var protocol by remember { mutableStateOf("tcp") } // Default protocol is "tcp"
    var isQuarantined by remember { mutableStateOf(false) }
    var whitelist by remember { mutableStateOf<List<String>>(emptyList()) }

    var isNameError by remember { mutableStateOf(false) }
    var isIpError by remember { mutableStateOf(false) }
    var isMacAddressError by remember { mutableStateOf(false) }
    var isPortError by remember { mutableStateOf(false) }

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
                    value = name,
                    onValueChange = { newName ->
                        name = newName
                        isNameError = !newName.matches(Regex("^[a-zA-Z0-9 ]+$"))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    label = { Text("Device Name") },
                    isError = isNameError,
                    trailingIcon = {
                        if (isNameError) {
                            Icon(Icons.Default.Warning, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                TextField(
                    value = ip,
                    onValueChange = { newIp ->
                        if (newIp.matches(Regex("^([0-9]{1,3}\\.){0,3}[0-9]{0,3}$"))) {
                            ip = newIp
                            isIpError = false
                        } else {
                            isIpError = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    label = { Text("IP Address") },
                    isError = isIpError,
                    trailingIcon = {
                        if (isIpError) {
                            Icon(Icons.Default.Warning, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                TextField(
                    value = macAddress,
                    onValueChange = { newMac ->
                       // if (newMac.matches(Regex("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$"))) {
                             macAddress = newMac
                      //      isMacAddressError = false
                      //  } else {
                        //    isMacAddressError = true
                      //  }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    label = { Text("MAC Address") },
                    isError = isMacAddressError,
                    trailingIcon = {
                        if (isMacAddressError) {
                            Icon(Icons.Default.Warning, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                TextField(
                    value = port,
                    onValueChange = { newPort ->
                        if (newPort.matches(Regex("^[0-9]{1,5}$"))) {
                            port = newPort
                            isPortError = false
                        } else {
                            isPortError = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    label = { Text("Port") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    isError = isPortError,
                    trailingIcon = {
                        if (isPortError) {
                            Icon(Icons.Default.Warning, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Protocol: ${protocol.uppercase()}", modifier = Modifier.weight(1f))
                    Switch(
                        checked = protocol == "tcp",
                        onCheckedChange = { isTcp ->
                            protocol = if (isTcp) "tcp" else "udp"
                        }
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Quarantine Device", modifier = Modifier.weight(1f))
                    Switch(
                        checked = isQuarantined,
                        onCheckedChange = { isQuarantined = it }
                    )
                }

                TextField(
                    value = whitelist.joinToString(","),
                    onValueChange = { newWhitelist ->
                        val addresses = newWhitelist.split(",").map { it.trim() }
                        val isValid = addresses.all { it.matches(Regex("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$")) }
                        if (isValid) {
                            whitelist = addresses
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    label = { Text("Whitelist (comma-separated IPs)") },
                    placeholder = { Text("e.g., 192.168.1.102, 192.168.1.120") }
                )

                Button(
                    onClick = {
                        if (!isNameError && !isIpError && !isMacAddressError && !isPortError) {
                            val device = CreateDevice(
                                name = name,
                                ip = ip,
                                mac_address = macAddress,
                                port = port.toIntOrNull() ?: 0,
                                protocol = protocol,
                                is_quarantined = if(isQuarantined) 1 else 0,
                                whitelist = if (whitelist.isEmpty()) null else whitelist
                            )
                            onAddDevice(device)
                            onDismiss()
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