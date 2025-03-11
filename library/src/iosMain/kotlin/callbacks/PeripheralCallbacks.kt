package com.sherlockblue.kmpble.callbacks

import com.sherlockblue.kmpble.ble.BleResponse
import com.sherlockblue.kmpble.ble.NativeBleEvent
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
  private var _nativeEventBus = MutableSharedFlow<NativeBleEvent>()

  fun nativeEventBus() = _nativeEventBus as SharedFlow<NativeBleEvent>

  private var _eventBus = MutableSharedFlow<BleResponse>()

  fun eventBus() = _eventBus as SharedFlow<BleResponse>

  private fun publishEvent(event: NativeBleEvent) {
    coroutineScope.launch {
      _nativeEventBus.emit(event)
      if (event.toBleResponse() !is BleResponse.Error) {
        _eventBus.emit(event.toBleResponse())
      }
    }
  }

  @ObjCSignatureOverride
  override fun peripheral(
    peripheral: CBPeripheral,
    didDiscoverIncludedServicesForService: CBService,
    error: NSError?,
  ) {
    publishEvent(
      OnServicesForServiceDiscovered(
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
      OnCharacteristicWrite(
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
      OnDescriptorWrite(
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
      OnServicesDiscovered(
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
      OnCharacteristicsDiscovered(
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
      OnDescriptorsDiscovered(
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
      OnCharacteristicUpdated(
        peripheral = peripheral,
        characteristic = didUpdateValueForCharacteristic,
      ),
    )
    publishEvent(
      OnCharacteristicRead(
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
      OnDescriptorRead(
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
      OnReadRemoteRssi(
        peripheral = peripheral,
        didReadRSSI = didReadRSSI,
      ),
    )
  }
}
