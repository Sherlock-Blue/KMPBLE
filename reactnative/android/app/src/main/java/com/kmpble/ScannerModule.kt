package com.kmpble

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.sherlockblue.kmpble.scanner // Import the class from KMPBLE

class ScannerModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String {
    return "ScannerModule"
  }

  @ReactMethod
  fun getPlatformName(promise: Promise) {
    try {
      val myClass = Scanner()
      promise.resolve(myClass.getPlatformName())
    } catch (e: Exception) {
      promise.reject("Error", e)
    }
  }
}
