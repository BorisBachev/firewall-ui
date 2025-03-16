package com.ttt.fmi.firewall.device.api

import com.ttt.fmi.firewall.device.CreateDevice
import com.ttt.fmi.firewall.device.Device
import com.ttt.fmi.firewall.device.Logs
import retrofit2.Response

class DeviceServiceImpl (

    private val deviceApi: DeviceApi

) : DeviceService {

    override suspend fun getDevice(
        ip: String
    ): Device {

        val response = deviceApi.getDevice(ip)
        val device = Device(
            id = response.body()?.id ?: 0,
            ip = response.body()?.ip ?: "",
            is_quarantined = response.body()?.is_quarantined ?: 0,
            logs_id = response.body()?.logs_id ?: 0,
            mac_address = response.body()?.mac_address ?: "",
            name = response.body()?.name ?: "",
            port = response.body()?.port ?: 0,
            protocol = response.body()?.protocol ?: "",
        )

        return device
    }

    override suspend fun getDevices(): Response<List<Device>> {
        return deviceApi.getDevices()
    }

    override suspend fun getLogs(): Response<List<Logs>> {
        return deviceApi.getLogs()
    }

    override suspend fun getDeviceLogs(
        ip: String
    ): Response<List<Logs>> {
        return deviceApi.getDeviceLogs(ip)
    }

    override suspend fun newDevice(
        device: CreateDevice
    ): Response<Device> {
        return deviceApi.newDevice(device)
    }

    override suspend fun quarantineDevice(
        ip: String
    ): Response<Device> {
        return deviceApi.quarantineDevice(ip)
    }



}