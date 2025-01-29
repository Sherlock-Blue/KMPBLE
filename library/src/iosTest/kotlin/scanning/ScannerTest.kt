package com.sherlockblue.kmpble.scanning

import com.sherlockblue.kmpble.callbacks.CentralManagerCallbacks
import com.sherlockblue.kmpble.callbacks.PeripheralCallbacks
import com.sherlockblue.kmpble.fixtures.MockCBCentralManager
import com.sherlockblue.kmpble.fixtures.MockCBPeripheral
import com.sherlockblue.kmpble.fixtures.TEST_RSSI
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import platform.CoreBluetooth.CBCentralManager
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ScannerTest {
  @Test
  fun `start function calls scanForPeripheralsWithServices in the CBCentralManager exactly once`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockCBCentralManager: CBCentralManager = MockCBCentralManager()
      val testScanner = Scanner(this).apply { centralManager = mockCBCentralManager }

      // Act
      testScanner.start()
      testScanner.stop()

      // Assert
      assertTrue((mockCBCentralManager as MockCBCentralManager).verify(exactly = 1, functionName = "scanForPeripheralsWithServices"))
    }

  @Test
  fun `stop function calls stopScan in the CBCentralManager exactly once`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockCBCentralManager: CBCentralManager = MockCBCentralManager()

      // Prepare object under test
      val testScanner = Scanner(this).apply { centralManager = mockCBCentralManager }

      // Act
      testScanner.stop()

      // Assert
      assertTrue((mockCBCentralManager as MockCBCentralManager).verify(exactly = 1, functionName = "stopScan"))
    }

  @Test
  fun `start function emits OnPeripheralDiscovered BleEvent`() =
    runTest {
      launch {
        // Arrange
        // Mocked Fixtures
        val centralManagerDelegates: CentralManagerCallbacks = CentralManagerCallbacks(this)
        val mockRSSI = TEST_RSSI
        val mockCBCentralManager: CBCentralManager =
          MockCBCentralManager(centralManagerDelegateOverride = centralManagerDelegates).apply {
            setMockDiscoveredPeripherals(
              listOf(
                MockCBCentralManager.MockDiscoveredPeripheral(
                  didDiscoverPeripheral =
                    MockCBPeripheral(
                      peripheralDelegateOverride = PeripheralCallbacks(this@launch),
                    ),
                  RSSI = mockRSSI,
                ),
              ),
            )
          }

        // Prepare object under test
        val testScanner = Scanner(this).apply { centralManager = mockCBCentralManager }

        // Act
        testScanner.start()

        // Assert
        testScanner.scanResults().collect { scannedResult ->
          assertEquals(scannedResult.rssi, mockRSSI.intValue)
          testScanner.stop()
          this.cancel()
        }
      }.join()
    }
}
