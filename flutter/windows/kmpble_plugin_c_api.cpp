#include "include/kmpble/kmpble_plugin_c_api.h"

#include <flutter/plugin_registrar_windows.h>

#include "kmpble_plugin.h"

void KmpblePluginCApiRegisterWithRegistrar(
    FlutterDesktopPluginRegistrarRef registrar) {
  kmpble::KmpblePlugin::RegisterWithRegistrar(
      flutter::PluginRegistrarManager::GetInstance()
          ->GetRegistrar<flutter::PluginRegistrarWindows>(registrar));
}
