import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'kmpble_platform_interface.dart';

/// An implementation of [KmpblePlatform] that uses method channels.
class MethodChannelKmpble extends KmpblePlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('kmpble');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }
  static Future<String?> getPlatformInfoPeripheral() async {
    final String? platformInfoPeripheral = await _channel.invokeMethod('getPlatformInfoPeripheral');
    return platformInfoPeripheral;
  }
  static Future<String?> getPlatformInfoScanner() async {
    final String? platformInfoScanner = await _channel.invokeMethod('getPlatformInfoScanner');
    return platformInfoScanner;
  }
}
