package com.sherlockblue.kmpble.ble.callbacks

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import com.sherlockblue.kmpble.ble.BleResponse
import com.sherlockblue.kmpble.ble.NativeBleEvent

class OnServiceChanged(val gatt: BluetoothGatt) : NativeBleEvent()

class OnServicesDiscovered(val gatt: BluetoothGatt?, val status: Int) : NativeBleEvent() {
  override fun toBleResponse(): BleResponse =
    BleResponse.ServicesDiscovered(
      status = status,
    )
}

class OnConnectionStateChange(val gatt: BluetoothGatt?, val status: Int, val newState: Int) : NativeBleEvent() {
  override fun toBleResponse(): BleResponse =
    BleResponse.ConnectionStateChange(
      status = status,
      newState = newState,
    )
}

class CallbackError(val message: String, val status: Int) : NativeBleEvent() {
  override fun toBleResponse(): BleResponse = BleResponse.Error(message = message, status = status)
}

class OnDescriptorWrite(
  val gatt: BluetoothGatt?,
  val descriptor: BluetoothGattDescriptor,
  val status: Int,
) : NativeBleEvent() {
  override fun toBleResponse(): BleResponse =
    BleResponse.DescriptorWrite(
      descriptorUUID = descriptor.uuid.toString(),
      status = status,
    )
}

class OnDescriptorRead(
  val gatt: BluetoothGatt,
  val descriptor: BluetoothGattDescriptor,
  val status: Int,
  val value: ByteArray,
) : NativeBleEvent() {
  override fun toBleResponse(): BleResponse =
    BleResponse.DescriptorRead(
      descriptorUUID = descriptor.uuid.toString(),
      data = value.copyOf(),
      status = 0,
    )
}

class OnCharacteristicChanged(
  val gatt: BluetoothGatt,
  val characteristic: BluetoothGattCharacteristic,
  val value: ByteArray,
) : NativeBleEvent() {
  override fun toBleResponse(): BleResponse =
    BleResponse.CharacteristicChanged(
      characteristicUUID = characteristic.service.uuid.toString(),
      data = value.copyOf(),
    )
}

class OnCharacteristicWrite(
  val gatt: BluetoothGatt,
  val characteristic: BluetoothGattCharacteristic,
  val status: Int,
) : NativeBleEvent() {
  override fun toBleResponse(): BleResponse =
    BleResponse.CharacteristicWrite(
      characteristicUUID = characteristic.service.uuid.toString(),
      status = status,
    )
}

class OnCharacteristicRead(
  val gatt: BluetoothGatt,
  val characteristic: BluetoothGattCharacteristic,
  val value: ByteArray,
  val status: Int,
) : NativeBleEvent() {
  override fun toBleResponse(): BleResponse =
    BleResponse.CharacteristicRead(
      characteristicUUID = characteristic.service.uuid.toString(),
      data = value.copyOf(),
      status = status,
    )
}

class OnMtuChanged(val gatt: BluetoothGatt?, val mtu: Int, val status: Int) : NativeBleEvent() {
  override fun toBleResponse(): BleResponse =
    BleResponse.MtuChanged(
      mtu = mtu,
      status = status,
    )
}

class OnReadRemoteRssi(val gatt: BluetoothGatt?, val rssi: Int, val status: Int) : NativeBleEvent() {
  override fun toBleResponse(): BleResponse =
    BleResponse.ReadRemoteRssi(
      rssi = rssi,
      status = status,
    )
}

class OnReliableWriteCompleted(val gatt: BluetoothGatt?, val status: Int) : NativeBleEvent() {
  override fun toBleResponse(): BleResponse =
    BleResponse.ReliableWriteCompleted(
      status = status,
    )
}

class OnPhyRead(val gatt: BluetoothGatt?, val txPhy: Int, val rxPhy: Int, val status: Int) : NativeBleEvent() {
  override fun toBleResponse(): BleResponse =
    BleResponse.PhyRead(
      txPhy = txPhy,
      rxPhy = rxPhy,
      status = status,
    )
}

class OnPhyUpdate(val gatt: BluetoothGatt?, val txPhy: Int, val rxPhy: Int, val status: Int) : NativeBleEvent() {
  override fun toBleResponse(): BleResponse =
    BleResponse.PhyUpdate(
      txPhy = txPhy,
      rxPhy = rxPhy,
      status = status,
    )
}
