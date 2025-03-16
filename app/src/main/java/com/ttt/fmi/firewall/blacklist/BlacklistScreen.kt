package com.ttt.fmi.firewall.whitelist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ttt.fmi.firewall.device.Device
import com.ttt.fmi.firewall.device.DeviceViewModel

@Composable
fun BlacklistScreen(
    onWhitelist: (Device) -> Unit,
    deviceId: String,
    deviceViewModel: DeviceViewModel,
    sharedWhitelistViewModel: SharedWhitelistViewModel

) {
    // Sample list of devices
    val devices by sharedWhitelistViewModel.foreignDevices.collectAsState()
    val selectedDevice by sharedWhitelistViewModel.selectedDevice.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Device Blacklist",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // List of Devices
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(devices?.size ?: 0) { index ->
                val device = devices?.get(index)
                if (device != null) {
                    DeviceBlacklistItem(
                        device = device,
                        isSelected = device.id == selectedDevice?.id,
                        onWhitelist = { },
                        onClick = { sharedWhitelistViewModel.switchSelected(device) }
                    )
                }
            }
        }


    }
}

@Composable
fun DeviceBlacklistItem(device: Device, isSelected: Boolean,
                        onWhitelist: (Device) -> Unit, onClick: (Device) -> Unit) {
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

                    onWhitelist(device)
                    isSelected
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
            ) {
                Text("Whitelist")
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}