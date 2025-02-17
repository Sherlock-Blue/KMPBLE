package com.sherlockblue.kmpble.scanning

import com.sherlockblue.kmpble.callbacks.CentralManagerCallbacks
import com.sherlockblue.kmpble.callbacks.OnPeripheralDiscovered
import com.sherlockblue.kmpble.callbacks.OnServiceChanged
import com.sherlockblue.kmpble.callbacks.PeripheralCallbacks
import com.sherlockblue.kmpble.peripheral.Peripheral
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerStatePoweredOn
import platform.CoreBluetooth.CBManagerState
import platform.CoreBluetooth.CBManagerStateUnknown

actual class Scanner(
  private val coroutineScope: CoroutineScope,
) {
  private val _scanResults = MutableSharedFlow<ScannedResult>()
  actual fun scanResults(): Flow<ScannedResult> = _scanResults

  internal var centralManagerCallbacks =
    CentralManagerCallbacks(coroutineScope).also {
      coroutineScope.launch {
        it.nativeEventBus().collect { event ->
          if (event is OnServiceChanged) {
            centralManagerState = event.central.state
            if (event.central.state == CBCentralManagerStatePoweredOn) {
              start()
            }
          }
        }
      }
    }
  internal var centralManager: CBCentralManager = CBCentralManager(centralManagerCallbacks, null, null)
  private var centralManagerState: CBManagerState = CBManagerStateUnknown

  private var scanningBusJob: Job =
    coroutineScope.launch {
      centralManagerCallbacks.nativeEventBus().collect { event ->
        if (event is OnPeripheralDiscovered) {
          val discoveredRSSI = event.RSSI.intValue
          val discoveredAdvertisementData = event.peripheral.parseAdvertisementData(event.advertisementData)
          val discoveredPeripheral =
            Peripheral(
              coroutineScope = coroutineScope,
              centralManager = event.central,
              peripheral = event.peripheral.apply { this.setDelegate(PeripheralCallbacks(coroutineScope = coroutineScope)) },
            )
          _scanResults.emit(
            ScannedResult(
              peripheral =
              discoveredPeripheral,
              advertisingData = discoveredAdvertisementData,
              rssi = discoveredRSSI,
            ),
          )
        }
      }
    }

  actual fun start() {
    if (centralManagerState == CBCentralManagerStatePoweredOn) {
      scanningBusJob.start()
      centralManager.scanForPeripheralsWithServices(null, null)
    }
  }

  actual fun stop() {
    centralManager.stopScan()
    scanningBusJob.cancel()
  }
}
