package com.ttt.fmi.firewall.whitelist

import com.ttt.fmi.firewall.whitelist.WhitelistedDesitinations.WHITELISTED
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WhitelistApi {

    @GET(WHITELISTED)
    suspend fun getWhitelisted(
        @Query("device_ip") deviceIp: String
    ): Response<List<WhitelistedResponse>>

}