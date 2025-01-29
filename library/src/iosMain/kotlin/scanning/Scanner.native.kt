package com.sherlockblue.kmpble.scanning

import com.sherlockblue.kmpble.callbacks.BleEvent
import com.sherlockblue.kmpble.callbacks.CentralManagerCallbacks
import com.sherlockblue.kmpble.peripheral.Peripheral
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerStatePoweredOn

actual class Scanner(
  private val coroutineScope: CoroutineScope,
) {
  private val _scanResults = MutableSharedFlow<ScannedResult>()
  private var eventBusJob: Job? = null

  internal var centralManager: CBCentralManager = CBCentralManager(CentralManagerCallbacks(coroutineScope), null, null)

  actual fun scanResults(): Flow<ScannedResult> = _scanResults

  private fun launchEventBusJob(coroutineScope: CoroutineScope) {
    eventBusJob =
      coroutineScope.launch {
        (centralManager.delegate as CentralManagerCallbacks).eventBus().collect { event ->
          if (event is BleEvent.OnPeripheralDiscovered) {
            _scanResults.emit(
              ScannedResult(
                peripheral =
                  Peripheral(
                    coroutineScope = coroutineScope,
                    centralManager = centralManager,
                    peripheral = event.peripheral,
                  ),
                advertisingData = event.peripheral.parseAdvertisementData(event.advertisementData),
                rssi = event.RSSI.intValue,
              ),
            )
          }
        }
      }
  }

  actual suspend fun start() {
    if (centralManager.state == CBCentralManagerStatePoweredOn) {
      launchEventBusJob(coroutineScope)
      eventBusJob?.start()
      centralManager.scanForPeripheralsWithServices(null, null)
    }
  }

  actual suspend fun stop() {
    eventBusJob?.cancel()
    centralManager.stopScan()
  }
}
