package com.ttt.fmi.firewall.whitelist

import retrofit2.Response

class WhitelistServiceImpl (

    private val whitelistApi: WhitelistApi

) : WhitelistService {

    override suspend fun getWhitelisted(deviceIp: String): Response<List<WhitelistedResponse>> {
        return whitelistApi.getWhitelisted(deviceIp)
    }

}