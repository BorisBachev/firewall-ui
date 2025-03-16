package com.ttt.fmi.firewall.navigation

import android.util.Log
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ttt.fmi.firewall.NavDestinations
import com.ttt.fmi.firewall.NavDestinations.BLACKLIST
import com.ttt.fmi.firewall.NavDestinations.DEVICE
import com.ttt.fmi.firewall.NavDestinations.LIST
import com.ttt.fmi.firewall.auth.start.StartViewModel
import com.ttt.fmi.firewall.auth.start.StartState
import com.ttt.fmi.firewall.NavDestinations.LOADING
import com.ttt.fmi.firewall.NavDestinations.QUARANTINE
import com.ttt.fmi.firewall.NavDestinations.WELCOME
import com.ttt.fmi.firewall.NavDestinations.WHITELIST
import com.ttt.fmi.firewall.auth.WelcomeScreen
import com.ttt.fmi.firewall.device.DeviceScreen
import com.ttt.fmi.firewall.device.DeviceViewModel
import com.ttt.fmi.firewall.list.DeviceListScreen
import com.ttt.fmi.firewall.list.DeviceListViewModel
import com.ttt.fmi.firewall.quarantine.QuarantineScreen

import com.ttt.fmi.firewall.ui.theme.FirewallTheme
import com.ttt.fmi.firewall.whitelist.BlacklistScreen
import com.ttt.fmi.firewall.whitelist.SharedWhitelistViewModel
import com.ttt.fmi.firewall.whitelist.WhitelistScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationHost(navController: NavHostController) {

    val startViewModel: StartViewModel = koinViewModel()
    val deviceViewModel: DeviceViewModel = koinViewModel()
    val deviceListViewModel: DeviceListViewModel = koinViewModel()
    val sharedWhitelistViewModel: SharedWhitelistViewModel = koinViewModel()

    val hasDevice by startViewModel.hasDevice.collectAsState()
    val state by startViewModel.startState.collectAsState()

    var currentRoute by rememberSaveable { mutableStateOf(LOADING) }


    LaunchedEffect(Unit) {
        startViewModel.checkDevice()
    }

    LaunchedEffect(state) {
        currentRoute = when {
            state == StartState.None || state == StartState.Loading -> {
                LOADING
            }

            state == StartState.Success && hasDevice -> {
                LIST
            }

            else -> {
                WELCOME
            }
        }
    }

    FirewallTheme {
        Scaffold(
            bottomBar = {
                if (false){
                    //shouldShowBottomNavigation(currentRoute)) {
                    BottomNavigation(
                        modifier = Modifier.height(64.dp),
                    ) {
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    Icons.Default.Info,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.padding(bottom = 4.dp, top = 8.dp)
                                )
                            },
                            label = {
                                Text("Your Device", modifier = Modifier.padding(top = 4.dp),
                                    color = Color.White
                                )
                            },
                            selected = currentRoute == "$DEVICE$",
                            onClick = { currentRoute = "$DEVICE$" },
                        )
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    Icons.AutoMirrored.Filled.List,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.padding(bottom = 4.dp, top = 8.dp)
                                )
                            },
                            label = {
                                Text("Device List", modifier = Modifier.padding(top = 4.dp),
                                    color = Color.White
                                )
                            },
                            selected = currentRoute == LIST,
                            onClick = { currentRoute = LIST },
                        )
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    Icons.AutoMirrored.Filled.List,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.padding(bottom = 4.dp, top = 8.dp)
                                )
                            },
                            label = {
                                Text("White List", modifier = Modifier.padding(top = 4.dp),
                                    color = Color.White
                                )
                            },
                            selected = currentRoute == "$WHITELIST$",
                            onClick = { currentRoute = "$WHITELIST$" },
                        )
                        BottomNavigationItem(
                            icon = {
                                Icon(
                                    Icons.AutoMirrored.Filled.List,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.padding(bottom = 4.dp, top = 8.dp)
                                )
                            },
                            label = {
                                Text("Black List", modifier = Modifier.padding(top = 4.dp),
                                    color = Color.White
                                )
                            },
                            selected = currentRoute == "$BLACKLIST$",
                            onClick = { currentRoute = "$BLACKLIST$" },
                        )
                    }
                }
            },
            content = { innerPadding ->
                NavHost(navController, startDestination = LOADING, Modifier.padding(innerPadding)) {
                    composable(WELCOME) {
                        WelcomeScreen(
                            toDeviceList = { currentRoute = LIST },
                            deviceViewModel = deviceViewModel
                        )
                    }
                    composable("$DEVICE/{deviceId}") { backStackEntry ->
                        val deviceId =
                            backStackEntry.arguments?.getString("deviceId")?.toString()

                        deviceId?.let {
                            DeviceScreen(
                                deviceId = it,
                                deviceViewModel = deviceViewModel
                            )
                        }
                    }
                    composable("$BLACKLIST/{deviceId}") { backStackEntry ->
                        val deviceId =
                            backStackEntry.arguments?.getString("deviceId")?.toString()

                        deviceId?.let {
                            BlacklistScreen(
                                onWhitelist = { currentRoute = LIST },
                                deviceId = deviceId,
                                deviceViewModel = deviceViewModel,
                                sharedWhitelistViewModel = sharedWhitelistViewModel
                            )
                        }
                    }
                    composable("$WHITELIST/{deviceId}") { backStackEntry ->
                        val deviceId =
                            backStackEntry.arguments?.getString("deviceId")?.toString()

                        deviceId?.let {
                            WhitelistScreen(
                                onBlacklist = { currentRoute = LIST },
                                deviceId = deviceId,
                                deviceViewModel = deviceViewModel,
                                sharedWhitelistViewModel = sharedWhitelistViewModel
                            )
                        }
                    }
                    composable(QUARANTINE) {
                        QuarantineScreen(

                        )
                    }
                    composable(LOADING) {
                        LoadingScreen()
                    }
                    composable(LIST) {
                        DeviceListScreen(
                            deviceViewModel = deviceViewModel,
                            deviceListViewModel = deviceListViewModel,
                            toDevice = { deviceId -> currentRoute = "$DEVICE/$deviceId" }
                        )
                    }
                }
            }
        )
    }

    LaunchedEffect(currentRoute) {
        when (currentRoute) {
            WELCOME -> navController.navigate(WELCOME)
            QUARANTINE -> navController.navigate(QUARANTINE)
            LOADING -> navController.navigate(LOADING)
            LIST -> navController.navigate(LIST)
            else -> {
                if (currentRoute.startsWith(DEVICE)) {
                    val deviceId = currentRoute.removePrefix(DEVICE).substringAfter("/")
                    navController.navigate("$DEVICE/$deviceId")
                }
                if (currentRoute.startsWith(WHITELIST)) {
                    val deviceId = currentRoute.removePrefix(WHITELIST).substringAfter("/")
                    navController.navigate("$WHITELIST/$deviceId")
                }
                if (currentRoute.startsWith(BLACKLIST)) {
                    val deviceId = currentRoute.removePrefix(BLACKLIST).substringAfter("/")
                    navController.navigate("$BLACKLIST/$deviceId")
                }
            }
        }
    }
}

@Composable
fun shouldShowBottomNavigation(currentRoute: String): Boolean {
    return currentRoute !in listOf(WELCOME, LOADING)
}