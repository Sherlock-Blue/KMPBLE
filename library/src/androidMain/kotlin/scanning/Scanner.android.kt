package com.sherlockblue.kmpble.scanning

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.content.Context
import com.sherlockblue.kmpble.peripheral.Peripheral
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
actual class Scanner(
  private val context: Context,
  coroutineScope: CoroutineScope,
) {
  private val bluetoothLeScanner = (context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager).adapter.bluetoothLeScanner

  private val _scannedResults: MutableSharedFlow<ScannedResult> = MutableSharedFlow<ScannedResult>()

  actual fun scanResults(): Flow<ScannedResult> = _scannedResults as Flow<ScannedResult>

  private var isScanning = false // BluetoothLeScanner does not expose its scanning state

  actual suspend fun start() {
    if (!isScanning) {
      isScanning = true
      bluetoothLeScanner.startScan(scannerCallback)
    }
  }

  actual suspend fun stop() {
    isScanning = false
    bluetoothLeScanner.stopScan(scannerCallback) // Must be the same instance passed in startScan
  }

  private val scannerCallback =
    object : ScanCallback() {
      override fun onScanResult(
        callbackType: Int,
        result: android.bluetooth.le.ScanResult?,
      ) {
        result!!.let { scannedResult ->
          scannedResult.scanRecord!!.bytes!!.let { scannedRecord ->
            scannedResult.device!!.let { scannedDevice ->
              coroutineScope.launch {
                _scannedResults.emit(
                  ScannedResult(
                    peripheral =
                      Peripheral(
                        device = scannedDevice,
                        coroutineScope = coroutineScope,
                        context = context,
                      ),
                    advertisingData = advertisementDataFromScanResult(scannedResult),
                    rssi = scannedResult.rssi,
                  ),
                )
              }
            }
          }
        }
      }

      override fun onScanFailed(errorCode: Int) {
        isScanning = false
        bluetoothLeScanner.stopScan(this) // Must be the same instance passed in startScan
      }
    }
}
