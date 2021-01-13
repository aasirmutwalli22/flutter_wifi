package com.aim.flutter_wifi

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.net.wifi.WifiNetworkSuggestion
import android.widget.Toast
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result


/** FlutterWifiPlugin */
class FlutterWifiPlugin: FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private lateinit var channel : MethodChannel
  private lateinit var manager : WifiManager
  private lateinit var context : Context
  private  lateinit var _result : Result
  lateinit var connectivityManager : ConnectivityManager

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_wifi")
    context = flutterPluginBinding.applicationContext
    manager =  context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
    connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    channel.setMethodCallHandler(this)
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    when (call.method) {
      "connect" -> {
        var success = false
        val ssid : String = call.argument<String>("ssid").toString()
        val password : String = call.argument<String>("password").toString()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
          val suggestionBuilder = WifiNetworkSuggestion.
          Builder().
          setSsid(ssid).
          setIsAppInteractionRequired(true) // Optional (Needs location permission)
          if(password == "") suggestionBuilder.setWpa2Passphrase(password)
          val status = manager.addNetworkSuggestions(listOf(suggestionBuilder.build()))
          Toast.makeText(context, status.toString(), Toast.LENGTH_SHORT).show()
//          connectivityManager.requestNetwork(
//                  NetworkRequest.
//                  Builder().
//                  addTransportType(NetworkCapabilities.TRANSPORT_WIFI).
////                  addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED).
////                  addCapability(NetworkCapabilities.NET_CAPABILITY_TRUSTED).
//                  setNetworkSpecifier(
//                          WifiNetworkSpecifier.
//                          Builder().
//                          setSsid(ssid).
//                          apply {
//                            if (password != "") setWpa2Passphrase(password)
//                          }.
//                          build()).
//                  build(),
//                  ConnectivityManager.NetworkCallback())
        } else {
          manager.apply {
            disconnect()
            success = enableNetwork(addNetwork(WifiConfiguration().
            apply {
              SSID = "\"$ssid\""
              if (password != "") preSharedKey = "\"$password\"" }
            ), true)
            reconnect()
          }
        }
        result.success(success)
      }
      "disconnect" -> {
        val ssid : String = call.argument<String>("ssid").toString()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q)
          manager.removeNetworkSuggestions(listOf(WifiNetworkSuggestion.Builder().setSsid(ssid).build()))
        else result.success(manager.disconnect())
      }
      "connectedWifi" -> result.success(manager.connectionInfo.ssid)
      else -> result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) =
          channel.setMethodCallHandler(null)
}
