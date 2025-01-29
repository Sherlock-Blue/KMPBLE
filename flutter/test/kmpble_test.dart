import 'package:flutter_test/flutter_test.dart';
import 'package:kmpble/kmpble.dart';
import 'package:kmpble/kmpble_platform_interface.dart';
import 'package:kmpble/kmpble_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockKmpblePlatform
    with MockPlatformInterfaceMixin
    implements KmpblePlatform {

  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final KmpblePlatform initialPlatform = KmpblePlatform.instance;

  test('$MethodChannelKmpble is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelKmpble>());
  });

  test('getPlatformVersion', () async {
    Kmpble kmpblePlugin = Kmpble();
    MockKmpblePlatform fakePlatform = MockKmpblePlatform();
    KmpblePlatform.instance = fakePlatform;

    expect(await kmpblePlugin.getPlatformVersion(), '42');
  });
}
