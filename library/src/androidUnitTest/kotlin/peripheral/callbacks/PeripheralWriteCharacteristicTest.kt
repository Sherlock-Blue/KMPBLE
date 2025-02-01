package peripheral.callbacks

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.content.Context
import com.sherlockblue.kmpble.ble.BleResponse
import com.sherlockblue.kmpble.ble.NativeBleEvent
import com.sherlockblue.kmpble.ble.callbacks.GattCallbackHandler
import com.sherlockblue.kmpble.ble.callbacks.OnCharacteristicWrite
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

class PeripheralWriteCharacteristicTest {
  // writeCharacteristic

  @Test
  fun `WriteCharacteristic with null Gatt returns CallbackError BleResponse`() =
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
        peripheral.writeCharacteristic(TEST_UUID, data = byteArrayOf()) { bleEvent ->
          // Assert
          Assert.assertTrue(bleEvent is BleResponse.Error)
          this.cancel()
        }
      }
    }

  @Test
  fun `WriteCharacteristic with invalid UUID returns an Error BleResponse`() =
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
        peripheral.writeCharacteristic("INVALID UUID", data = byteArrayOf()) { bleEvent ->
          // Assert
          Assert.assertTrue(bleEvent is BleResponse.Error)
          this.cancel()
        }
      }
    }

  @Test
  fun `WriteCharacteristic returns OnCharacteristicWrite BleResponse`() =
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
      val mockEventBus =
        MockMutableSharedFlow<NativeBleEvent>(
          events =
            listOf(
              OnCharacteristicWrite(
                gatt = mockGatt,
                characteristic = mockCharacteristic,
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
        peripheral.writeCharacteristic(TEST_UUID, data = byteArrayOf()) { bleEvent ->
          // Assert
          Assert.assertTrue(bleEvent is BleResponse.CharacteristicWrite)
          this.cancel()
        }
      }
    }

  @Test
  fun `WriteCharacteristic with Invalid UUID returns an Error BleResponse`() =
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
        peripheral.writeCharacteristic("INVALID UUID", data = byteArrayOf()) { bleEvent ->
          // Assert
          Assert.assertTrue(bleEvent is BleResponse.Error)
          this.cancel()
        }
      }
    }

  @Test
  fun `WriteCharacteristic returns a CharacteristicWrite BleResponse`() =
    runTest {
      launch {
        // Arrange
        // Mocked Fixtures
        val mockCharacteristic =
          MockBluetoothGattCharacteristic.Builder()
            .build()
        val mockService =
          MockBluetoothGattService.Builder()
            .setCharacteristics(listOf(mockCharacteristic))
            .build()
        val mockBleResponse =
          OnCharacteristicWrite(
            gatt = mockk<BluetoothGatt>(relaxed = true),
            characteristic = mockCharacteristic,
            status = BluetoothGatt.GATT_SUCCESS,
          )
        val mockedCallbackHandler =
          MockBluetoothGattCallback.Builder()
            .setCoroutineScope(this)
            .setCallbackResponse(mockBleResponse)
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
        peripheral.writeCharacteristic(TEST_UUID, data = byteArrayOf()) { bleEvent ->
          // Assert
          Assert.assertTrue(bleEvent is BleResponse.CharacteristicWrite)
          this.cancel()
        }
      }
    }
}
