import Flutter
import UIKit
import KMPBLE

public class KmpblePlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "kmpble", binaryMessenger: registrar.messenger())
    let instance = KmpblePlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    switch call.method {
    case "getPlatformVersion":
      result("iOS " + UIDevice.current.systemVersion)
    case "getPlatformInfoPeripheral":
      val peripheral = Peripheral()
      result.success(peripheral.getPlatformInfoPeripheral())
    case "getPlatformInfoScanner":
      val scanner = Scanner()
      result.success(scanner.getPlatformInfoScanner())
    default:
      result(FlutterMethodNotImplemented)
    }
  }
}
