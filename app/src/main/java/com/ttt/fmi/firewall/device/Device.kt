package com.ttt.fmi.firewall.device

data class Device (

    val id: Int,
    val ip: String,
    val is_quarantined: Int,
    val logs_id: Int,
    val mac_address: String,
    val name: String,
    val port: Int,
    val protocol: String

)