package com.sherlockblue.kmpble.ble.fixtures

import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanRecord
import android.bluetooth.le.ScanResult
import io.mockk.every
import io.mockk.mockkClass

class MockScanResult {
  class Builder {
    private var mockScanRecord: ScanRecord = MockScanRecord.Builder().build()
    private var mockDevice: BluetoothDevice = MockBluetoothDevice.Builder().build()
    private var mockRssi: Int = 0
    private var mockPeriodicAdvertisingInterval: Int = 0

    fun setScanRecord(newScanRecord: ScanRecord): Builder {
      this.mockScanRecord = newScanRecord
      return this
    }

    fun setDevice(newDevice: BluetoothDevice): Builder {
      this.mockDevice = newDevice
      return this
    }

    fun setRssi(newRssi: Int): Builder {
      this.mockRssi = newRssi
      return this
    }

    fun setPeriodicAdvertisingInterval(newPeriodicAdvertisingInterval: Int): Builder {
      this.mockPeriodicAdvertisingInterval = newPeriodicAdvertisingInterval
      return this
    }

    fun build(): ScanResult =
      mockkClass<ScanResult>(ScanResult::class).apply {
        val mockScanRecord: ScanRecord = mockScanRecord
        val mockDevice: BluetoothDevice = mockDevice
        val mockRssi: Int = mockRssi
        val mockPeriodicAdvertisingInterval: Int = mockPeriodicAdvertisingInterval

        every { scanRecord } returns mockScanRecord
        every { device } returns mockDevice
        every { rssi } returns mockRssi
        every { periodicAdvertisingInterval } returns mockPeriodicAdvertisingInterval
      }
  }
}
