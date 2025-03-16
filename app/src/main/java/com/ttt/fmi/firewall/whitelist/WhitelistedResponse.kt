package com.ttt.fmi.firewall.whitelist

data class WhitelistedResponse(

    val device_id: Int,
    val id: String,
    val whitelisted_ip: String

)
