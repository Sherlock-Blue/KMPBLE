package com.sherlockblue.kmpble.ble.fixtures

import android.app.PendingIntent
import android.bluetooth.BluetoothGatt
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanSettings
import io.mockk.every
import io.mockk.mockkClass

class MockBluetoothLeScanner {
  class Builder() {
    private var mockScannerResults = listOf<MockScannerScanResult>()

    fun setScannerResults(newScannerResults: List<MockScannerScanResult>): Builder {
      this.mockScannerResults = newScannerResults
      return this
    }

    fun build(): BluetoothLeScanner =
      mockkClass<BluetoothLeScanner>(BluetoothLeScanner::class).apply {
        val scannerResults = mockScannerResults

        every { startScan(any<ScanCallback>()) } answers {
          val scanCallback = firstArg<ScanCallback>()
          if (scannerResults.isNotEmpty()) {
            scannerResults.forEach { scannerScanResult ->
              scanCallback.onScanResult(scannerScanResult.callbackType, scannerScanResult.result)
            }
          } else {
            scanCallback.onScanFailed(0)
          }
        }

        every { startScan(any(), any<ScanSettings>(), any<ScanCallback>()) } answers {
          val scanCallback = thirdArg<ScanCallback>()
          scannerResults.forEach { scannerScanResult ->
            scanCallback.onScanResult(scannerScanResult.callbackType, scannerScanResult.result)
          }
        }

        every { startScan(any(), any<ScanSettings>(), any<PendingIntent>()) } answers {
          BluetoothGatt.GATT_SUCCESS
        }

        every { stopScan(any<ScanCallback>()) } answers {
          // noop
        }

        every { stopScan(any<PendingIntent>()) } answers {
          // noop
        }
      }
  }

  data class MockScannerScanResult(
    val callbackType: Int,
    val result: android.bluetooth.le.ScanResult?,
  )
}
