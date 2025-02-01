package com.sherlockblue.kmpble.ble

abstract class NativeBleEvent {
  open fun toBleResponse(): BleResponse = BleResponse.Error(message = "Unsupported Function", status = Int.MAX_VALUE)
}
