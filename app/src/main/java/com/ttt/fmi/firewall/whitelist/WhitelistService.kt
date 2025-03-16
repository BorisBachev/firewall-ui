package com.ttt.fmi.firewall.whitelist

import retrofit2.Response

interface WhitelistService {

    suspend fun getWhitelisted(
        deviceIp: String
    ): Response<List<WhitelistedResponse>>

}