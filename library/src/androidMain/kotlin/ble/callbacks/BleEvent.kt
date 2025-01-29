package com.sherlockblue.kmpble.ble.callbacks

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import com.sherlockblue.kmpble.ble.BleResponse

sealed class BleEvent : BleResponse() {
  class OnServiceChanged(val gatt: BluetoothGatt) : BleEvent()

  class OnMtuChanged(val gatt: BluetoothGatt?, val mtu: Int, val status: Int) : BleEvent()

  class OnReadRemoteRssi(val gatt: BluetoothGatt?, val rssi: Int, val status: Int) : BleEvent()

  class OnReliableWriteCompleted(val gatt: BluetoothGatt?, val status: Int) : BleEvent()

  class OnServicesDiscovered(val gatt: BluetoothGatt?, val status: Int) : BleEvent()

  class OnConnectionStateChange(val gatt: BluetoothGatt?, val status: Int, val newState: Int) : BleEvent()

  class OnPhyRead(val gatt: BluetoothGatt?, val txPhy: Int, val rxPhy: Int, val status: Int) : BleEvent()

  class OnPhyUpdate(val gatt: BluetoothGatt?, val txPhy: Int, val rxPhy: Int, val status: Int) : BleEvent()

  class CallbackError(val message: String, val status: Int) : BleEvent()

  class OnDescriptorWrite(
    val gatt: BluetoothGatt?,
    val descriptor: BluetoothGattDescriptor?,
    val status: Int,
  ) : BleEvent()

  class OnDescriptorRead(
    val gatt: BluetoothGatt,
    val descriptor: BluetoothGattDescriptor,
    val status: Int,
    val value: ByteArray,
  ) : BleEvent()

  class OnCharacteristicChanged(
    val gatt: BluetoothGatt,
    val characteristic: BluetoothGattCharacteristic,
    val value: ByteArray,
  ) : BleEvent()

  class OnCharacteristicWrite(
    val gatt: BluetoothGatt,
    val characteristic: BluetoothGattCharacteristic,
    val status: Int,
  ) : BleEvent()

  class OnCharacteristicRead(
    val gatt: BluetoothGatt,
    val characteristic: BluetoothGattCharacteristic,
    val value: ByteArray,
    val status: Int,
  ) : BleEvent()
}
