package com.sherlockblue.kmpble.callbacks

import com.sherlockblue.kmpble.ble.BleResponse
import com.sherlockblue.kmpble.ble.NativeBleEvent
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

typealias PeripheralState = Boolean

class CentralManagerCallbacks(private val coroutineScope: CoroutineScope) : NSObject(), CBCentralManagerDelegateProtocol {
  private var _nativeEventBus = MutableSharedFlow<NativeBleEvent>()

  fun nativeEventBus() = _nativeEventBus as SharedFlow<NativeBleEvent>

  private var _eventBus = MutableSharedFlow<BleResponse>()

  fun eventBus() = _eventBus as SharedFlow<BleResponse>

  private fun publishEvent(event: NativeBleEvent) {
    coroutineScope.launch {
      _nativeEventBus.emit(event)
      event.toBleResponse().let { bleResponse -> _eventBus.emit(bleResponse) }
    }
  }

  override fun centralManager(
    central: CBCentralManager,
    didDiscoverPeripheral: CBPeripheral,
    advertisementData: Map<Any?, *>,
    RSSI: NSNumber,
  ) {
    publishEvent(
      OnPeripheralDiscovered(
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
      OnPeripheralConnect(
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
      OnPeripheralDisconnect(
        central = central,
        peripheral = didDisconnectPeripheral,
      ),
    )
  }

  override fun centralManagerDidUpdateState(central: CBCentralManager) {
    publishEvent(OnServiceChanged(central = central))
  }
}
