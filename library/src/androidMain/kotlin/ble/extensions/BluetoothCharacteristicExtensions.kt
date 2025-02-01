package com.sherlockblue.kmpble.ble.extensions

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import com.sherlockblue.kmpble.ble.Characteristic
import com.sherlockblue.kmpble.ble.Descriptor
import com.sherlockblue.kmpble.ble.Service
import java.util.UUID

fun BluetoothGattCharacteristic.getDescriptor(uuid: String): BluetoothGattDescriptor? {
  if (uuid.isValidUUID()) {
    return getDescriptor(UUID.fromString(uuid))
  }
  return null
}

fun BluetoothGattService.toService(): Service = Service(UUID = uuid.toString(), characteristics = characteristics.getCharacteristics())

fun BluetoothGattCharacteristic.toCharacteristic(): Characteristic =
  Characteristic(
    UUID = uuid.toString(),
    serviceUUID = service.uuid.toString(),
    data = value,
    descriptors = descriptors.getDescriptors(),
  )

fun BluetoothGattDescriptor.toDescriptor(): Descriptor =
  Descriptor(
    UUID = uuid.toString(),
    characteristicUUID = characteristic.uuid.toString(),
    data = value,
  )

fun List<BluetoothGattService>.getServices() = map { service -> service.toService() }

fun List<BluetoothGattCharacteristic>.getCharacteristics() = map { characteristic -> characteristic.toCharacteristic() }

fun List<BluetoothGattDescriptor>.getDescriptors() = map { descriptor -> descriptor.toDescriptor() }
