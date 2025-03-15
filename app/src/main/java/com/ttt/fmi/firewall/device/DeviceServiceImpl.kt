package com.ttt.fmi.firewall.device

import retrofit2.Response

class DeviceServiceImpl (

    private val deviceApi: DeviceApi

) : DeviceService  {

    override suspend fun getDevice(
        id: Long
    ): Response<DeviceResponse> {
        return deviceApi.getDevice(id)
    }

}