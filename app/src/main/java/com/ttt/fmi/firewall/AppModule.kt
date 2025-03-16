package com.ttt.fmi.firewall

import com.ttt.fmi.firewall.RetrofitHost.PHONE
import com.ttt.fmi.firewall.auth.start.StartViewModel
import com.ttt.fmi.firewall.device.api.DeviceApi
import com.ttt.fmi.firewall.device.api.DeviceService
import com.ttt.fmi.firewall.device.api.DeviceServiceImpl
import com.ttt.fmi.firewall.device.DeviceViewModel
import com.ttt.fmi.firewall.list.DeviceListViewModel
import com.ttt.fmi.firewall.whitelist.SharedWhitelistViewModel
import com.ttt.fmi.firewall.whitelist.WhitelistApi
import com.ttt.fmi.firewall.whitelist.WhitelistService
import com.ttt.fmi.firewall.whitelist.WhitelistServiceImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    single {
        AuthSharedPreferences.apply {
            init(androidContext())
        }
    }

    single(named("Retrofit")) {

        val httpInterceptor = HttpLoggingInterceptor()
        httpInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClientBuilder = OkHttpClient.Builder()
            //.addInterceptor(TokenInterceptor(get()))
            .addInterceptor(httpInterceptor)

        Retrofit.Builder()
            .baseUrl(PHONE)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClientBuilder.build())
            .build()
    }

    viewModel {
        StartViewModel(get())
    }
    viewModel {
        DeviceViewModel(get<DeviceService>(), get())
    }
    viewModel {
        DeviceListViewModel(get<DeviceService>())
    }
    viewModel{
        SharedWhitelistViewModel()
    }

    single<DeviceApi> { get<Retrofit>(named("Retrofit")).create(DeviceApi::class.java) }

    single<DeviceService> { DeviceServiceImpl(get<DeviceApi>()) }

    single<WhitelistApi> { get<Retrofit>(named("Retrofit")).create(WhitelistApi::class.java) }

    single<WhitelistService> { WhitelistServiceImpl(get<WhitelistApi>()) }





}