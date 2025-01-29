package com.sherlockblue.kmpble.callbacks

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBPeripheral
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import platform.darwin.NSObject

class CentralManagerCallbacks(private val coroutineScope: CoroutineScope) : NSObject(), CBCentralManagerDelegateProtocol {
  private var _eventBus = MutableSharedFlow<BleEvent>()

  fun eventBus() = _eventBus as SharedFlow<BleEvent>

  private fun publishEvent(event: BleEvent) {
    coroutineScope.launch {
      _eventBus.emit(event)
    }
  }

  override fun centralManager(
    central: CBCentralManager,
    didDiscoverPeripheral: CBPeripheral,
    advertisementData: Map<Any?, *>,
    RSSI: NSNumber,
  ) {
    publishEvent(
      BleEvent.OnPeripheralDiscovered(
        central = central,
        peripheral = didDiscoverPeripheral,
        advertisementData = advertisementData,
        RSSI = RSSI,
      ),
    )
  }

  override fun centralManager(
    central: CBCentralManager,
    didConnectPeripheral: CBPeripheral,
  ) {
    publishEvent(
      BleEvent.OnPeripheralConnect(
        central = central,
        peripheral = didConnectPeripheral,
      ),
    )
  }

  override fun centralManager(
    central: CBCentralManager,
    didDisconnectPeripheral: CBPeripheral,
    error: NSError?,
  ) {
    publishEvent(
      BleEvent.OnPeripheralDisconnect(
        central = central,
        peripheral = didDisconnectPeripheral,
      ),
    )
  }

  override fun centralManagerDidUpdateState(central: CBCentralManager) {
    publishEvent(BleEvent.OnServiceChanged(central = central))
  }
}
