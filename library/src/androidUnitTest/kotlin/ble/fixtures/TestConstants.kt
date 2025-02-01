package com.sherlockblue.kmpble.ble.fixtures

import ble.fixtures.MockParcelUUID

val TEST_DEVICE_NAME = "TEST DEVICE NAME"
val TEST_DEVICE_ADDRESS = "TEST DEVICE ADDRESS"
val TEST_UUID = "00000000-0000-1000-8000-00805F9B34FB"
val TEST_RSSI = Int.MIN_VALUE // i.e. A number that would never be returned for RSSI
val TEST_STATUS = Int.MAX_VALUE // i.e. A number that would never be returned by GATT

val TEST_SERVICE_DATA =
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

val TEST_ADVERTISING_DATA =
  MockAdvertisingData.Builder()
    .setFlags(byteArrayOf(0.toByte()))
    .setName("Test Name")
    .setShortenedName("Test Shortened Name")
    .setDeviceAddress("Test Device Address")
    .setTxPowerLevel(0)
    .setAdvertisingInterval(0)
    .setUri("Test Uri")
    .setAppearance(0)
    .setComplete16BitUUIDs(listOf(TEST_UUID))
    .setIncomplete16BitUUIDs(listOf(TEST_UUID))
    .setComplete32BitUUIDs(listOf(TEST_UUID))
    .setIncomplete32BitUUIDs(listOf(TEST_UUID))
    .setComplete128BitUUIDs(listOf(TEST_UUID))
    .setIncomplete128BitUUIDs(listOf(TEST_UUID))
    .setLeRole(0)
    .setLeSecureConnectionsOOB(byteArrayOf(0.toByte()))
    .setLeSupportedFeatures(byteArrayOf(0.toByte()))
    .setPublicTargetAddress("Test Public Target Address")
    .setRandomTargetAddress("Test Random Target Address")
    .build()
