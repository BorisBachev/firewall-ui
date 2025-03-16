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
import androidx.compose.material3.Switch
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
import com.ttt.fmi.firewall.device.CreateDevice
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

    toDeviceList :() -> Unit,
    deviceViewModel: DeviceViewModel,

) {

    var name by remember { mutableStateOf("") }
    var ip by remember { mutableStateOf("") }
    var macAddress by remember { mutableStateOf("") }
    var port by remember { mutableStateOf("") }
    var protocol by remember { mutableStateOf("tcp") } // Default protocol is "tcp"
    var isQuarantined by remember { mutableStateOf(0) }
    var whitelist by remember { mutableStateOf<List<String>>(emptyList()) }

    var isNameError by remember { mutableStateOf(false) }
    var isIpError by remember { mutableStateOf(false) }
    var isMacAddressError by remember { mutableStateOf(false) }
    var isPortError by remember { mutableStateOf(false) }

    FirewallTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Name Field
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

            // IP Field
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

            // MAC Address Field
            TextField(
                value = macAddress,
                onValueChange = { newMac ->
                    ///if (newMac.matches(Regex("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$"))) {
                        macAddress = newMac
                        isMacAddressError = false
                    //} else {
                    //    isMacAddressError = true
                    //}
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

            // Port Field
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

            // Protocol Switch (tcp/udp)
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

            // Quarantine Toggle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Quarantine Device", modifier = Modifier.weight(1f))
                Switch(
                    checked = if(isQuarantined == 1) true else false,
                    onCheckedChange = { isQuarantined = if(it) 1 else 0 }
                )
            }

            // Whitelist Field (Nullable)
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

            // Submit Button
            Button(
                onClick = {
                    if (!isNameError && !isIpError && !isMacAddressError && !isPortError) {
                        val device = CreateDevice(
                            name = name,
                            ip = ip,
                            mac_address = macAddress,
                            port = port.toIntOrNull() ?: 0,
                            protocol = protocol,
                            is_quarantined = isQuarantined,
                            whitelist = if (whitelist.isEmpty()) null else whitelist
                        )
                        deviceViewModel.newDevice(device)
                        toDeviceList()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit")
            }
        }
    }

}