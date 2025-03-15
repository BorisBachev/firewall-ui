package com.ttt.fmi.firewall.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.ttt.fmi.firewall.device.Device
import com.ttt.fmi.firewall.device.DeviceViewModel
import com.ttt.fmi.firewall.ui.theme.FirewallTheme

@Composable
fun WelcomeCard()
{
    Row {
        Text(text = "Welcome to Гвинпин Firewall!", color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun WelcomeScreen(
    toDeviceList :() -> Unit,  deviceViewModel: DeviceViewModel
)
{

    var text by remember { mutableStateOf("") }
    var isError by remember { mutableStateOf(false) }


    FirewallTheme {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center)
        {
            Text(text = "Enter your IP:",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            TextField(
                value = text,
                onValueChange = { newText ->
                    if (newText.matches(Regex("^([0-9]{1,3}\\.){0,3}[0-9]{0,3}$"))) {
                        text = newText
                        isError = false
                    } else {
                        isError = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                isError = isError,
                placeholder = { Text("e.g., 192.168.1.1") },
                trailingIcon = {
                    if (isError) {
                        Icon(Icons.Default.Warning, contentDescription = "Error", tint = MaterialTheme.colorScheme.error)
                    }
                }
            )

            Button(
                onClick = {

                    if (text.matches(Regex("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"))) {
                        println("Valid IP Address: $text")
                        val newDevice = Device(text, true)
                        deviceViewModel.addDevice(newDevice)
                        toDeviceList()
                    } else {
                        println("Invalid IP Address")
                        isError = true
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit")
            }


        }

    }
}