package com.aim.flutter_wifi

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiNetworkSpecifier
import android.os.Build
import androidx.annotation.RequiresApi

class WifiConnect {
    @RequiresApi(api = Build.VERSION_CODES.Q)
    fun connect(context: Context) {
        val builder = WifiNetworkSpecifier.Builder()
        builder.setSsid("abcdefgh")
        builder.setWpa2Passphrase("1234567890")
        val wifiNetworkSpecifier = builder.build()
        val networkRequestBuilder = NetworkRequest.Builder()
        networkRequestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        networkRequestBuilder.addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
        networkRequestBuilder.addCapability(NetworkCapabilities.NET_CAPABILITY_TRUSTED)
        networkRequestBuilder.setNetworkSpecifier(wifiNetworkSpecifier)
        val networkRequest = networkRequestBuilder.build()
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        cm?.requestNetwork(networkRequest, object : NetworkCallback() {
            override fun onAvailable(network: Network) {
                //Use this network object to Send request.
                super.onAvailable(network)
                //eg - Using OkHttp library to create a service request
            }
        })
    }
}