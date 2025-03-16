package com.ttt.fmi.firewall.device

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ttt.fmi.firewall.NavDestinations.BLACKLIST
import com.ttt.fmi.firewall.NavDestinations.LOADING
import com.ttt.fmi.firewall.NavDestinations.WHITELIST

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceScreen (
    deviceId: String,
    deviceViewModel: DeviceViewModel

) {
    var currentRoute by rememberSaveable { mutableStateOf(LOADING) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row {


            Text(
                text = "Device Page ",//$deviceName",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Button(onClick = { }) {
                Text(text = "Back")
            }
        }

        Text(
            text = "IP/Domain Name: 192.168.1.1",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "MAC Address: 00:1A:2B:3C:4D:5E",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Port: 8080",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        //whitelist/blacklist button
        Row {
            Button(onClick = {currentRoute = "$WHITELIST/$"}) {
                    Text(text = "Whitelist")
            }

            Button(onClick = {currentRoute = "$BLACKLIST/$" }) {
                    Text(text = "Blacklist")
            }
        }

    }
}