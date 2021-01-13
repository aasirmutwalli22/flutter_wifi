import 'package:flutter/material.dart';
import 'package:flutter_wifi/flutter_wifi.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final ssidController = TextEditingController();
  final passwordController = TextEditingController();
  @override Widget build(BuildContext context) => MaterialApp(
    home: Scaffold(
      appBar: AppBar(
        title: const Text('Plugin example app'),
      ),
      body: Column(
        children: [
          TextField(
            controller: ssidController,
            decoration: InputDecoration(
              labelText: 'ssid',
            ),
          ),
          TextField(
            controller: passwordController,
            decoration: InputDecoration(
              labelText: 'password',
            ),
          ),
          FlatButton(
            child: Text('connect'),
            onPressed: ()=> FlutterWifi.
            connect(ssid: ssidController.text, password: passwordController.text).
            then(print),
          ),
          FlatButton(
            child: Text('connected wifi'),
            onPressed: ()=> FlutterWifi.connectedWifi().then((value) {
              if(value != "<unknown ssid>") print((value as String).replaceAll(RegExp('\"'), '')); else print(value);
            }),
          ),
        ],
      ),
      floatingActionButton: FloatingActionButton(
        child: Icon(Icons.add_rounded),
        onPressed: ()=> FlutterWifi.disConnect(ssid: ssidController.text).then(print),
      ),
    ),
  );
}
