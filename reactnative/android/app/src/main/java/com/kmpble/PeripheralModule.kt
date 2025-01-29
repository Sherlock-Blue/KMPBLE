package com.kmpble

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.sherlockblue.kmpble.peripheral // Import the class from KMPBLE

class PeripheralModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String {
    return "PeripheralModule"
  }

  @ReactMethod
  fun getPlatformName(promise: Promise) {
    try {
      val myClass = Peripheral()
      promise.resolve(myClass.getPlatformName())
    } catch (e: Exception) {
      promise.reject("Error", e)
    }
  }
}
