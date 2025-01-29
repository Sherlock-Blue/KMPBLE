
import 'kmpble_platform_interface.dart';

class Kmpble {
  Future<String?> getPlatformVersion() {
    return KmpblePlatform.instance.getPlatformVersion();
  }
}
