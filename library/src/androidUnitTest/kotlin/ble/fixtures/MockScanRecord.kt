package com.sherlockblue.kmpble.ble.fixtures

import android.bluetooth.le.ScanRecord
import android.os.ParcelUuid
import android.util.SparseArray
import io.mockk.every
import io.mockk.mockkClass

class MockScanRecord {
  class Builder {
    private var mockDeviceName: String = "Mock Device Name"
    private var mockAdvertiseFlags: Int = 0
    private var mockTxPowerLevel: Int = 0
    private var mockBytes: ByteArray = byteArrayOf()
    private var mockManufacturerSpecificData: SparseArray<ByteArray> = SparseArray<ByteArray>()
    private var mockServiceData: Map<ParcelUuid, ByteArray> = mapOf()
    private var mockServiceUUIDs: List<ParcelUuid> = listOf()

    fun setDeviceName(newDeviceName: String): Builder {
      this.mockDeviceName = newDeviceName
      return this
    }

    fun setAdvertiseFlags(newAdvertiseFlags: Int): Builder {
      this.mockAdvertiseFlags = newAdvertiseFlags
      return this
    }

    fun setTxPowerLevel(newTxPowerLevel: Int): Builder {
      this.mockTxPowerLevel = newTxPowerLevel
      return this
    }

    fun setBytes(newBytes: ByteArray): Builder {
      this.mockBytes = newBytes
      return this
    }

    fun setManufacturerData(newManufacturerData: SparseArray<ByteArray>): Builder {
      this.mockManufacturerSpecificData = newManufacturerData
      return this
    }

    fun setServiceData(newServiceData: Map<ParcelUuid, ByteArray>): Builder {
      this.mockServiceData = newServiceData
      return this
    }

    fun build(): ScanRecord =
      mockkClass<ScanRecord>(ScanRecord::class).apply {
        val mockDeviceName: String = mockDeviceName
        val mockAdvertiseFlags: Int = mockAdvertiseFlags
        val mockBytes: ByteArray = mockBytes
        val mockTxPowerLevel: Int = mockTxPowerLevel
        val mockManufacturerSpecificData: SparseArray<ByteArray> = mockManufacturerSpecificData
        val mockServiceData: Map<ParcelUuid, ByteArray> = mockServiceData
        val mockServiceUUIDs: List<ParcelUuid> = mockServiceUUIDs

        every { deviceName } returns mockDeviceName
        every { bytes } returns mockBytes
        every { advertiseFlags } returns mockAdvertiseFlags
        every { txPowerLevel } returns mockTxPowerLevel
        every { manufacturerSpecificData } returns mockManufacturerSpecificData
        every { manufacturerSpecificData.size() } returns 1
        every { manufacturerSpecificData.valueAt(any()) } returns
          byteArrayOf(
            0x1A.toByte(),
            0x18.toByte(),
            0x10.toByte(),
            0x20.toByte(),
            0x30.toByte(),
            0x40.toByte(),
          )
        every { serviceData } returns mockServiceData
        every { serviceUuids } returns mockServiceUUIDs
      }
  }
}
