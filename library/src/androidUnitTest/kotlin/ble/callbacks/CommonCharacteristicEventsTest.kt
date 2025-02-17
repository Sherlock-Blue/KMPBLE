package com.sherlockblue.kmpble.ble.callbacks

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import com.sherlockblue.kmpble.ble.BleResponse
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGatt
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGattCharacteristic
import com.sherlockblue.kmpble.ble.fixtures.TEST_STATUS
import com.sherlockblue.kmpble.constants.NULL_CHARACTERISTIC_ERROR
import com.sherlockblue.kmpble.constants.NULL_GATT_ERROR
import com.sherlockblue.kmpble.constants.getErrorMessage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class CommonCharacteristicEventsTest {
  @Test
  fun `onCharacteristicChanged callback emits an CharacteristicChanged BleResponse`() =
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
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleResponse.CharacteristicChanged)
    }

  @Test
  fun `onCharacteristicChanged callback emits an CharacteristicChanged BleResponse with correct UUID`() =
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
        (
          gattCallbackHandler.eventBus()
            .first() as BleResponse.CharacteristicChanged
        ).characteristicUUID == mockCharacteristic.uuid.toString(),
      )
    }

  @Test
  fun `onCharacteristicChanged callback emits an CharacteristicChanged BleResponse with copy of value array`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.CharacteristicChanged).data !== mockValue,
      )
    }

  @Test
  fun `onCharacteristicChanged callback emits an CharacteristicChanged BleResponse with value of equivalent contents`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.CharacteristicChanged).data contentEquals mockValue,
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicChanged callback emits an CharacteristicChanged BleResponse`() =
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
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleResponse.CharacteristicChanged)
    }

  @Test
  fun `DEPRECATED onCharacteristicChanged callback emits an CharacteristicChanged with same Characteristic UUID`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.CharacteristicChanged).characteristicUUID ==
          mockCharacteristic.uuid.toString(),
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicChanged callback emits an CharacteristicChanged BleResponse with copy of value array`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.CharacteristicChanged).data !== mockValue,
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicChanged callback emits an CharacteristicChanged BleResponse with equivalent value array`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.CharacteristicChanged).data contentEquals mockValue,
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicChanged callback with null Gatt emits an Error BleResponse`() =
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
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleResponse.Error)
    }

  @Test
  fun `DEPRECATED onCharacteristicChanged callback with null Gatt emits an Error BleResponse with null Gatt instance message`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.Error).message ==
          getErrorMessage(
            NULL_GATT_ERROR,
          ),
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicChanged callback with null Characteristic emits an Error with null Characteristic message`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.Error).message ==
          getErrorMessage(
            NULL_CHARACTERISTIC_ERROR,
          ),
      )
    }

  @Test
  fun `onCharacteristicWrite callback emits an CharacteristicWrite BleResponse`() =
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
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleResponse.CharacteristicWrite)
    }

  @Test
  fun `onCharacteristicWrite callback emits an CharacteristicWrite BleResponse with same Characteristic UUID`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.CharacteristicWrite).characteristicUUID ==
          mockCharacteristic.uuid.toString(),
      )
    }

  @Test
  fun `onCharacteristicWrite callback emits an CharacteristicWrite BleResponse with correct status`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockCharacteristic: BluetoothGattCharacteristic = MockBluetoothGattCharacteristic.Builder().build()
      val mockStatus = TEST_STATUS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onCharacteristicWrite(mockBleGatt, mockCharacteristic, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.eventBus().first() as BleResponse.CharacteristicWrite).status == mockStatus,
      )
    }

  @Test
  fun `onCharacteristicWrite callback with null Gatt emits an Error BleResponse`() =
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
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleResponse.Error)
    }

  @Test
  fun `onCharacteristicWrite callback with null Gatt emits an Error BleResponse with NULL_GATT_ERROR message`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.Error).message ==
          getErrorMessage(
            NULL_GATT_ERROR,
          ),
      )
    }

  @Test
  fun `onCharacteristicWrite callback with null Gatt emits an Error BleResponse with correct status`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.Error).status == mockStatus,
      )
    }

  @Test
  fun `onCharacteristicWrite callback with null Characteristic emits an Error BleResponse`() =
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
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleResponse.Error)
    }

  @Test
  fun `onCharacteristicWrite callback with null Characteristic emits an Error BleResponse with NULL_CHARACTERISTIC_ERROR message`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.Error).message ==
          getErrorMessage(
            NULL_CHARACTERISTIC_ERROR,
          ),
      )
    }

  @Test
  fun `onCharacteristicWrite callback with null Characteristic emits an Error BleResponse with correct status`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.Error).status == mockStatus,
      )
    }

  @Test
  fun `onCharacteristicRead callback emits an CharacteristicRead BleResponse`() =
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
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleResponse.CharacteristicRead)
    }

  @Test
  fun `onCharacteristicRead callback emits an CharacteristicRead BleResponse with same Characteristic UUID`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.CharacteristicRead).characteristicUUID == mockCharacteristic.uuid.toString(),
      )
    }

  @Test
  fun `onCharacteristicRead callback emits an CharacteristicRead BleResponse with correct status`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.CharacteristicRead).status == mockStatus,
      )
    }

  @Test
  fun `onCharacteristicRead callback emits an CharacteristicRead BleResponse with copy of value`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.CharacteristicRead).data !== mockValue,
      )
    }

  @Test
  fun `onCharacteristicRead callback emits an CharacteristicRead BleResponse with equivalent value`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.CharacteristicRead).data contentEquals mockValue,
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicRead callback emits an CharacteristicRead BleResponse`() =
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
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleResponse.CharacteristicRead)
    }

  @Test
  fun `DEPRECATED onCharacteristicRead callback emits an CharacteristicRead BleResponse with same Characteristic UUID`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.CharacteristicRead).characteristicUUID == mockCharacteristic.uuid.toString(),
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicRead callback emits an CharacteristicRead BleResponse with correct status`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.CharacteristicRead).status == mockStatus,
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicRead callback emits an CharacteristicRead BleResponse with copy of value`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.CharacteristicRead).data !== mockValue,
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicRead callback emits an CharacteristicRead BleResponse with equivalent value`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.CharacteristicRead).data contentEquals mockValue,
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicRead callback with null Gatt emits a CallbackError BleResponse`() =
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
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleResponse.Error)
    }

  @Test
  fun `DEPRECATED onCharacteristicRead callback emits a CallbackError BleResponse with null Gatt message`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.Error).message ==
          getErrorMessage(
            NULL_GATT_ERROR,
          ),
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicRead callback emits a CallbackError BleResponse with correct status`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.Error).status == mockStatus,
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicRead callback with null Characteristic emits a CallbackError BleResponse`() =
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
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleResponse.Error)
    }

  @Test
  fun `DEPRECATED onCharacteristicRead callback emits an CallbackError BleResponse with null Characteristic message`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.Error).message ==
          getErrorMessage(
            NULL_CHARACTERISTIC_ERROR,
          ),
      )
    }

  @Test
  fun `DEPRECATED onCharacteristicRead callback emits an CallbackError BleResponse with correct status`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.Error).status == mockStatus,
      )
    }
}
