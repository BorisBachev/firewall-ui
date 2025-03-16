package com.ttt.fmi.firewall.device

data class CreateDevice(
    val ip: String,
    val is_quarantined: Int,
    val mac_address: String,
    val name: String,
    val port: Int,
    val protocol: String,
    val whitelist: List<String>?
)
