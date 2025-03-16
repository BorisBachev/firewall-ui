package com.ttt.fmi.firewall.device.api

import com.ttt.fmi.firewall.device.CreateDevice
import com.ttt.fmi.firewall.device.Device
import com.ttt.fmi.firewall.device.Logs
import retrofit2.Response

interface DeviceService {

    suspend fun getLogs(): Response<List<Logs>>

    suspend fun getDeviceLogs(
        ip: String
    ): Response<List<Logs>>

    suspend fun getDevices(): Response<List<Device>>

    suspend fun getDevice(
        ip: String
    ): Device

    suspend fun newDevice(
        device: CreateDevice
    ): Response<Device>

    suspend fun quarantineDevice(
        ip: String
    ): Response<Device>

}