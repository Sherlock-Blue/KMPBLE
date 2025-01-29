package com.sherlockblue.kmpble.fixtures

import platform.CoreBluetooth.CBAdvertisementDataIsConnectable
import platform.CoreBluetooth.CBAdvertisementDataLocalNameKey
import platform.CoreBluetooth.CBAdvertisementDataManufacturerDataKey
import platform.CoreBluetooth.CBAdvertisementDataServiceDataKey
import platform.CoreBluetooth.CBAdvertisementDataServiceUUIDsKey
import platform.CoreBluetooth.CBAdvertisementDataTxPowerLevelKey
import platform.CoreBluetooth.CBUUID
import platform.Foundation.NSData
import utils.toNSData

class MockAdvertisingDictionary() {
  class Builder() {
    var name: String? = null
    var txPowerLevel: Int? = null
    var isConnectable: Boolean? = null
    var manufacturerData: ByteArray? = null
    var serviceUuids: List<CBUUID>? = null
    var serviceData: Map<CBUUID, NSData>? = emptyMap()

    fun setName(newName: String): Builder {
      this.name = newName
      return this
    }

    fun setTxPowerLevel(newTxPowerLevel: Int): Builder {
      this.txPowerLevel = newTxPowerLevel
      return this
    }

    fun setIsConnectable(newIsConnectable: Boolean): Builder {
      this.isConnectable = newIsConnectable
      return this
    }

    fun setManufacturerData(newManufacturerData: ByteArray): Builder {
      this.manufacturerData = newManufacturerData
      return this
    }

    fun setServiceUuids(newServiceUuids: List<String>): Builder {
      this.serviceUuids = newServiceUuids.map { CBUUID.UUIDWithString(it) }
      return this
    }

    fun setServiceData(newServiceData: Map<String, ByteArray>): Builder {
      this.serviceData = newServiceData.map { CBUUID.UUIDWithString(it.key) to it.value.toNSData() }.toMap()
      return this
    }

    fun build(): Map<Any?, *> =
      mutableMapOf<Any?, Any?>().apply {
        name?.let { it -> put(CBAdvertisementDataLocalNameKey, it) }
        txPowerLevel?.let { it -> put(CBAdvertisementDataTxPowerLevelKey, it) }
        isConnectable?.let { it -> put(CBAdvertisementDataIsConnectable, it) }
        manufacturerData?.let { it -> put(CBAdvertisementDataManufacturerDataKey, it) }
        serviceUuids?.let { it -> put(CBAdvertisementDataServiceUUIDsKey, it) }
        serviceData?.let { it -> put(CBAdvertisementDataServiceDataKey, it) }
      }
  }
}

val mockServiceData: ByteArray =
  byteArrayOf(
    0x1A.toByte(),
    0x18.toByte(),
    0x10.toByte(),
    0x20.toByte(),
    0x30.toByte(),
    0x40.toByte(),
  )

val mockManufacturerData: ByteArray =
  byteArrayOf(
    0x4C.toByte(),
    0x00.toByte(),
    0x01.toByte(),
    0x23.toByte(),
    0x45.toByte(),
    0x67.toByte(),
    0x89.toByte(),
  )

val DEFAULT_ADVERTISING_DATA =
  MockAdvertisingDictionary.Builder()
    .setServiceUuids(listOf(DEFAULT_UUID))
    .setServiceData(mapOf(DEFAULT_UUID to mockServiceData))
    .setManufacturerData(mockManufacturerData)
    .setName("Test Name")
    .setTxPowerLevel(0)
    .setIsConnectable(true)
