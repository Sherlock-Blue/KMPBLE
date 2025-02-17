package com.sherlockblue.kmpble.scanning

import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
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

  actual fun start() {
    if (!isScanning) {
      isScanning = true
      bluetoothLeScanner.startScan(scannerCallback)
    }
  }

  actual fun stop() {
    isScanning = false
    bluetoothLeScanner.stopScan(scannerCallback) // Must be the same instance passed in startScan
  }

  private val scannerCallback =
    object : ScanCallback() {
      @RequiresApi(Build.VERSION_CODES.O)
      override fun onScanResult(
        callbackType: Int,
        result: android.bluetooth.le.ScanResult,
      ) {
        coroutineScope.launch {
          _scannedResults.emit(
            ScannedResult(
              peripheral =
                Peripheral(
                  device = result.device,
                  coroutineScope = coroutineScope,
                  context = context,
                ),
              advertisingData = advertisementDataFromScanResult(result),
              rssi = result.rssi,
            ),
          )
        }
      }

      override fun onScanFailed(errorCode: Int) {
        isScanning = false
        bluetoothLeScanner.stopScan(this) // Must be the same instance passed in startScan
      }
    }
}
