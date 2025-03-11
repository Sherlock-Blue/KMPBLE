package com.sherlockblue.kmpble.scanning

import com.sherlockblue.kmpble.fixtures.MockCBCentralManager
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerStatePoweredOn
import kotlin.test.Test
import kotlin.test.assertTrue

class ScannerTest {
  @Test
  fun `start function calls scanForPeripheralsWithServices in the CBCentralManager exactly once`() =
    runTest {
      launch {
        // Arrange
        // Mocked Fixtures
        val mockCBCentralManager: CBCentralManager = MockCBCentralManager()
        val testScanner =
          Scanner(this).apply {
            centralManager = mockCBCentralManager
            centralManagerState = CBCentralManagerStatePoweredOn
          }

        // Act
        testScanner.start()
        testScanner.stop()

        // Assert
        assertTrue((mockCBCentralManager as MockCBCentralManager).verify(exactly = 1, functionName = "scanForPeripheralsWithServices"))
        cancel()
      }
    }

  @Test
  fun `start function calls scanForPeripheralsWithServices in the CBCentralManager zero times when CBCentralManagerStatePoweredOff`() =
    runTest {
      launch {
        // Arrange
        // Mocked Fixtures
        val mockCBCentralManager: CBCentralManager = MockCBCentralManager()
        val testScanner =
          Scanner(this).apply {
            centralManager = mockCBCentralManager
          }

        // Act
        testScanner.start()
        testScanner.stop()

        // Assert
        assertTrue((mockCBCentralManager as MockCBCentralManager).verify(exactly = 0, functionName = "scanForPeripheralsWithServices"))
        cancel()
      }
    }

  @Test
  fun `stop function calls stopScan in the CBCentralManager exactly once`() =
    runTest {
      launch {
        // Arrange
        // Mocked Fixtures
        val mockCBCentralManager: CBCentralManager = MockCBCentralManager()

        // Prepare object under test
        val testScanner = Scanner(this).apply { centralManager = mockCBCentralManager }

        // Act
        testScanner.stop()

        // Assert
        assertTrue((mockCBCentralManager as MockCBCentralManager).verify(exactly = 1, functionName = "stopScan"))
        cancel()
      }
    }
}
