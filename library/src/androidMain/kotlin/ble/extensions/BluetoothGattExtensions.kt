package com.sherlockblue.kmpble.ble.extensions

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import java.util.UUID

fun BluetoothGatt.getCharacteristic(uuid: String): BluetoothGattCharacteristic? {
  if (uuid.isValidUUID()) {
    services.forEach { service ->
      service.getCharacteristic(UUID.fromString(uuid))?.let { return it }
    }
  }
  return null
}
