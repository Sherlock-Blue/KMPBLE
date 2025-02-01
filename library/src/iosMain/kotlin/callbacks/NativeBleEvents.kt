package com.sherlockblue.kmpble.callbacks

import com.sherlockblue.kmpble.ble.BleResponse
import com.sherlockblue.kmpble.ble.NativeBleEvent
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBService
import platform.Foundation.NSData
import platform.Foundation.NSNumber
import utils.toByteArray

class OnServiceChanged(val central: CBCentralManager) : NativeBleEvent()

class OnPeripheralDiscovered(
  val central: CBCentralManager,
  val peripheral: CBPeripheral,
  val advertisementData: Map<Any?, *>,
  val RSSI: NSNumber,
) : NativeBleEvent()

class OnServicesDiscovered(val peripheral: CBPeripheral) : NativeBleEvent() {
  override fun toBleResponse(): BleResponse =
    BleResponse.ServicesDiscovered(
      status = 0,
    )
}

class OnServicesForServiceDiscovered(val peripheral: CBPeripheral, val service: CBService) : NativeBleEvent()

class OnCharacteristicsDiscovered(val peripheral: CBPeripheral, val service: CBService) : NativeBleEvent()

class OnDescriptorsDiscovered(val peripheral: CBPeripheral, val characteristic: CBCharacteristic) : NativeBleEvent()

class OnPeripheralConnect(val central: CBCentralManager, val peripheral: CBPeripheral?) :
  NativeBleEvent() {
  override fun toBleResponse(): BleResponse =
    BleResponse.ConnectionStateChange(
      status = 0,
      newState = 2,
    )
}

class OnPeripheralDisconnect(val central: CBCentralManager, val peripheral: CBPeripheral) :
  NativeBleEvent() {
  override fun toBleResponse(): BleResponse =
    BleResponse.ConnectionStateChange(
      status = 0,
      newState = 0,
    )
}

class OnReadRemoteRssi(val peripheral: CBPeripheral, val didReadRSSI: NSNumber) : NativeBleEvent()

class OnCharacteristicRead(val characteristic: CBCharacteristic) : NativeBleEvent() {
  override fun toBleResponse(): BleResponse =
    BleResponse.CharacteristicRead(
      characteristicUUID = characteristic.UUID.UUIDString,
      data = characteristic.value!!.toByteArray(),
      status = 0,
    )
}

class OnCharacteristicWrite(val peripheral: CBPeripheral, val characteristic: CBCharacteristic) : NativeBleEvent() {
  override fun toBleResponse(): BleResponse =
    BleResponse.CharacteristicWrite(
      characteristicUUID = characteristic.UUID.UUIDString,
      status = 0,
    )
}

class OnCharacteristicUpdated(val peripheral: CBPeripheral, val characteristic: CBCharacteristic) : NativeBleEvent() {
  override fun toBleResponse(): BleResponse =
    BleResponse.CharacteristicChanged(
      characteristicUUID = characteristic.UUID.UUIDString,
      data = characteristic.value!!.toByteArray(),
    )
}

class OnDescriptorRead(val descriptor: CBDescriptor) : NativeBleEvent() {
  override fun toBleResponse(): BleResponse =
    BleResponse.DescriptorRead(
      descriptorUUID = descriptor.UUID.UUIDString,
      data = (descriptor.value!! as NSData).toByteArray(),
      status = 0,
    )
}

class OnDescriptorWrite(val peripheral: CBPeripheral, val descriptor: CBDescriptor) : NativeBleEvent() {
  override fun toBleResponse(): BleResponse =
    BleResponse.DescriptorWrite(
      descriptorUUID = descriptor.UUID.UUIDString,
      status = 0,
    )
}

class CallbackError(val message: String, val status: Int) : NativeBleEvent()
