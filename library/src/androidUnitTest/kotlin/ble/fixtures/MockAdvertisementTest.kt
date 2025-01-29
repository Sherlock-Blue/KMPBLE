package ble.fixtures

import com.sherlockblue.kmpble.ble.fixtures.MockAdvertisingData
import org.junit.Assert
import org.junit.Test

class MockAdvertisementTest {
  @Test
  fun `MockAdvertisingData correctly populates the name field in the output array`() {
    // Arrange
    // Mocked Fixtures
    val testName = "Test Device Name"

    // Act
    val advertisingData =
      MockAdvertisingData.Builder()
        .setName(testName)
        .build()

    // Assert
    Assert.assertTrue(advertisingData[0] == (testName.length + 1).toByte())
  }
}
