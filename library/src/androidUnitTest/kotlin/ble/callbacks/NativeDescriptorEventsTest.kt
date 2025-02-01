package com.sherlockblue.kmpble.ble.callbacks

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattDescriptor
import com.sherlockblue.kmpble.NULL_DESCRIPTOR_ERROR
import com.sherlockblue.kmpble.NULL_GATT_ERROR
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGatt
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGattDescriptor
import com.sherlockblue.kmpble.ble.fixtures.TEST_STATUS
import com.sherlockblue.kmpble.constants.getErrorMessage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class NativeDescriptorEventsTest {
  @Test
  fun `onDescriptorWrite callback emits an OnDescriptorWrite BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor = MockBluetoothGattDescriptor.Builder().build()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorWrite(mockBleGatt, mockDescriptor, BluetoothGatt.GATT_SUCCESS)

      // Assert
      Assert.assertTrue(gattCallbackHandler.nativeEventBus().first() is OnDescriptorWrite)
    }

  @Test
  fun `onDescriptorWrite callback emits an OnDescriptorWrite BleEvent with same Gatt instance`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor = MockBluetoothGattDescriptor.Builder().build()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorWrite(mockBleGatt, mockDescriptor, BluetoothGatt.GATT_SUCCESS)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnDescriptorWrite).gatt === mockBleGatt,
      )
    }

  @Test
  fun `onDescriptorWrite callback emits an OnDescriptorWrite BleEvent with same Descriptor instance`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor = MockBluetoothGattDescriptor.Builder().build()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorWrite(mockBleGatt, mockDescriptor, BluetoothGatt.GATT_SUCCESS)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnDescriptorWrite).descriptor === mockDescriptor,
      )
    }

  @Test
  fun `onDescriptorWrite callback emits an OnDescriptorWrite BleEvent with correct status`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor = MockBluetoothGattDescriptor.Builder().build()
      val mockStatus = TEST_STATUS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorWrite(mockBleGatt, mockDescriptor, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnDescriptorWrite).status == mockStatus,
      )
    }

  @Test
  fun `onDescriptorWrite callback with null Gatt emits a CallbackError BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt? = null
      val mockValue: ByteArray = byteArrayOf()
      val mockDescriptor: BluetoothGattDescriptor =
        MockBluetoothGattDescriptor.Builder()
          .setValue(mockValue)
          .build()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorWrite(mockBleGatt, mockDescriptor, BluetoothGatt.GATT_SUCCESS)

      // Assert
      Assert.assertTrue(gattCallbackHandler.nativeEventBus().first() is CallbackError)
    }

  @Test
  fun `onDescriptorWrite callback with null Gatt emits a CallbackError BleEvent with NULL_GATT_ERROR message`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt? = null
      val mockValue: ByteArray = byteArrayOf()
      val mockDescriptor: BluetoothGattDescriptor =
        MockBluetoothGattDescriptor.Builder()
          .setValue(mockValue)
          .build()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorWrite(mockBleGatt, mockDescriptor, BluetoothGatt.GATT_SUCCESS)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as CallbackError).message == getErrorMessage(NULL_GATT_ERROR),
      )
    }

  @Test
  fun `onDescriptorWrite callback with null Gatt emits a CallbackError BleEvent with correct status`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt? = null
      val mockValue: ByteArray = byteArrayOf()
      val mockDescriptor: BluetoothGattDescriptor =
        MockBluetoothGattDescriptor.Builder()
          .setValue(mockValue)
          .build()
      val mockStatus = TEST_STATUS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorWrite(mockBleGatt, mockDescriptor, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as CallbackError).status == mockStatus,
      )
    }

  @Test
  fun `onDescriptorWrite callback with null Descriptor emits a CallbackError BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor? = null

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorWrite(mockBleGatt, mockDescriptor, BluetoothGatt.GATT_SUCCESS)

      // Assert
      Assert.assertTrue(gattCallbackHandler.nativeEventBus().first() is CallbackError)
    }

  @Test
  fun `onDescriptorWrite callback with null Descriptor emits a CallbackError BleEvent with NULL_DESCRIPTOR_ERROR message`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor? = null

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorWrite(mockBleGatt, mockDescriptor, BluetoothGatt.GATT_SUCCESS)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as CallbackError).message == getErrorMessage(NULL_DESCRIPTOR_ERROR),
      )
    }

  @Test
  fun `onDescriptorWrite callback with null Descriptor emits a CallbackError BleEvent with correct status`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor? = null
      val mockStatus = TEST_STATUS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorWrite(mockBleGatt, mockDescriptor, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as CallbackError).status == mockStatus,
      )
    }

  @Test
  fun `onDescriptorRead callback emits an OnDescriptorRead BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor = MockBluetoothGattDescriptor.Builder().build()
      val mockValue: ByteArray = byteArrayOf()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(
        mockBleGatt,
        mockDescriptor,
        BluetoothGatt.GATT_SUCCESS,
        mockValue,
      )

      // Assert
      Assert.assertTrue(gattCallbackHandler.nativeEventBus().first() is OnDescriptorRead)
    }

  @Test
  fun `onDescriptorRead callback emits an OnDescriptorRead BleEvent with same Gatt instance`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor = MockBluetoothGattDescriptor.Builder().build()
      val mockValue: ByteArray = byteArrayOf()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(
        mockBleGatt,
        mockDescriptor,
        BluetoothGatt.GATT_SUCCESS,
        mockValue,
      )

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnDescriptorRead).gatt === mockBleGatt,
      )
    }

  @Test
  fun `onDescriptorRead callback emits an OnDescriptorRead BleEvent with same Descriptor instance`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor = MockBluetoothGattDescriptor.Builder().build()
      val mockValue: ByteArray = byteArrayOf()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(
        mockBleGatt,
        mockDescriptor,
        BluetoothGatt.GATT_SUCCESS,
        mockValue,
      )

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnDescriptorRead).descriptor === mockDescriptor,
      )
    }

  @Test
  fun `onDescriptorRead callback emits an OnDescriptorRead BleEvent with correct status`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor = MockBluetoothGattDescriptor.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockStatus = TEST_STATUS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(
        mockBleGatt,
        mockDescriptor,
        mockStatus,
        mockValue,
      )

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnDescriptorRead).status == mockStatus,
      )
    }

  @Test
  fun `onDescriptorRead callback emits an OnDescriptorRead BleEvent with copy of value`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor = MockBluetoothGattDescriptor.Builder().build()
      val mockValue: ByteArray = byteArrayOf()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(
        mockBleGatt,
        mockDescriptor,
        BluetoothGatt.GATT_SUCCESS,
        mockValue,
      )

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnDescriptorRead).value !== mockValue,
      )
    }

  @Test
  fun `onDescriptorRead callback emits an OnDescriptorRead BleEvent with equivalent value`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor = MockBluetoothGattDescriptor.Builder().build()
      val mockValue: ByteArray = byteArrayOf()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(
        mockBleGatt,
        mockDescriptor,
        BluetoothGatt.GATT_SUCCESS,
        mockValue,
      )

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnDescriptorRead).value contentEquals mockValue,
      )
    }

  @Test
  fun `DEPRECATED onDescriptorRead callback emits an OnDescriptorRead BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockDescriptor: BluetoothGattDescriptor =
        MockBluetoothGattDescriptor.Builder()
          .setValue(mockValue)
          .build()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(mockBleGatt, mockDescriptor, BluetoothGatt.GATT_SUCCESS)

      // Assert
      Assert.assertTrue(gattCallbackHandler.nativeEventBus().first() is OnDescriptorRead)
    }

  @Test
  fun `DEPRECATED onDescriptorRead callback emits an OnDescriptorRead BleEvent with same Gatt instance`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockDescriptor: BluetoothGattDescriptor =
        MockBluetoothGattDescriptor.Builder()
          .setValue(mockValue)
          .build()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(mockBleGatt, mockDescriptor, BluetoothGatt.GATT_SUCCESS)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnDescriptorRead).gatt === mockBleGatt,
      )
    }

  @Test
  fun `DEPRECATED onDescriptorRead callback emits an OnDescriptorRead BleEvent with same Descriptor instance`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockDescriptor: BluetoothGattDescriptor =
        MockBluetoothGattDescriptor.Builder()
          .setValue(mockValue)
          .build()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(mockBleGatt, mockDescriptor, BluetoothGatt.GATT_SUCCESS)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnDescriptorRead).descriptor === mockDescriptor,
      )
    }

  @Test
  fun `DEPRECATED onDescriptorRead callback emits an OnDescriptorRead BleEvent with correct status`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockDescriptor: BluetoothGattDescriptor =
        MockBluetoothGattDescriptor.Builder()
          .setValue(mockValue)
          .build()
      val mockStatus = TEST_STATUS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(mockBleGatt, mockDescriptor, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnDescriptorRead).status == mockStatus,
      )
    }

  @Test
  fun `DEPRECATED onDescriptorRead callback emits an OnDescriptorRead BleEvent with copy of value`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockDescriptor: BluetoothGattDescriptor =
        MockBluetoothGattDescriptor.Builder()
          .setValue(mockValue)
          .build()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(mockBleGatt, mockDescriptor, BluetoothGatt.GATT_SUCCESS)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnDescriptorRead).value !== mockValue,
      )
    }

  @Test
  fun `DEPRECATED onDescriptorRead callback emits an OnDescriptorRead BleEvent with equivalent value`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockDescriptor: BluetoothGattDescriptor =
        MockBluetoothGattDescriptor.Builder()
          .setValue(mockValue)
          .build()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(mockBleGatt, mockDescriptor, BluetoothGatt.GATT_SUCCESS)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnDescriptorRead).value contentEquals mockValue,
      )
    }

  @Test
  fun `DEPRECATED onDescriptorRead callback with null Gatt emits a CallbackError BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt? = null
      val mockDescriptor: BluetoothGattDescriptor = MockBluetoothGattDescriptor.Builder().build()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(mockBleGatt, mockDescriptor, BluetoothGatt.GATT_SUCCESS)

      // Assert
      Assert.assertTrue(gattCallbackHandler.nativeEventBus().first() is CallbackError)
    }

  @Test
  fun `DEPRECATED onDescriptorRead callback emits a CallbackError BleEvent with null Gatt message`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt? = null
      val mockDescriptor: BluetoothGattDescriptor = MockBluetoothGattDescriptor.Builder().build()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(mockBleGatt, mockDescriptor, BluetoothGatt.GATT_SUCCESS)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as CallbackError).message == getErrorMessage(NULL_GATT_ERROR),
      )
    }

  @Test
  fun `DEPRECATED onDescriptorRead callback emits a CallbackError BleEvent with correct status`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt? = null
      val mockDescriptor: BluetoothGattDescriptor = MockBluetoothGattDescriptor.Builder().build()
      val mockStatus = TEST_STATUS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(mockBleGatt, mockDescriptor, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as CallbackError).status == mockStatus,
      )
    }

  @Test
  fun `DEPRECATED onDescriptorRead callback with null Descriptor emits a CallbackError BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor? = null

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(mockBleGatt, mockDescriptor, BluetoothGatt.GATT_SUCCESS)

      // Assert
      Assert.assertTrue(gattCallbackHandler.nativeEventBus().first() is CallbackError)
    }

  @Test
  fun `DEPRECATED onDescriptorRead callback emits an CallbackError BleEvent with null Descriptor message`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor? = null

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(mockBleGatt, mockDescriptor, BluetoothGatt.GATT_SUCCESS)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as CallbackError).message == getErrorMessage(NULL_DESCRIPTOR_ERROR),
      )
    }

  @Test
  fun `DEPRECATED onDescriptorRead callback emits an CallbackError BleEvent with correct status`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor? = null
      val mockStatus = TEST_STATUS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(mockBleGatt, mockDescriptor, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as CallbackError).status == mockStatus,
      )
    }
}
