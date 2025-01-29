#ifndef FLUTTER_PLUGIN_KMPBLE_PLUGIN_H_
#define FLUTTER_PLUGIN_KMPBLE_PLUGIN_H_

#include <flutter/method_channel.h>
#include <flutter/plugin_registrar_windows.h>

#include <memory>

namespace kmpble {

class KmpblePlugin : public flutter::Plugin {
 public:
  static void RegisterWithRegistrar(flutter::PluginRegistrarWindows *registrar);

  KmpblePlugin();

  virtual ~KmpblePlugin();

  // Disallow copy and assign.
  KmpblePlugin(const KmpblePlugin&) = delete;
  KmpblePlugin& operator=(const KmpblePlugin&) = delete;

  // Called when a method is called on this plugin's channel from Dart.
  void HandleMethodCall(
      const flutter::MethodCall<flutter::EncodableValue> &method_call,
      std::unique_ptr<flutter::MethodResult<flutter::EncodableValue>> result);
};

}  // namespace kmpble

#endif  // FLUTTER_PLUGIN_KMPBLE_PLUGIN_H_
