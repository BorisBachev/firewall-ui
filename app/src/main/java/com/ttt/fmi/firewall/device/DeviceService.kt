package com.ttt.fmi.firewall.device

import retrofit2.Response

interface DeviceService {

    suspend fun getDevice(
        id: Long
    ): Response<DeviceResponse>

}