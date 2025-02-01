package peripheral.callbacks

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.content.Context
import com.sherlockblue.kmpble.ble.BleResponse
import com.sherlockblue.kmpble.ble.NativeBleEvent
import com.sherlockblue.kmpble.ble.callbacks.GattCallbackHandler
import com.sherlockblue.kmpble.ble.callbacks.OnCharacteristicRead
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothDevice
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGatt
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGattCallback
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGattCharacteristic
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGattService
import com.sherlockblue.kmpble.ble.fixtures.MockMutableSharedFlow
import com.sherlockblue.kmpble.ble.fixtures.TEST_UUID
import com.sherlockblue.kmpble.peripheral.Peripheral
import io.mockk.mockk
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class PeripheralReadCharacteristicTest {
  // readCharacteristic

  @Test
  fun `ReadCharacteristic with null Gatt returns an Error BleResponse`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockContext: Context = mockk()
      val mockDevice: BluetoothDevice = MockBluetoothDevice.Builder().build()

      launch {
        // Prepare object under test
        val peripheral =
          Peripheral(
            device = mockDevice,
            coroutineScope = this,
            context = mockContext,
            gattCallbackHandler = GattCallbackHandler(this),
          )

        // Act
        peripheral.readCharacteristic(TEST_UUID) { bleEvent ->
          // Assert
          Assert.assertTrue(bleEvent is BleResponse.Error)
          this.cancel()
        }
      }
    }

  @Test
  fun `ReadCharacteristic with invalid UUID returns an Error BleResponse`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockContext: Context = mockk()
      val mockDevice: BluetoothDevice = MockBluetoothDevice.Builder().build()

      launch {
        // Prepare object under test
        val peripheral =
          Peripheral(
            device = mockDevice,
            coroutineScope = this,
            context = mockContext,
            gattCallbackHandler = GattCallbackHandler(this),
          )

        // Act
        peripheral.readCharacteristic("INVALID UUID") { bleEvent ->
          // Assert
          Assert.assertTrue(bleEvent is BleResponse.Error)
          this.cancel()
        }
      }
    }

  @Test
  fun `ReadCharacteristic returns CharacteristicRead BleResponse`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockContext: Context = mockk()
      val mockDevice: BluetoothDevice = MockBluetoothDevice.Builder().build()
      val mockCharacteristic1 =
        MockBluetoothGattCharacteristic.Builder()
          .setUUID("00000001-0000-1000-8000-00805F9B34FB")
          .build()
      val mockCharacteristic2 = MockBluetoothGattCharacteristic.Builder().build()
      val mockService =
        MockBluetoothGattService.Builder()
          .setCharacteristics(listOf(mockCharacteristic1, mockCharacteristic2))
          .build()
      val mockGatt: BluetoothGatt =
        MockBluetoothGatt.Builder()
          .setServices(listOf(mockService))
          .build()
      val mockEventBus =
        MockMutableSharedFlow<NativeBleEvent>(
          events =
            listOf(
              OnCharacteristicRead(
                gatt = mockGatt,
                characteristic = mockCharacteristic2,
                value = byteArrayOf(),
                status = BluetoothGatt.GATT_SUCCESS,
              ),
            ),
          subscriptionCount = mockk<StateFlow<Int>>(),
        )

      launch {
        // Prepare object under test
        val gattCallbackHandler = GattCallbackHandler(this)
        gattCallbackHandler._nativeEventBus = mockEventBus
        val peripheral =
          Peripheral(
            device = mockDevice,
            coroutineScope = this,
            context = mockContext,
            gattCallbackHandler = gattCallbackHandler,
          ).apply {
            gatt = mockGatt
          }

        // Act
        peripheral.readCharacteristic(TEST_UUID) { bleEvent ->
          // Assert
          Assert.assertTrue(bleEvent is BleResponse.CharacteristicRead)
          this.cancel()
        }
      }
    }

  @Test
  fun `ReadCharacteristic with Incorrect UUID returns an Error BleResponse`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockContext: Context = mockk()
      val mockDevice: BluetoothDevice = MockBluetoothDevice.Builder().build()
      val mockCharacteristic =
        MockBluetoothGattCharacteristic.Builder()
          .setUUID("00000001-0000-1000-8000-00805F9B34FB")
          .build()
      val mockService =
        MockBluetoothGattService.Builder()
          .setCharacteristics(listOf(mockCharacteristic))
          .build()
      val mockGatt: BluetoothGatt =
        MockBluetoothGatt.Builder()
          .setServices(listOf(mockService))
          .build()

      launch {
        // Prepare object under test
        val peripheral =
          Peripheral(
            device = mockDevice,
            coroutineScope = this,
            context = mockContext,
            gattCallbackHandler = GattCallbackHandler(this),
          ).apply {
            gatt = mockGatt
          }

        // Act
        peripheral.readCharacteristic("INVALID UUID") { bleEvent ->
          // Assert
          Assert.assertTrue(bleEvent is BleResponse.Error)
          this.cancel()
        }
      }
    }

  @Test
  fun `ReadCharacteristic with Invalid UUID returns an Error BleResponse`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockContext: Context = mockk()
      val mockDevice: BluetoothDevice = MockBluetoothDevice.Builder().build()
      val mockCharacteristic = MockBluetoothGattCharacteristic.Builder().build()
      val mockService =
        MockBluetoothGattService.Builder()
          .setCharacteristics(listOf(mockCharacteristic))
          .build()
      val mockGatt: BluetoothGatt =
        MockBluetoothGatt.Builder()
          .setServices(listOf(mockService))
          .build()

      launch {
        // Prepare object under test
        val peripheral =
          Peripheral(
            device = mockDevice,
            coroutineScope = this,
            context = mockContext,
            gattCallbackHandler = GattCallbackHandler(this),
          ).apply {
            gatt = mockGatt
          }

        // Act
        peripheral.readCharacteristic("INVALID UUID") { bleEvent ->
          // Assert
          Assert.assertTrue(bleEvent is BleResponse.Error)
          this.cancel()
        }
      }
    }

  @Test
  fun `ReadCharacteristic returns an CharacteristicRead BleResponse`() =
    runTest {
      launch {
        // Arrange
        // Mocked Fixtures
        val mockCharacteristic = MockBluetoothGattCharacteristic.Builder().build()
        val mockService =
          MockBluetoothGattService.Builder()
            .setCharacteristics(listOf(mockCharacteristic))
            .build()
        val mockedCallbackHandler =
          MockBluetoothGattCallback.Builder()
            .setCoroutineScope(this)
            .setCallbackResponse(
              OnCharacteristicRead(
                gatt = MockBluetoothGatt.Builder().build(),
                characteristic = mockCharacteristic,
                value = byteArrayOf(),
                status = BluetoothGatt.GATT_SUCCESS,
              ),
            )
            .build()
        val mockGatt: BluetoothGatt =
          MockBluetoothGatt.Builder()
            .setCallbackHandler(
              mockedCallbackHandler,
            )
            .setServices(listOf(mockService))
            .build()
        val mockContext: Context = mockk()
        val mockDevice: BluetoothDevice =
          MockBluetoothDevice.Builder()
            .build()

        // Prepare object under test
        val peripheral =
          Peripheral(
            device = mockDevice,
            coroutineScope = this,
            context = mockContext,
            gattCallbackHandler = mockedCallbackHandler,
          ).apply {
            gatt = mockGatt
          }

        // Act
        peripheral.readCharacteristic(TEST_UUID) { bleEvent ->
          // Assert
          Assert.assertTrue(bleEvent is BleResponse.CharacteristicRead)
          this.cancel()
        }
      }
    }
}
