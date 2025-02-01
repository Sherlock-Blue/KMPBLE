package com.sherlockblue.kmpble.callbacks

import com.sherlockblue.kmpble.fixtures.DEFAULT_TEST_RUNS
import com.sherlockblue.kmpble.fixtures.MockCBCharacteristic
import com.sherlockblue.kmpble.fixtures.MockCBDescriptor
import com.sherlockblue.kmpble.fixtures.MockCBService
import com.sherlockblue.kmpble.fixtures.mockCBPeripheral
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBService
import kotlin.test.Test
import kotlin.test.assertTrue

class PeripheralCallbacksTest {
  @Test
  fun `didDiscoverIncludedServicesForService callback emits an OnServicesForServiceDiscovered BleEvent`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val mockService: CBService = MockCBService()

        // Prepare object under test
        val peripheralCallbacks = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(peripheralCallbacks)

        // Act
        peripheralCallbacks.peripheral(
          peripheral = mockCBPeripheral,
          didDiscoverIncludedServicesForService = mockService,
          error = null,
        )

        // Assert
        assertTrue(peripheralCallbacks.nativeEventBus().first() is OnServicesForServiceDiscovered)
      }
    }

  @Test
  fun `didDiscoverServices callback emits an OnServicesDiscovered BleEvent`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Prepare object under test
        val peripheralCallbacks = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(peripheralCallbacks)

        // Act
        peripheralCallbacks.peripheral(peripheral = mockCBPeripheral, didDiscoverServices = null)

        // Assert
        assertTrue(peripheralCallbacks.nativeEventBus().first() is OnServicesDiscovered)
      }
    }

  @Test
  fun `didDiscoverCharacteristicsForService callback emits an OnCharacteristicsDiscovered BleEvent`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Prepare object under test
        val peripheralCallbacks = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(peripheralCallbacks)

        // Act
        peripheralCallbacks.peripheral(
          peripheral = mockCBPeripheral,
          didDiscoverCharacteristicsForService = MockCBService(),
          error = null,
        )

        // Assert
        assertTrue(peripheralCallbacks.nativeEventBus().first() is OnCharacteristicsDiscovered)
      }
    }

  @Test
  fun `didDiscoverDescriptorsForCharacteristic callback emits an OnDescriptorsDiscovered BleEvent`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val mockCharacteristic: CBCharacteristic = MockCBCharacteristic()

        // Prepare object under test
        val peripheralCallbacks = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(peripheralCallbacks)

        // Act
        peripheralCallbacks.peripheral(
          peripheral = mockCBPeripheral,
          didDiscoverDescriptorsForCharacteristic = mockCharacteristic,
          error = null,
        )

        // Assert
        assertTrue(peripheralCallbacks.nativeEventBus().first() is OnDescriptorsDiscovered)
      }
    }

  @Test
  fun `didWriteValueForCharacteristic callback emits an OnCharacteristicWrite BleEvent`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val mockCharacteristic: CBCharacteristic = MockCBCharacteristic()

        // Prepare object under test
        val peripheralCallbacks = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(peripheralCallbacks)

        // Act
        peripheralCallbacks.peripheral(
          peripheral = mockCBPeripheral,
          didWriteValueForCharacteristic = mockCharacteristic,
          error = null,
        )

        // Assert
        assertTrue(peripheralCallbacks.nativeEventBus().first() is OnCharacteristicWrite)
      }
    }

  @Test
  fun `didUpdateValueForCharacteristic callback emits an OnCharacteristicUpdated BleEvent`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val peripheralCallbacks = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(peripheralCallbacks)
        val mockCharacteristic: CBCharacteristic = MockCBCharacteristic()

        // Act
        peripheralCallbacks.peripheral(
          peripheral = mockCBPeripheral,
          didUpdateValueForCharacteristic = mockCharacteristic,
          error = null,
        )

        // Assert
        assertTrue(peripheralCallbacks.nativeEventBus().first() is OnCharacteristicUpdated)
      }
    }

  @Test
  fun `didWriteValueForDescriptor callback emits an OnDescriptorWrite BleEvent`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val peripheralCallbacks = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(peripheralCallbacks)
        val mockDescriptor: CBDescriptor = MockCBDescriptor()

        // Act
        peripheralCallbacks.peripheral(
          peripheral = mockCBPeripheral,
          didWriteValueForDescriptor = mockDescriptor,
          error = null,
        )

        // Assert
        assertTrue(peripheralCallbacks.nativeEventBus().first() is OnDescriptorWrite)
      }
    }

  @Test
  fun `didUpdateValueForDescriptor callback emits an OnDescriptorUpdated BleEvent`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val mockDescriptor: CBDescriptor = MockCBDescriptor()

        // Prepare object under test
        val peripheralCallbacks = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(peripheralCallbacks)

        // Act
        peripheralCallbacks.peripheral(
          peripheral = mockCBPeripheral,
          didUpdateValueForDescriptor = mockDescriptor,
          error = null,
        )

        // Assert
        assertTrue(peripheralCallbacks.nativeEventBus().first() is OnDescriptorRead)
      }
    }
}
