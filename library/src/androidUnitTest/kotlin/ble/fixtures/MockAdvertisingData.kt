package com.sherlockblue.kmpble.ble.fixtures

import ble.fixtures.MockParcelUUID
import com.sherlockblue.kmpble.scanning.ManufacturerData
import com.sherlockblue.kmpble.scanning.ServiceData

class MockAdvertisingData {
  class Builder() {
    private var flags: ByteArray? = null

    private var name: String? = null
    private var shortenedName: String? = null
    private var deviceAddress: String? = null

    private var txPowerLevel: Int? = null
    private var manufacturerData: ManufacturerData? = null
    private var appearance: Int? = null

    private var serviceData: List<ServiceData>? = null
    private var incomplete16BitUUIDs: List<String>? = null
    private var complete16BitUUIDs: List<String>? = null
    private var incomplete32BitUUIDs: List<String>? = null
    private var complete32BitUUIDs: List<String>? = null
    private var incomplete128BitUUIDs: List<String>? = null
    private var complete128BitUUIDs: List<String>? = null

    private var advertisingInterval: Int? = null
    private var publicTargetAddress: String? = null
    private var randomTargetAddress: String? = null
    private var leRole: Int? = null
    private var leSecureConnectionsOOB: ByteArray? = null
    private var leSupportedFeatures: ByteArray? = null
    private var uri: String? = null

    fun setFlags(newFlags: ByteArray): Builder {
      this.flags = newFlags
      return this
    }

    fun setShortenedName(newShortenedName: String): Builder {
      this.shortenedName = newShortenedName
      return this
    }

    fun setDeviceAddress(newDeviceAddress: String): Builder {
      this.deviceAddress = newDeviceAddress
      return this
    }

    fun setTxPowerLevel(newTxPowerLevel: Int): Builder {
      this.txPowerLevel = newTxPowerLevel
      return this
    }

    fun setName(newName: String): Builder {
      this.name = newName
      return this
    }

    fun setAppearance(newAppearance: Int): Builder {
      this.appearance = newAppearance
      return this
    }

    fun setServiceData(newServiceData: List<ServiceData>): Builder {
      this.serviceData = newServiceData
      return this
    }

    fun setManufacturerData(newManufacturerData: ManufacturerData): Builder {
      this.manufacturerData = newManufacturerData
      return this
    }

    fun setIncomplete16BitUUIDs(newIncomplete16BitUUIDs: List<String>): Builder {
      this.incomplete16BitUUIDs = newIncomplete16BitUUIDs
      return this
    }

    fun setComplete16BitUUIDs(newComplete16BitUUIDs: List<String>): Builder {
      this.complete16BitUUIDs = newComplete16BitUUIDs
      return this
    }

    fun setIncomplete32BitUUIDs(newIncomplete32BitUUIDs: List<String>): Builder {
      this.incomplete32BitUUIDs = newIncomplete32BitUUIDs
      return this
    }

    fun setComplete32BitUUIDs(newComplete32BitUUIDs: List<String>): Builder {
      this.complete32BitUUIDs = newComplete32BitUUIDs
      return this
    }

    fun setIncomplete128BitUUIDs(newIncomplete128BitUUIDs: List<String>): Builder {
      this.incomplete128BitUUIDs = newIncomplete128BitUUIDs
      return this
    }

    fun setComplete128BitUUIDs(newComplete128BitUUIDs: List<String>): Builder {
      this.complete128BitUUIDs = newComplete128BitUUIDs
      return this
    }

    fun setAdvertisingInterval(newAdvertisingInterval: Int): Builder {
      this.advertisingInterval = newAdvertisingInterval
      return this
    }

    fun setPublicTargetAddress(newPublicTargetAddress: String): Builder {
      this.publicTargetAddress = newPublicTargetAddress
      return this
    }

    fun setRandomTargetAddress(newRandomTargetAddress: String): Builder {
      this.randomTargetAddress = newRandomTargetAddress
      return this
    }

    fun setLeRole(newLeRole: Int): Builder {
      this.leRole = newLeRole
      return this
    }

    fun setUri(newUri: String): Builder {
      this.uri = newUri
      return this
    }

    fun setLeSecureConnectionsOOB(newLeSecureConnectionsOOB: ByteArray): Builder {
      this.leSecureConnectionsOOB = newLeSecureConnectionsOOB
      return this
    }

    fun setLeSupportedFeatures(newLeSupportedFeatures: ByteArray): Builder {
      this.leSupportedFeatures = newLeSupportedFeatures
      return this
    }

    data class AdvertisementDataField(val length: Int, val type: Int, val data: ByteArray) {
      fun toByteArray(): ByteArray = byteArrayOf(length.toByte(), type.toByte()) + data

      fun toByteList(): List<Byte> = (byteArrayOf(length.toByte(), type.toByte()) + data).toList()
    }

    fun build(): ByteArray =
      mutableListOf<Byte>().apply {
        flags?.let { it -> addAll(AdvertisementDataField(it.size + 1, 1, it).toByteList()) }
        shortenedName?.let { it -> addAll(AdvertisementDataField(it.length + 1, 8, it.encodeToByteArray()).toByteList()) }
        name?.let { it -> addAll(AdvertisementDataField(it.length + 1, 9, it.encodeToByteArray()).toByteList()) }
        deviceAddress?.let { it -> addAll(AdvertisementDataField(it.length + 1, 27, it.encodeToByteArray()).toByteList()) }
        txPowerLevel?.let { it -> addAll(AdvertisementDataField(2, 10, byteArrayOf(it.toByte())).toByteList()) }
        appearance?.let { it -> addAll(AdvertisementDataField(2, 19, byteArrayOf(it.toByte())).toByteList()) }
        incomplete16BitUUIDs?.let { it ->
          addAll(
            AdvertisementDataField(
              it.size + 1,
              2,
              it.fold(byteArrayOf(), { acc, uuid -> acc + uuid.encodeToByteArray() }),
            ).toByteList(),
          )
        }
        complete16BitUUIDs?.let { it ->
          addAll(
            AdvertisementDataField(
              it.size + 1,
              3,
              it.fold(byteArrayOf(), { acc, uuid -> acc + uuid.encodeToByteArray() }),
            ).toByteList(),
          )
        }
        incomplete32BitUUIDs?.let { it ->
          addAll(
            AdvertisementDataField(
              it.size + 1,
              4,
              it.fold(byteArrayOf(), { acc, uuid -> acc + uuid.encodeToByteArray() }),
            ).toByteList(),
          )
        }
        complete32BitUUIDs?.let { it ->
          addAll(
            AdvertisementDataField(
              it.size + 1,
              5,
              it.fold(byteArrayOf(), { acc, uuid -> acc + uuid.encodeToByteArray() }),
            ).toByteList(),
          )
        }
        incomplete128BitUUIDs?.let { it ->
          addAll(
            AdvertisementDataField(
              it.size + 1,
              6,
              it.fold(byteArrayOf(), { acc, uuid -> acc + uuid.encodeToByteArray() }),
            ).toByteList(),
          )
        }
        complete128BitUUIDs?.let { it ->
          addAll(
            AdvertisementDataField(
              it.size + 1,
              7,
              it.fold(byteArrayOf(), { acc, uuid -> acc + uuid.encodeToByteArray() }),
            ).toByteList(),
          )
        }
        // ToDo Service Data
        // ToDo Manufacturer Data
        publicTargetAddress?.let { it -> addAll(AdvertisementDataField(it.length + 1, 17, it.encodeToByteArray()).toByteList()) }
        randomTargetAddress?.let { it -> addAll(AdvertisementDataField(it.length + 1, 18, it.encodeToByteArray()).toByteList()) }
        advertisingInterval?.let { it -> addAll(AdvertisementDataField(2, 19, byteArrayOf(it.toByte())).toByteList()) }
        leRole?.let { it -> addAll(AdvertisementDataField(2, 11, byteArrayOf(it.toByte())).toByteList()) }
        leSupportedFeatures?.let { it -> addAll(AdvertisementDataField(it.size + 1, 27, it).toByteList()) }
        leSecureConnectionsOOB?.let { it -> addAll(AdvertisementDataField(it.size + 1, 22, it).toByteList()) }
      }.toByteArray()
  }
}

val DEFAULT_ADVERTISING_DATA =
  MockAdvertisingData.Builder()
    .setFlags(byteArrayOf(0.toByte()))
    .setName("Test Name")
    .setShortenedName("Test Shortened Name")
    .setDeviceAddress("Test Device Address")
    .setTxPowerLevel(0)
    .setAdvertisingInterval(0)
    .setUri("Test Uri")
    .setAppearance(0)
    .setComplete16BitUUIDs(listOf(DEFAULT_UUID))
    .setIncomplete16BitUUIDs(listOf(DEFAULT_UUID))
    .setComplete32BitUUIDs(listOf(DEFAULT_UUID))
    .setIncomplete32BitUUIDs(listOf(DEFAULT_UUID))
    .setComplete128BitUUIDs(listOf(DEFAULT_UUID))
    .setIncomplete128BitUUIDs(listOf(DEFAULT_UUID))
    .setLeRole(0)
    .setLeSecureConnectionsOOB(byteArrayOf(0.toByte()))
    .setLeSupportedFeatures(byteArrayOf(0.toByte()))
    .setPublicTargetAddress("Test Public Target Address")
    .setRandomTargetAddress("Test Random Target Address")
    .build()

val mockServiceData =
  mapOf(
    MockParcelUUID.Builder().build() to
      byteArrayOf(
        0x1A.toByte(),
        0x18.toByte(),
        0x10.toByte(),
        0x20.toByte(),
        0x30.toByte(),
        0x40.toByte(),
      ),
  )
