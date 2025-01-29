package com.sherlockblue.kmpble.ble.extensions

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import java.util.UUID

fun BluetoothGattCharacteristic.getDescriptor(uuid: String): BluetoothGattDescriptor? {
  if (uuid.isValidUUID()) {
    return getDescriptor(UUID.fromString(uuid))
  }
  return null
}
