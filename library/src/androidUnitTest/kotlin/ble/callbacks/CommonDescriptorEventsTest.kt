package com.sherlockblue.kmpble.ble.callbacks

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattDescriptor
import com.sherlockblue.kmpble.ble.BleResponse
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGatt
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGattDescriptor
import com.sherlockblue.kmpble.ble.fixtures.TEST_STATUS
import com.sherlockblue.kmpble.constants.NULL_DESCRIPTOR_ERROR
import com.sherlockblue.kmpble.constants.NULL_GATT_ERROR
import com.sherlockblue.kmpble.constants.getErrorMessage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class CommonDescriptorEventsTest {
  @Test
  fun `onDescriptorWrite callback emits an DescriptorWrite BleResponse`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor = MockBluetoothGattDescriptor.Builder().build()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorWrite(mockBleGatt, mockDescriptor, mockStatus)

      // Assert
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleResponse.DescriptorWrite)
    }

  @Test
  fun `onDescriptorWrite callback emits an DescriptorWrite BleResponse with same Descriptor UUID`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor = MockBluetoothGattDescriptor.Builder().build()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorWrite(mockBleGatt, mockDescriptor, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleResponse.DescriptorWrite).descriptorUUID == mockDescriptor.uuid.toString(),
      )
    }

  @Test
  fun `onDescriptorWrite callback emits an DescriptorWrite BleResponse with correct status`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.DescriptorWrite).status == mockStatus,
      )
    }

  @Test
  fun `onDescriptorWrite callback with null Gatt emits an Error BleResponse`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt? = null
      val mockValue: ByteArray = byteArrayOf()
      val mockDescriptor: BluetoothGattDescriptor =
        MockBluetoothGattDescriptor.Builder()
          .setValue(mockValue)
          .build()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorWrite(mockBleGatt, mockDescriptor, mockStatus)

      // Assert
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleResponse.Error)
    }

  @Test
  fun `onDescriptorWrite callback with null Gatt emits an Error BleResponse with NULL_GATT_ERROR message`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt? = null
      val mockValue: ByteArray = byteArrayOf()
      val mockDescriptor: BluetoothGattDescriptor =
        MockBluetoothGattDescriptor.Builder()
          .setValue(mockValue)
          .build()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorWrite(mockBleGatt, mockDescriptor, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleResponse.Error).message ==
          getErrorMessage(
            NULL_GATT_ERROR,
          ),
      )
    }

  @Test
  fun `onDescriptorWrite callback with null Gatt emits an Error BleResponse with correct status`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.Error).status == mockStatus,
      )
    }

  @Test
  fun `onDescriptorWrite callback with null Descriptor emits an Error BleResponse`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor? = null
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorWrite(mockBleGatt, mockDescriptor, mockStatus)

      // Assert
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleResponse.Error)
    }

  @Test
  fun `onDescriptorWrite callback with null Descriptor emits an Error BleResponse with NULL_DESCRIPTOR_ERROR message`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor? = null
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorWrite(mockBleGatt, mockDescriptor, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleResponse.Error).message ==
          getErrorMessage(
            NULL_DESCRIPTOR_ERROR,
          ),
      )
    }

  @Test
  fun `onDescriptorWrite callback with null Descriptor emits an Error BleResponse with correct status`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.Error).status == mockStatus,
      )
    }

  @Test
  fun `onDescriptorRead callback emits an DescriptorRead BleResponse`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor = MockBluetoothGattDescriptor.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

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
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleResponse.DescriptorRead)
    }

  @Test
  fun `onDescriptorRead callback emits an DescriptorRead BleResponse with same Descriptor UUID`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor = MockBluetoothGattDescriptor.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

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
        (gattCallbackHandler.eventBus().first() as BleResponse.DescriptorRead).descriptorUUID == mockDescriptor.uuid.toString(),
      )
    }

  @Test
  fun `onDescriptorRead callback emits an DescriptorRead BleResponse with correct status`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor = MockBluetoothGattDescriptor.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

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
        (gattCallbackHandler.eventBus().first() as BleResponse.DescriptorRead).status == mockStatus,
      )
    }

  @Test
  fun `onDescriptorRead callback emits an DescriptorRead BleResponse with copy of value`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor = MockBluetoothGattDescriptor.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

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
        (gattCallbackHandler.eventBus().first() as BleResponse.DescriptorRead).data !== mockValue,
      )
    }

  @Test
  fun `onDescriptorRead callback emits an DescriptorRead BleResponse with equivalent value`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor = MockBluetoothGattDescriptor.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

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
        (gattCallbackHandler.eventBus().first() as BleResponse.DescriptorRead).data contentEquals mockValue,
      )
    }

  @Test
  fun `DEPRECATED onDescriptorRead callback emits an DescriptorRead BleResponse`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockDescriptor: BluetoothGattDescriptor =
        MockBluetoothGattDescriptor.Builder()
          .setValue(mockValue)
          .build()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(mockBleGatt, mockDescriptor, mockStatus)

      // Assert
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleResponse.DescriptorRead)
    }

  @Test
  fun `DEPRECATED onDescriptorRead callback emits an DescriptorRead BleResponse with same Descriptor UUID`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockDescriptor: BluetoothGattDescriptor =
        MockBluetoothGattDescriptor.Builder()
          .setValue(mockValue)
          .build()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(mockBleGatt, mockDescriptor, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleResponse.DescriptorRead).descriptorUUID == mockDescriptor.uuid.toString(),
      )
    }

  @Test
  fun `DEPRECATED onDescriptorRead callback emits an DescriptorRead BleResponse with correct status`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockDescriptor: BluetoothGattDescriptor =
        MockBluetoothGattDescriptor.Builder()
          .setValue(mockValue)
          .build()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(mockBleGatt, mockDescriptor, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleResponse.DescriptorRead).status == mockStatus,
      )
    }

  @Test
  fun `DEPRECATED onDescriptorRead callback emits an DescriptorRead BleResponse with copy of value`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockDescriptor: BluetoothGattDescriptor =
        MockBluetoothGattDescriptor.Builder()
          .setValue(mockValue)
          .build()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(mockBleGatt, mockDescriptor, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleResponse.DescriptorRead).data !== mockValue,
      )
    }

  @Test
  fun `DEPRECATED onDescriptorRead callback emits an DescriptorRead BleResponse with equivalent value`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockDescriptor: BluetoothGattDescriptor =
        MockBluetoothGattDescriptor.Builder()
          .setValue(mockValue)
          .build()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(mockBleGatt, mockDescriptor, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleResponse.DescriptorRead).data contentEquals mockValue,
      )
    }

  @Test
  fun `DEPRECATED onDescriptorRead callback with null Gatt emits a CallbackError BleResponse`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt? = null
      val mockDescriptor: BluetoothGattDescriptor = MockBluetoothGattDescriptor.Builder().build()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(mockBleGatt, mockDescriptor, mockStatus)

      // Assert
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleResponse.Error)
    }

  @Test
  fun `DEPRECATED onDescriptorRead callback emits a CallbackError BleResponse with null Gatt message`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt? = null
      val mockDescriptor: BluetoothGattDescriptor = MockBluetoothGattDescriptor.Builder().build()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(mockBleGatt, mockDescriptor, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleResponse.Error).message ==
          getErrorMessage(
            NULL_GATT_ERROR,
          ),
      )
    }

  @Test
  fun `DEPRECATED onDescriptorRead callback emits a CallbackError BleResponse with correct status`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.Error).status == mockStatus,
      )
    }

  @Test
  fun `DEPRECATED onDescriptorRead callback with null Descriptor emits a CallbackError BleResponse`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor? = null
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(mockBleGatt, mockDescriptor, mockStatus)

      // Assert
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleResponse.Error)
    }

  @Test
  fun `DEPRECATED onDescriptorRead callback emits an CallbackError BleResponse with null Descriptor message`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockDescriptor: BluetoothGattDescriptor? = null
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onDescriptorRead(mockBleGatt, mockDescriptor, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleResponse.Error).message ==
          getErrorMessage(
            NULL_DESCRIPTOR_ERROR,
          ),
      )
    }

  @Test
  fun `DEPRECATED onDescriptorRead callback emits an CallbackError BleResponse with correct status`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.Error).status == mockStatus,
      )
    }
}
