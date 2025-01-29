import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'kmpble_method_channel.dart';

abstract class KmpblePlatform extends PlatformInterface {
  /// Constructs a KmpblePlatform.
  KmpblePlatform() : super(token: _token);

  static final Object _token = Object();

  static KmpblePlatform _instance = MethodChannelKmpble();

  /// The default instance of [KmpblePlatform] to use.
  ///
  /// Defaults to [MethodChannelKmpble].
  static KmpblePlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [KmpblePlatform] when
  /// they register themselves.
  static set instance(KmpblePlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }
}
