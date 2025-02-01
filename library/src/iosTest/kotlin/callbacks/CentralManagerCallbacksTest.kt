package com.sherlockblue.kmpble.callbacks

import com.sherlockblue.kmpble.fixtures.DEFAULT_ADVERTISING_DATA
import com.sherlockblue.kmpble.fixtures.DEFAULT_TEST_RUNS
import com.sherlockblue.kmpble.fixtures.MockCBCentralManager
import com.sherlockblue.kmpble.fixtures.TEST_RSSI
import com.sherlockblue.kmpble.fixtures.mockCBPeripheral
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import platform.CoreBluetooth.CBCentralManager
import platform.Foundation.NSNumber
import kotlin.test.Test
import kotlin.test.assertTrue

class CentralManagerCallbacksTest {
  @Test
  fun `didConnectPeripheral callback emits an OnPeripheralConnect BleEvent`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val mockCentralManager: CBCentralManager = MockCBCentralManager()
        val mockPeripheralCallbacks = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(mockPeripheralCallbacks)

        // Prepare object under test
        val centralManagerCallbacks = CentralManagerCallbacks(this)

        // Execute Test
        centralManagerCallbacks.centralManager(
          central = mockCentralManager,
          didConnectPeripheral = mockCBPeripheral,
        )

        // Assert
        assertTrue(centralManagerCallbacks.nativeEventBus().first() is OnPeripheralConnect)
      }
    }

  @Test
  fun `didDiscoverPeripheral callback emits an OnPeripheralDiscovered BleEvent`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val mockCentral: CBCentralManager = MockCBCentralManager()
        val mockPeripheralCallbacks = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(mockPeripheralCallbacks)
        val mockAdvertisementData: Map<Any?, *> = DEFAULT_ADVERTISING_DATA.build()
        val mockRSSI: NSNumber = TEST_RSSI

        // Prepare object under test
        val centralManagerCallbacks = CentralManagerCallbacks(this)

        // Execute Test
        centralManagerCallbacks.centralManager(
          central = mockCentral,
          didDiscoverPeripheral = mockCBPeripheral,
          advertisementData = mockAdvertisementData,
          RSSI = mockRSSI,
        )

        // Assert
        assertTrue(centralManagerCallbacks.nativeEventBus().first() is OnPeripheralDiscovered)
      }
    }

  @Test
  fun `didDisconnectPeripheral callback emits an OnPeripheralDisconnect BleEvent`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val mockCentral: CBCentralManager = MockCBCentralManager()
        val mockPeripheralCallbacks = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(mockPeripheralCallbacks)

        // Prepare object under test
        val centralManagerCallbacks = CentralManagerCallbacks(this)

        // Execute Test
        centralManagerCallbacks.centralManager(
          central = mockCentral,
          didDisconnectPeripheral = mockCBPeripheral,
          error = null,
        )

        // Evaluate Result
        assertTrue(centralManagerCallbacks.nativeEventBus().first() is OnPeripheralDisconnect)
      }
    }

  @Test
  fun `didUpdateState callback emits an OnServiceChanged BleEvent`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val mockCentral: CBCentralManager = MockCBCentralManager()

        // Prepare object under test
        val centralManagerCallbacks = CentralManagerCallbacks(this)

        // Execute Test
        centralManagerCallbacks.centralManagerDidUpdateState(central = mockCentral)

        // Evaluate Result
        assertTrue(centralManagerCallbacks.nativeEventBus().first() is OnServiceChanged)
      }
    }
}
