package com.ttt.fmi.firewall.device

import com.ttt.fmi.firewall.device.DeviceDestinations.DEVICE
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

interface DeviceApi {

    @POST(DEVICE)
    suspend fun getDevice(
        @Path("deviceId") deviceId: Long
    ): Response<DeviceResponse>

}