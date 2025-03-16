package com.ttt.fmi.firewall.device.api

import com.ttt.fmi.firewall.device.CreateDevice
import com.ttt.fmi.firewall.device.Device
import com.ttt.fmi.firewall.device.Logs
import com.ttt.fmi.firewall.device.api.DeviceDestinations.DEVICELOGS
import com.ttt.fmi.firewall.device.api.DeviceDestinations.DEVICES
import com.ttt.fmi.firewall.device.api.DeviceDestinations.GETDEVICE
import com.ttt.fmi.firewall.device.api.DeviceDestinations.LOGS
import com.ttt.fmi.firewall.device.api.DeviceDestinations.NEWDEVICE
import com.ttt.fmi.firewall.device.api.DeviceDestinations.QUARANTINEDEVICE
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface DeviceApi {

    @GET(DEVICES)
    suspend fun getDevices(): Response<List<Device>>

    @GET(GETDEVICE)
    suspend fun getDevice(
        @Query("device_ip") deviceIp: String
    ): Response<DeviceResponse>

    @GET(LOGS)
    suspend fun getLogs(): Response<List<Logs>>

    @GET(DEVICELOGS)
    suspend fun getDeviceLogs(
        @Query("device_ip") deviceIp: String
    ): Response<List<Logs>>

    @POST(NEWDEVICE)
    suspend fun newDevice(
        @Body device: CreateDevice
    ): Response<Device>

    @POST(QUARANTINEDEVICE)
    suspend fun quarantineDevice(
        @Query("device_ip") deviceIp: String
    ): Response<Device>

}