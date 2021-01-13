
import 'dart:async';

import 'package:flutter/services.dart';

class FlutterWifi {
  static const MethodChannel _channel = const MethodChannel('flutter_wifi');
  static Future<dynamic> connect({String ssid, String password}) async =>
      await _channel.invokeMethod('connect', <String, dynamic>{'ssid' : ssid, 'password': password,});
  static Future<dynamic> connectedWifi() async => await _channel.invokeMethod('connectedWifi');
  static Future<dynamic> disConnect({String ssid}) async => await _channel.invokeMethod('disconnect', <String, dynamic>{'ssid': ssid});
  // static Future<dynamic> scan() async => await _channel.invokeMethod('scan');

}
