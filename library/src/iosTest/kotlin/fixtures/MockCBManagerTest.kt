package com.sherlockblue.kmpble.fixtures

import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MockCBManagerTest {
  // Test auditing

  @Test
  fun `verify returns count for requested function name`() =
    runTest {
      val mockCBCentralManager = MockCBCentralManager()
      mockCBCentralManager.scanForPeripheralsWithServices(null, null)
      assertTrue(mockCBCentralManager.verify(exactly = 0, functionName = "isScanning"))
      assertTrue(mockCBCentralManager.verify(exactly = 1, functionName = "scanForPeripheralsWithServices"))
    }

  @Test
  fun `verify returns count for requested function name with lambda`() =
    runTest {
      val mockCBCentralManager = MockCBCentralManager()
      mockCBCentralManager.scanForPeripheralsWithServices(null, null)
      assertTrue(mockCBCentralManager.verify(exactly = 0) { "isScanning" })
      assertTrue(mockCBCentralManager.verify(exactly = 1) { "scanForPeripheralsWithServices" })
    }

  @Test
  fun `verify returns number of function calls`() =
    runTest {
      val mockCBCentralManager = MockCBCentralManager()
      mockCBCentralManager.scanForPeripheralsWithServices(null, null)
      mockCBCentralManager.stopScan()
      mockCBCentralManager.scanForPeripheralsWithServices(null, null)
      assertTrue(mockCBCentralManager.verify(exactly = 0) { "isScanning" })
      assertTrue(mockCBCentralManager.verify(exactly = 1) { "stopScan" })
      assertTrue(mockCBCentralManager.verify(exactly = 2) { "scanForPeripheralsWithServices" })
    }

  @Test
  fun `verify returns number of function calls with lambda`() =
    runTest {
      val mockCBCentralManager = MockCBCentralManager()
      mockCBCentralManager.scanForPeripheralsWithServices(null, null)
      mockCBCentralManager.stopScan()
      mockCBCentralManager.scanForPeripheralsWithServices(null, null)
      assertTrue(mockCBCentralManager.verify(exactly = 0, functionName = "isScanning"))
      assertTrue(mockCBCentralManager.verify(exactly = 1, functionName = "stopScan"))
      assertTrue(mockCBCentralManager.verify(exactly = 2, functionName = "scanForPeripheralsWithServices"))
    }

  // Test mocking overrides

  @Test
  fun `setting isScanningOverride to true returns true when isScanning is called`() {
    val mockCBCentralManager = MockCBCentralManager(isScanningOverride = true)
    assertTrue(mockCBCentralManager.isScanning)
  }

  @Test
  fun `isScanning returns false by default`() {
    val mockCBCentralManager = MockCBCentralManager()
    assertFalse(mockCBCentralManager.isScanning)
  }
}
