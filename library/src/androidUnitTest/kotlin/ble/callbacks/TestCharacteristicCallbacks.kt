package com.sherlockblue.kmpble.ble.callbacks

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import com.sherlockblue.kmpble.NULL_CHARACTERISTIC_ERROR
import com.sherlockblue.kmpble.NULL_GATT_ERROR
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGatt
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGattCharacteristic
import com.sherlockblue.kmpble.ble.fixtures.TEST_STATUS
import com.sherlockblue.kmpble.constants.getErrorMessage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class TestCharacteristicCallbacks {
  @Test
  fun `onCharacteristicChanged callback emits an OnCharacteristicChanged BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockCharacteristic: BluetoothGattCharacteristic = MockBluetoothGattCharacteristic.Builder().build()
      val mockValue: ByteArray = byteArrayOf()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicChanged(mockBleGatt, mockCharacteristic, mockValue)

      // Assert
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleEvent.OnCharacteristicChanged)
    }

  @Test
  fun `onCharacteristicChanged callback emits an OnCharacteristicChanged BleEvent with same Gatt instance`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockCharacteristic: BluetoothGattCharacteristic = MockBluetoothGattCharacteristic.Builder().build()
      val mockValue: ByteArray = byteArrayOf()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicChanged(mockBleGatt, mockCharacteristic, mockValue)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.OnCharacteristicChanged).gatt === mockBleGatt,
      )
    }

  @Test
  fun `onCharacteristicChanged callback emits an OnCharacteristicChanged BleEvent with same Characteristic instance`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockCharacteristic: BluetoothGattCharacteristic = MockBluetoothGattCharacteristic.Builder().build()
      val mockValue: ByteArray = byteArrayOf()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicChanged(mockBleGatt, mockCharacteristic, mockValue)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.OnCharacteristicChanged).characteristic === mockCharacteristic,
      )
    }

  @Test
  fun `onCharacteristicChanged callback emits an OnCharacteristicChanged BleEvent with copy of value array`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockCharacteristic: BluetoothGattCharacteristic = MockBluetoothGattCharacteristic.Builder().build()
      val mockValue: ByteArray = byteArrayOf()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicChanged(mockBleGatt, mockCharacteristic, mockValue)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.OnCharacteristicChanged).value !== mockValue,
      )
    }

  @Test
  fun `onCharacteristicChanged callback emits an OnCharacteristicChanged BleEvent with value of equivalent contents`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockCharacteristic: BluetoothGattCharacteristic = MockBluetoothGattCharacteristic.Builder().build()
      val mockValue: ByteArray = byteArrayOf()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicChanged(mockBleGatt, mockCharacteristic, mockValue)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.OnCharacteristicChanged).value contentEquals mockValue,
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicChanged callback emits an OnCharacteristicChanged BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockCharacteristic: BluetoothGattCharacteristic =
        MockBluetoothGattCharacteristic.Builder()
          .setValue(mockValue)
          .build()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicChanged(mockBleGatt, mockCharacteristic)

      // Assert
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleEvent.OnCharacteristicChanged)
    }

  @Test
  fun `DEPRECATED onCharacteristicChanged callback emits an OnCharacteristicChanged with same Gatt instance`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockCharacteristic: BluetoothGattCharacteristic =
        MockBluetoothGattCharacteristic.Builder()
          .setValue(mockValue)
          .build()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicChanged(mockBleGatt, mockCharacteristic)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.OnCharacteristicChanged).gatt === mockBleGatt,
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicChanged callback emits an OnCharacteristicChanged with same Characteristic instance`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockCharacteristic: BluetoothGattCharacteristic =
        MockBluetoothGattCharacteristic.Builder()
          .setValue(mockValue)
          .build()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicChanged(mockBleGatt, mockCharacteristic)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.OnCharacteristicChanged).characteristic === mockCharacteristic,
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicChanged callback emits an OnCharacteristicChanged BleEvent with copy of value array`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockCharacteristic: BluetoothGattCharacteristic =
        MockBluetoothGattCharacteristic.Builder()
          .setValue(mockValue)
          .build()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicChanged(mockBleGatt, mockCharacteristic)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.OnCharacteristicChanged).value !== mockValue,
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicChanged callback emits an OnCharacteristicChanged BleEvent with equivalent value array`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockCharacteristic: BluetoothGattCharacteristic =
        MockBluetoothGattCharacteristic.Builder()
          .setValue(mockValue)
          .build()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicChanged(mockBleGatt, mockCharacteristic)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.OnCharacteristicChanged).value contentEquals mockValue,
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicChanged callback with null Gatt emits a CallbackError BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt? = null
      val mockCharacteristic: BluetoothGattCharacteristic = MockBluetoothGattCharacteristic.Builder().build()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicChanged(mockBleGatt, mockCharacteristic)

      // Assert
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleEvent.CallbackError)
    }

  @Test
  fun `DEPRECATED onCharacteristicChanged callback with null Gatt emits a CallbackError BleEvent with null Gatt instance message`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt? = null
      val mockCharacteristic: BluetoothGattCharacteristic = MockBluetoothGattCharacteristic.Builder().build()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicChanged(mockBleGatt, mockCharacteristic)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.CallbackError).message == getErrorMessage(NULL_GATT_ERROR),
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicChanged callback with null Characteristic emits CallbackError with null Characteristic message`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockCharacteristic: BluetoothGattCharacteristic? = null

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicChanged(mockBleGatt, mockCharacteristic)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.CallbackError).message == getErrorMessage(NULL_CHARACTERISTIC_ERROR),
      )
    }

  @Test
  fun `onCharacteristicWrite callback emits an OnCharacteristicWrite BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockCharacteristic: BluetoothGattCharacteristic = MockBluetoothGattCharacteristic.Builder().build()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicWrite(mockBleGatt, mockCharacteristic, mockStatus)

      // Assert
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleEvent.OnCharacteristicWrite)
    }

  @Test
  fun `onCharacteristicWrite callback emits an OnCharacteristicWrite BleEvent with same Gatt instance`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockCharacteristic: BluetoothGattCharacteristic = MockBluetoothGattCharacteristic.Builder().build()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicWrite(mockBleGatt, mockCharacteristic, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.OnCharacteristicWrite).gatt === mockBleGatt,
      )
    }

  @Test
  fun `onCharacteristicWrite callback emits an OnCharacteristicWrite BleEvent with same Characteristic instance`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockCharacteristic: BluetoothGattCharacteristic = MockBluetoothGattCharacteristic.Builder().build()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicWrite(mockBleGatt, mockCharacteristic, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.OnCharacteristicWrite).characteristic === mockCharacteristic,
      )
    }

  @Test
  fun `onCharacteristicWrite callback emits an OnCharacteristicWrite BleEvent with correct status`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockCharacteristic: BluetoothGattCharacteristic = MockBluetoothGattCharacteristic.Builder().build()
      val mockStatus = Int.MAX_VALUE

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicWrite(mockBleGatt, mockCharacteristic, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.OnCharacteristicWrite).status == mockStatus,
      )
    }

  @Test
  fun `onCharacteristicWrite callback with null Gatt emits a CallbackError BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt? = null
      val mockValue: ByteArray = byteArrayOf()
      val mockCharacteristic: BluetoothGattCharacteristic =
        MockBluetoothGattCharacteristic.Builder()
          .setValue(mockValue)
          .build()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicWrite(mockBleGatt, mockCharacteristic, mockStatus)

      // Assert
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleEvent.CallbackError)
    }

  @Test
  fun `onCharacteristicWrite callback with null Gatt emits a CallbackError BleEvent with NULL_GATT_ERROR message`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt? = null
      val mockValue: ByteArray = byteArrayOf()
      val mockCharacteristic: BluetoothGattCharacteristic =
        MockBluetoothGattCharacteristic.Builder()
          .setValue(mockValue)
          .build()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicWrite(mockBleGatt, mockCharacteristic, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.CallbackError).message == getErrorMessage(NULL_GATT_ERROR),
      )
    }

  @Test
  fun `onCharacteristicWrite callback with null Gatt emits a CallbackError BleEvent with correct status`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt? = null
      val mockValue: ByteArray = byteArrayOf()
      val mockCharacteristic: BluetoothGattCharacteristic =
        MockBluetoothGattCharacteristic.Builder()
          .setValue(mockValue)
          .build()
      val mockStatus = TEST_STATUS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicWrite(mockBleGatt, mockCharacteristic, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.CallbackError).status == mockStatus,
      )
    }

  @Test
  fun `onCharacteristicWrite callback with null Characteristic emits a CallbackError BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockCharacteristic: BluetoothGattCharacteristic? = null
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicWrite(mockBleGatt, mockCharacteristic, mockStatus)

      // Assert
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleEvent.CallbackError)
    }

  @Test
  fun `onCharacteristicWrite callback with null Characteristic emits a CallbackError BleEvent with NULL_CHARACTERISTIC_ERROR message`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockCharacteristic: BluetoothGattCharacteristic? = null
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicWrite(mockBleGatt, mockCharacteristic, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.CallbackError).message == getErrorMessage(NULL_CHARACTERISTIC_ERROR),
      )
    }

  @Test
  fun `onCharacteristicWrite callback with null Characteristic emits a CallbackError BleEvent with correct status`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockCharacteristic: BluetoothGattCharacteristic? = null
      val mockStatus = TEST_STATUS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicWrite(mockBleGatt, mockCharacteristic, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.CallbackError).status == mockStatus,
      )
    }

  @Test
  fun `onCharacteristicRead callback emits an OnCharacteristicRead BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockCharacteristic: BluetoothGattCharacteristic = MockBluetoothGattCharacteristic.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicRead(
        mockBleGatt,
        mockCharacteristic,
        mockValue,
        mockStatus,
      )

      // Assert
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleEvent.OnCharacteristicRead)
    }

  @Test
  fun `onCharacteristicRead callback emits an OnCharacteristicRead BleEvent with same Gatt instance`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockCharacteristic: BluetoothGattCharacteristic = MockBluetoothGattCharacteristic.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicRead(
        mockBleGatt,
        mockCharacteristic,
        mockValue,
        mockStatus,
      )

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.OnCharacteristicRead).gatt === mockBleGatt,
      )
    }

  @Test
  fun `onCharacteristicRead callback emits an OnCharacteristicRead BleEvent with same Characteristic instance`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockCharacteristic: BluetoothGattCharacteristic = MockBluetoothGattCharacteristic.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicRead(
        mockBleGatt,
        mockCharacteristic,
        mockValue,
        mockStatus,
      )

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.OnCharacteristicRead).characteristic === mockCharacteristic,
      )
    }

  @Test
  fun `onCharacteristicRead callback emits an OnCharacteristicRead BleEvent with correct status`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockCharacteristic: BluetoothGattCharacteristic = MockBluetoothGattCharacteristic.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicRead(
        mockBleGatt,
        mockCharacteristic,
        mockValue,
        mockStatus,
      )

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.OnCharacteristicRead).status == mockStatus,
      )
    }

  @Test
  fun `onCharacteristicRead callback emits an OnCharacteristicRead BleEvent with copy of value`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockCharacteristic: BluetoothGattCharacteristic = MockBluetoothGattCharacteristic.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicRead(
        mockBleGatt,
        mockCharacteristic,
        mockValue,
        mockStatus,
      )

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.OnCharacteristicRead).value !== mockValue,
      )
    }

  @Test
  fun `onCharacteristicRead callback emits an OnCharacteristicRead BleEvent with equivalent value`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockCharacteristic: BluetoothGattCharacteristic = MockBluetoothGattCharacteristic.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicRead(
        mockBleGatt,
        mockCharacteristic,
        mockValue,
        mockStatus,
      )

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.OnCharacteristicRead).value contentEquals mockValue,
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicRead callback emits an OnCharacteristicRead BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockCharacteristic: BluetoothGattCharacteristic =
        MockBluetoothGattCharacteristic.Builder()
          .setValue(mockValue)
          .build()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicRead(mockBleGatt, mockCharacteristic, mockStatus)

      // Assert
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleEvent.OnCharacteristicRead)
    }

  @Test
  fun `DEPRECATED onCharacteristicRead callback emits an OnCharacteristicRead BleEvent with same Gatt instance`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockCharacteristic: BluetoothGattCharacteristic =
        MockBluetoothGattCharacteristic.Builder()
          .setValue(mockValue)
          .build()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicRead(mockBleGatt, mockCharacteristic, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.OnCharacteristicRead).gatt === mockBleGatt,
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicRead callback emits an OnCharacteristicRead BleEvent with same Characteristic instance`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockCharacteristic: BluetoothGattCharacteristic =
        MockBluetoothGattCharacteristic.Builder()
          .setValue(mockValue)
          .build()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicRead(mockBleGatt, mockCharacteristic, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.OnCharacteristicRead).characteristic === mockCharacteristic,
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicRead callback emits an OnCharacteristicRead BleEvent with correct status`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockCharacteristic: BluetoothGattCharacteristic =
        MockBluetoothGattCharacteristic.Builder()
          .setValue(mockValue)
          .build()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicRead(mockBleGatt, mockCharacteristic, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.OnCharacteristicRead).status == mockStatus,
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicRead callback emits an OnCharacteristicRead BleEvent with copy of value`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockCharacteristic: BluetoothGattCharacteristic =
        MockBluetoothGattCharacteristic.Builder()
          .setValue(mockValue)
          .build()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicRead(mockBleGatt, mockCharacteristic, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.OnCharacteristicRead).value !== mockValue,
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicRead callback emits an OnCharacteristicRead BleEvent with equivalent value`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockValue: ByteArray = byteArrayOf()
      val mockCharacteristic: BluetoothGattCharacteristic =
        MockBluetoothGattCharacteristic.Builder()
          .setValue(mockValue)
          .build()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicRead(mockBleGatt, mockCharacteristic, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.OnCharacteristicRead).value contentEquals mockValue,
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicRead callback with null Gatt emits a CallbackError BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt? = null
      val mockCharacteristic: BluetoothGattCharacteristic = MockBluetoothGattCharacteristic.Builder().build()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicRead(mockBleGatt, mockCharacteristic, mockStatus)

      // Assert
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleEvent.CallbackError)
    }

  @Test
  fun `DEPRECATED onCharacteristicRead callback emits a CallbackError BleEvent with null Gatt message`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt? = null
      val mockCharacteristic: BluetoothGattCharacteristic = MockBluetoothGattCharacteristic.Builder().build()
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicRead(mockBleGatt, mockCharacteristic, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.CallbackError).message == getErrorMessage(NULL_GATT_ERROR),
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicRead callback emits a CallbackError BleEvent with correct status`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt? = null
      val mockCharacteristic: BluetoothGattCharacteristic = MockBluetoothGattCharacteristic.Builder().build()
      val mockStatus = TEST_STATUS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicRead(mockBleGatt, mockCharacteristic, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.CallbackError).status == mockStatus,
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicRead callback with null Characteristic emits a CallbackError BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockCharacteristic: BluetoothGattCharacteristic? = null
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicRead(mockBleGatt, mockCharacteristic, mockStatus)

      // Assert
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleEvent.CallbackError)
    }

  @Test
  fun `DEPRECATED onCharacteristicRead callback emits an CallbackError BleEvent with null Characteristic message`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockCharacteristic: BluetoothGattCharacteristic? = null
      val mockStatus = BluetoothGatt.GATT_SUCCESS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicRead(mockBleGatt, mockCharacteristic, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.CallbackError).message == getErrorMessage(NULL_CHARACTERISTIC_ERROR),
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicRead callback emits an CallbackError BleEvent with correct status`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockCharacteristic: BluetoothGattCharacteristic? = null
      val mockStatus = TEST_STATUS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicRead(mockBleGatt, mockCharacteristic, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleEvent.CallbackError).status == mockStatus,
      )
    }
}
