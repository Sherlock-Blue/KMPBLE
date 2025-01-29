package com.sherlockblue.kmpble.scanning

import com.sherlockblue.kmpble.fixtures.DEFAULT_ADVERTISING_DATA
import com.sherlockblue.kmpble.fixtures.mockCBPeripheral
import kotlin.test.Test
import kotlin.test.assertEquals

class AdvertisementDataTest {
  @Test
  fun `parseAdvertisementData maps Advertisement Dictionary correctly to AdvertisementData`() {
    // Arrange
    // Mocked Fixtures
    val testCBPeripheral = mockCBPeripheral

    // Act
    val advertisementData = testCBPeripheral.parseAdvertisementData(DEFAULT_ADVERTISING_DATA.build())

    // Assert
    assertEquals(advertisementData.name, DEFAULT_ADVERTISING_DATA.name)
    assertEquals(advertisementData.txPowerLevel, DEFAULT_ADVERTISING_DATA.txPowerLevel)
    assertEquals(advertisementData.isConnectable, DEFAULT_ADVERTISING_DATA.isConnectable)
    assertEquals(advertisementData.serviceUuids?.first(), DEFAULT_ADVERTISING_DATA.serviceUuids?.first()?.UUIDString())
  }
}
