package com.ttt.fmi.firewall

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object AuthSharedPreferences {
    private const val PREFS_NAME = "devices"
    private const val KEY_DEVICES = "devices_map"
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveDevice(ip: String, state: String) {
        val devices = getDevices().toMutableMap()
        devices[ip] = state
        saveDevices(devices)
    }

    fun getDevice(ip: String): String? {
        return getDevices()[ip]
    }

    fun getDevices(): Map<String, String> {
        val devicesJson = sharedPreferences.getString(KEY_DEVICES, null)
        return if (devicesJson.isNullOrEmpty()) {
            emptyMap()
        } else {
            val type = object : TypeToken<Map<String, String>>() {}.type
            Gson().fromJson(devicesJson, type)
        }
    }

    fun removeDevice(ip: String?) {
        val devices = getDevices().toMutableMap()
        devices.remove(ip)
        saveDevices(devices)
    }

    fun clearDevices() {
        sharedPreferences.edit().remove(KEY_DEVICES).apply()
    }

    private fun saveDevices(devices: Map<String, String>) {
        val devicesJson = Gson().toJson(devices)
        sharedPreferences.edit().putString(KEY_DEVICES, devicesJson).apply()
    }

    fun hasDevices(): Boolean {
        //return getDevices().isNotEmpty()
        return false
    }
}