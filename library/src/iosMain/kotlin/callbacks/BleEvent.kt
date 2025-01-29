package com.sherlockblue.kmpble.callbacks

import com.sherlockblue.kmpble.ble.BleResponse
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBService
import platform.Foundation.NSNumber

sealed class BleEvent : BleResponse() {
  class OnServiceChanged(val central: CBCentralManager) : BleEvent()

  class OnPeripheralDiscovered(
    val central: CBCentralManager,
    val peripheral: CBPeripheral,
    val advertisementData: Map<Any?, *>,
    val RSSI: NSNumber,
  ) : BleEvent()

  class OnServicesDiscovered(val peripheral: CBPeripheral) : BleEvent()

  class OnServicesForServiceDiscovered(val peripheral: CBPeripheral, val service: CBService) : BleEvent()

  class OnCharacteristicsDiscovered(val peripheral: CBPeripheral, val service: CBService) : BleEvent()

  class OnDescriptorsDiscovered(val peripheral: CBPeripheral, val characteristic: CBCharacteristic) : BleEvent()

  class OnPeripheralConnect(val central: CBCentralManager, val peripheral: CBPeripheral?) :
    BleEvent()

  class OnPeripheralDisconnect(val central: CBCentralManager, val peripheral: CBPeripheral) :
    BleEvent()

  class OnReadRemoteRssi(val peripheral: CBPeripheral, val didReadRSSI: NSNumber) : BleEvent()

  class OnCharacteristicWrite(val peripheral: CBPeripheral, val characteristic: CBCharacteristic) : BleEvent()

  class OnCharacteristicUpdated(val peripheral: CBPeripheral, val characteristic: CBCharacteristic) : BleEvent()

  class OnDescriptorWrite(val peripheral: CBPeripheral, val descriptor: CBDescriptor) : BleEvent()

  class OnDescriptorUpdated(val peripheral: CBPeripheral, val descriptor: CBDescriptor) : BleEvent()

  class CallbackError(val message: String, val status: Int) : BleEvent()
}
