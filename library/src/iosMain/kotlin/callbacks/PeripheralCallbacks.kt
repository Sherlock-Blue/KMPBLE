package com.sherlockblue.kmpble.callbacks

import kotlinx.cinterop.ObjCSignatureOverride
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBPeripheralDelegateProtocol
import platform.CoreBluetooth.CBService
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import platform.darwin.NSObject

class PeripheralCallbacks(private val coroutineScope: CoroutineScope) : NSObject(), CBPeripheralDelegateProtocol {
  private var _eventBus = MutableSharedFlow<BleEvent>()

  fun eventBus() = _eventBus as SharedFlow<BleEvent>

  private fun publishEvent(event: BleEvent) {
    coroutineScope.launch {
      _eventBus.emit(event)
    }
  }

  @ObjCSignatureOverride
  override fun peripheral(
    peripheral: CBPeripheral,
    didDiscoverIncludedServicesForService: CBService,
    error: NSError?,
  ) {
    publishEvent(
      BleEvent.OnServicesForServiceDiscovered(
        peripheral = peripheral,
        service = didDiscoverIncludedServicesForService,
      ),
    )
  }

  @ObjCSignatureOverride
  override fun peripheral(
    peripheral: CBPeripheral,
    didWriteValueForCharacteristic: CBCharacteristic,
    error: NSError?,
  ) {
    publishEvent(
      BleEvent.OnCharacteristicWrite(
        peripheral = peripheral,
        characteristic = didWriteValueForCharacteristic,
      ),
    )
  }

  @ObjCSignatureOverride
  override fun peripheral(
    peripheral: CBPeripheral,
    didWriteValueForDescriptor: CBDescriptor,
    error: NSError?,
  ) {
    publishEvent(
      BleEvent.OnDescriptorWrite(
        peripheral = peripheral,
        descriptor = didWriteValueForDescriptor,
      ),
    )
  }

  override fun peripheral(
    peripheral: CBPeripheral,
    didDiscoverServices: NSError?,
  ) {
    publishEvent(
      BleEvent.OnServicesDiscovered(
        peripheral = peripheral,
      ),
    )
  }

  @ObjCSignatureOverride
  override fun peripheral(
    peripheral: CBPeripheral,
    didDiscoverCharacteristicsForService: CBService,
    error: NSError?,
  ) {
    publishEvent(
      BleEvent.OnCharacteristicsDiscovered(
        peripheral = peripheral,
        service = didDiscoverCharacteristicsForService,
      ),
    )
  }

  @ObjCSignatureOverride
  override fun peripheral(
    peripheral: CBPeripheral,
    didDiscoverDescriptorsForCharacteristic: CBCharacteristic,
    error: NSError?,
  ) {
    publishEvent(
      BleEvent.OnDescriptorsDiscovered(
        peripheral = peripheral,
        characteristic = didDiscoverDescriptorsForCharacteristic,
      ),
    )
  }

  @ObjCSignatureOverride
  override fun peripheral(
    peripheral: CBPeripheral,
    didUpdateValueForCharacteristic: CBCharacteristic,
    error: NSError?,
  ) {
    publishEvent(
      BleEvent.OnCharacteristicUpdated(
        peripheral = peripheral,
        characteristic = didUpdateValueForCharacteristic,
      ),
    )
  }

  @ObjCSignatureOverride
  override fun peripheral(
    peripheral: CBPeripheral,
    didUpdateValueForDescriptor: CBDescriptor,
    error: NSError?,
  ) {
    publishEvent(
      BleEvent.OnDescriptorUpdated(
        peripheral = peripheral,
        descriptor = didUpdateValueForDescriptor,
      ),
    )
  }

  override fun peripheral(
    peripheral: CBPeripheral,
    didReadRSSI: NSNumber,
    error: NSError?,
  ) {
    publishEvent(
      BleEvent.OnReadRemoteRssi(
        peripheral = peripheral,
        didReadRSSI = didReadRSSI,
      ),
    )
  }
}
