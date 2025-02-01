package com.sherlockblue.kmpble.scanning

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.content.Context
import ble.fixtures.MockSparseArray
import com.sherlockblue.kmpble.ble.fixtures.DEFAULT_ADVERTISING_DATA
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothAdapter
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothDevice
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothLeScanner
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothManager
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothSystemContext
import com.sherlockblue.kmpble.ble.fixtures.MockScanRecord
import com.sherlockblue.kmpble.ble.fixtures.MockScanResult
import com.sherlockblue.kmpble.ble.fixtures.TEST_RSSI
import com.sherlockblue.kmpble.ble.fixtures.mockServiceData
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class ScannerTest {
  @Test
  fun `start() function calls the startScan() function from the BluetoothLeScanner`() =
    runTest {
      launch {
        // Arrange
        // Mocked Fixtures
        val mockBytes = DEFAULT_ADVERTISING_DATA
        val mockScanRecord =
          MockScanRecord.Builder()
            .setDeviceName("Test Name")
            .setTxPowerLevel(TEST_RSSI)
            .setServiceData(mockServiceData)
            .setManufacturerData(MockSparseArray.Builder().build())
            .setBytes(mockBytes)
            .build()
        val mockScanResult =
          MockScanResult.Builder()
            .setScanRecord(mockScanRecord)
            .setDevice(MockBluetoothDevice.Builder().build())
            .setRssi(mockk<Int>(relaxed = true))
            .build()
        val mockBluetoothLeScanner: BluetoothLeScanner =
          MockBluetoothLeScanner.Builder()
            .setScannerResults(listOf(MockBluetoothLeScanner.ScannerScanResult(callbackType = 0, result = mockScanResult)))
            .build()
        val mockBluetoothAdapter: BluetoothAdapter =
          MockBluetoothAdapter.Builder()
            .setBluetoothLeScanner(mockBluetoothLeScanner)
            .build()
        val mockBluetoothManager: BluetoothManager =
          MockBluetoothManager.Builder()
            .setBluetoothAdapter(mockBluetoothAdapter)
            .build()
        val mockContext: Context =
          MockBluetoothSystemContext.Builder()
            .setBluetoothManager(mockBluetoothManager)
            .build()

        // Prepare object under test
        val scanner = Scanner(context = mockContext, coroutineScope = this)

        // Act
        scanner.start()

        // Assert
        verify { mockBluetoothLeScanner.startScan(any<ScanCallback>()) }
      }.join()
    }

  @Test
  fun `stop() function calls the stopScan() function from the BluetoothLeScanner`() =
    runTest {
      launch {
        // Arrange
        // Mocked Fixtures
        val mockScanResult =
          MockScanResult.Builder()
            .setScanRecord(MockScanRecord.Builder().build())
            .setDevice(MockBluetoothDevice.Builder().build())
            .setRssi(mockk<Int>(relaxed = true))
            .build()
        val mockBluetoothLeScanner: BluetoothLeScanner =
          MockBluetoothLeScanner.Builder()
            .setScannerResults(listOf(MockBluetoothLeScanner.ScannerScanResult(callbackType = 0, result = mockScanResult)))
            .build()
        val mockBluetoothAdapter: BluetoothAdapter =
          MockBluetoothAdapter.Builder()
            .setBluetoothLeScanner(mockBluetoothLeScanner)
            .build()
        val mockBluetoothManager: BluetoothManager =
          MockBluetoothManager.Builder()
            .setBluetoothAdapter(mockBluetoothAdapter)
            .build()
        val mockContext: Context =
          MockBluetoothSystemContext.Builder()
            .setBluetoothManager(mockBluetoothManager)
            .build()

        // Prepare object under test
        val scanner = Scanner(context = mockContext, coroutineScope = this)

        // Act
        scanner.start()
        scanner.stop()

        // Assert
        verify { mockBluetoothLeScanner.stopScan(any<ScanCallback>()) }
      }.join()
    }

  @Test
  fun `start() function calls the startScan() function from the BluetoothLeScanner only once before stop()`() =
    runTest {
      launch {
        // Arrange
        // Mocked Fixtures
        val mockScanResult =
          MockScanResult.Builder()
            .setScanRecord(MockScanRecord.Builder().build())
            .setDevice(MockBluetoothDevice.Builder().build())
            .setRssi(mockk<Int>(relaxed = true))
            .build()
        val mockBluetoothLeScanner: BluetoothLeScanner =
          MockBluetoothLeScanner.Builder()
            .setScannerResults(listOf(MockBluetoothLeScanner.ScannerScanResult(callbackType = 0, result = mockScanResult)))
            .build()
        val mockBluetoothAdapter: BluetoothAdapter =
          MockBluetoothAdapter.Builder()
            .setBluetoothLeScanner(mockBluetoothLeScanner)
            .build()
        val mockBluetoothManager: BluetoothManager =
          MockBluetoothManager.Builder()
            .setBluetoothAdapter(mockBluetoothAdapter)
            .build()
        val mockContext: Context =
          MockBluetoothSystemContext.Builder()
            .setBluetoothManager(mockBluetoothManager)
            .build()

        // Prepare object under test
        val scanner = Scanner(context = mockContext, coroutineScope = this)

        // Act
        scanner.start()
        scanner.start()

        // Assert
        verify(exactly = 1) { mockBluetoothLeScanner.startScan(any<ScanCallback>()) }
      }.join()
    }

  @Test
  fun `start() function calls the startScan() function from the BluetoothLeScanner again after stop()`() =
    runTest {
      launch {
        // Arrange
        // Mocked Fixtures
        val mockScanResult =
          MockScanResult.Builder()
            .setScanRecord(MockScanRecord.Builder().build())
            .setDevice(MockBluetoothDevice.Builder().build())
            .setRssi(mockk<Int>(relaxed = true))
            .build()
        val mockBluetoothLeScanner: BluetoothLeScanner =
          MockBluetoothLeScanner.Builder()
            .setScannerResults(listOf(MockBluetoothLeScanner.ScannerScanResult(callbackType = 0, result = mockScanResult)))
            .build()
        val mockBluetoothAdapter: BluetoothAdapter =
          MockBluetoothAdapter.Builder()
            .setBluetoothLeScanner(mockBluetoothLeScanner)
            .build()
        val mockBluetoothManager: BluetoothManager =
          MockBluetoothManager.Builder()
            .setBluetoothAdapter(mockBluetoothAdapter)
            .build()
        val mockContext: Context =
          MockBluetoothSystemContext.Builder()
            .setBluetoothManager(mockBluetoothManager)
            .build()

        // Prepare object under test
        val scanner = Scanner(context = mockContext, coroutineScope = this)

        // Act
        scanner.start()
        scanner.stop()
        scanner.start()

        // Assert
        verify(exactly = 2) { mockBluetoothLeScanner.startScan(any<ScanCallback>()) }
      }.join()
    }

  @Test
  fun `start() function calls the startScan() function from the BluetoothLeScanner again after scanning error`() =
    runTest {
      launch {
        // Arrange
        // Mocked Fixtures
        val mockScanResult =
          MockScanResult.Builder()
            .setScanRecord(MockScanRecord.Builder().build())
            .setDevice(MockBluetoothDevice.Builder().build())
            .setRssi(mockk<Int>(relaxed = true))
            .build()
        val mockBluetoothLeScanner: BluetoothLeScanner =
          MockBluetoothLeScanner.Builder()
            .build()
        val mockBluetoothAdapter: BluetoothAdapter =
          MockBluetoothAdapter.Builder()
            .setBluetoothLeScanner(mockBluetoothLeScanner)
            .build()
        val mockBluetoothManager: BluetoothManager =
          MockBluetoothManager.Builder()
            .setBluetoothAdapter(mockBluetoothAdapter)
            .build()
        val mockContext: Context =
          MockBluetoothSystemContext.Builder()
            .setBluetoothManager(mockBluetoothManager)
            .build()

        // Prepare object under test
        val scanner = Scanner(context = mockContext, coroutineScope = this)

        // Act
        scanner.start()
        scanner.start()

        // Assert
        verify(exactly = 2) { mockBluetoothLeScanner.startScan(any<ScanCallback>()) }
      }.join()
    }

  @Test
  fun `stopScan() function from the BluetoothLeScanner gets called after scanning error`() =
    runTest {
      launch {
        // Arrange
        // Mocked Fixtures
        val mockScanResult =
          MockScanResult.Builder()
            .setScanRecord(MockScanRecord.Builder().build())
            .setDevice(MockBluetoothDevice.Builder().build())
            .setRssi(mockk<Int>(relaxed = true))
            .build()
        val mockBluetoothLeScanner: BluetoothLeScanner =
          MockBluetoothLeScanner.Builder()
            .build()
        val mockBluetoothAdapter: BluetoothAdapter =
          MockBluetoothAdapter.Builder()
            .setBluetoothLeScanner(mockBluetoothLeScanner)
            .build()
        val mockBluetoothManager: BluetoothManager =
          MockBluetoothManager.Builder()
            .setBluetoothAdapter(mockBluetoothAdapter)
            .build()
        val mockContext: Context =
          MockBluetoothSystemContext.Builder()
            .setBluetoothManager(mockBluetoothManager)
            .build()

        // Prepare object under test
        val scanner = Scanner(context = mockContext, coroutineScope = this)

        // Act
        scanner.start()

        // Assert
        verify { mockBluetoothLeScanner.stopScan(any<ScanCallback>()) }
      }.join()
    }

  @Test
  fun `Scanner returns scanned result with correct rssi`() =
    runTest {
      launch {
        // Arrange
        // Mocked Fixtures
        val mockRssi = TEST_RSSI
        val mockScanResult =
          MockScanResult.Builder()
            .setScanRecord(MockScanRecord.Builder().build())
            .setDevice(MockBluetoothDevice.Builder().build())
            .setRssi(mockRssi)
            .build()
        val mockBluetoothLeScanner: BluetoothLeScanner =
          MockBluetoothLeScanner.Builder()
            .setScannerResults(listOf(MockBluetoothLeScanner.ScannerScanResult(callbackType = 0, result = mockScanResult)))
            .build()
        val mockBluetoothAdapter: BluetoothAdapter =
          MockBluetoothAdapter.Builder()
            .setBluetoothLeScanner(mockBluetoothLeScanner)
            .build()
        val mockBluetoothManager: BluetoothManager =
          MockBluetoothManager.Builder()
            .setBluetoothAdapter(mockBluetoothAdapter)
            .build()
        val mockContext: Context =
          MockBluetoothSystemContext.Builder()
            .setBluetoothManager(mockBluetoothManager)
            .build()

        // Prepare object under test
        val scanner = Scanner(context = mockContext, coroutineScope = this)

        // Act
        scanner.start()

        // Assert
        scanner.scanResults().collect { scanResult ->
          Assert.assertTrue(scanResult.rssi == mockRssi)
          scanner.stop()
          this.cancel()
        }
      }.join()
    }

  @Test
  fun `Scanner returns scanned result with correct scanned data`() =
    runTest {
      launch {
        // Arrange
        // Mocked Fixtures
        val mockBytes = DEFAULT_ADVERTISING_DATA
        val mockScanRecord =
          MockScanRecord.Builder()
            .setDeviceName("Test Name")
            .setTxPowerLevel(TEST_RSSI)
            .setServiceData(mockServiceData)
            .setManufacturerData(MockSparseArray.Builder().build())
            .setBytes(mockBytes)
            .build()
        val mockScanResult =
          MockScanResult.Builder()
            .setScanRecord(mockScanRecord)
            .setDevice(MockBluetoothDevice.Builder().build())
            .setRssi(mockk<Int>(relaxed = true))
            .build()
        val mockBluetoothLeScanner: BluetoothLeScanner =
          MockBluetoothLeScanner.Builder()
            .setScannerResults(listOf(MockBluetoothLeScanner.ScannerScanResult(callbackType = 0, result = mockScanResult)))
            .build()
        val mockBluetoothAdapter: BluetoothAdapter =
          MockBluetoothAdapter.Builder()
            .setBluetoothLeScanner(mockBluetoothLeScanner)
            .build()
        val mockBluetoothManager: BluetoothManager =
          MockBluetoothManager.Builder()
            .setBluetoothAdapter(mockBluetoothAdapter)
            .build()
        val mockContext: Context =
          MockBluetoothSystemContext.Builder()
            .setBluetoothManager(mockBluetoothManager)
            .build()

        // Prepare object under test
        val scanner = Scanner(context = mockContext, coroutineScope = this)

        // Act
        scanner.start()

        // Assert
        scanner.scanResults().collect { scanResult ->
          Assert.assertTrue(scanResult.advertisingData.name == "Test Name")
          Assert.assertTrue(scanResult.advertisingData.txPowerLevel == TEST_RSSI)
          scanner.stop()
          this.cancel()
        }
      }.join()
    }
}
