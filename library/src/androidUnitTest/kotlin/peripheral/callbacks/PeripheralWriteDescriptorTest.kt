package peripheral.callbacks

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.content.Context
import com.sherlockblue.kmpble.ble.callbacks.BleEvent
import com.sherlockblue.kmpble.ble.callbacks.GattCallbackHandler
import com.sherlockblue.kmpble.ble.fixtures.DEFAULT_UUID
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothDevice
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGatt
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGattCallback
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGattCharacteristic
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGattDescriptor
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGattService
import com.sherlockblue.kmpble.ble.fixtures.MockMutableSharedFlow
import com.sherlockblue.kmpble.peripheral.Peripheral
import io.mockk.mockk
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class PeripheralWriteDescriptorTest {
  // writeDescriptor

  @Test
  fun `WriteDescriptor with null Gatt returns CallbackError BleEvent`() =
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
        peripheral.writeDescriptor(
          characteristicUUID = DEFAULT_UUID,
          descriptorUUID = DEFAULT_UUID,
          data = byteArrayOf(),
        ) { bleEvent ->
          // Assert
          Assert.assertTrue(
            bleEvent is BleEvent.CallbackError,
          )
        }
      }
    }

  @Test
  fun `WriteDescriptor with invalid Descriptor UUID returns CallbackError BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockContext: Context = mockk()
      val mockDevice: BluetoothDevice = MockBluetoothDevice.Builder().build()
      val mockDescriptor = MockBluetoothGattDescriptor.Builder().build()
      val mockCharacteristic =
        MockBluetoothGattCharacteristic.Builder()
          .setDescriptors(listOf(mockDescriptor))
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
        peripheral.writeDescriptor(
          characteristicUUID = DEFAULT_UUID,
          descriptorUUID = "INVALID UUID",
          data = byteArrayOf(),
        ) { bleEvent ->
          // Assert
          Assert.assertTrue(
            bleEvent is BleEvent.CallbackError,
          )
        }
      }
    }

  @Test
  fun `WriteDescriptor with invalid Characteristic UUID returns CallbackError BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockContext: Context = mockk()
      val mockDevice: BluetoothDevice = MockBluetoothDevice.Builder().build()
      val mockDescriptor = MockBluetoothGattDescriptor.Builder().build()
      val mockCharacteristic =
        MockBluetoothGattCharacteristic.Builder()
          .setDescriptors(listOf(mockDescriptor))
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
        peripheral.writeDescriptor(
          characteristicUUID = "INVALID UUID",
          descriptorUUID = DEFAULT_UUID,
          data = byteArrayOf(),
        ) { bleEvent ->
          // Assert
          Assert.assertTrue(
            bleEvent is BleEvent.CallbackError,
          )
        }
      }
    }

  @Test
  fun `WriteDescriptor returns OnDescriptorWrite BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockContext: Context = mockk()
      val mockDevice: BluetoothDevice = MockBluetoothDevice.Builder().build()
      val mockDescriptor = MockBluetoothGattDescriptor.Builder().build()
      val mockCharacteristic =
        MockBluetoothGattCharacteristic.Builder()
          .setDescriptors(listOf(mockDescriptor))
          .build()
      val mockService =
        MockBluetoothGattService.Builder()
          .setCharacteristics(listOf(mockCharacteristic))
          .build()
      val mockGatt: BluetoothGatt =
        MockBluetoothGatt.Builder()
          .setServices(listOf(mockService))
          .build()
      val mockEventBus =
        MockMutableSharedFlow<BleEvent>(
          events =
            listOf(
              BleEvent.OnDescriptorWrite(
                gatt = mockGatt,
                descriptor = mockDescriptor,
                status = BluetoothGatt.GATT_SUCCESS,
              ),
            ),
          subscriptionCount = mockk<StateFlow<Int>>(),
        )

      launch {
        // Prepare object under test
        val gattCallbackHandler = GattCallbackHandler(this)
        gattCallbackHandler._eventBus = mockEventBus
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
        peripheral.writeDescriptor(
          characteristicUUID = DEFAULT_UUID,
          descriptorUUID = DEFAULT_UUID,
          data = byteArrayOf(),
        ) { bleEvent ->
          // Assert
          Assert.assertTrue(
            bleEvent is BleEvent.OnDescriptorWrite,
          )
        }
      }
    }

  @Test
  fun `WriteDescriptor returns an OnDescriptorWrite BleEvent`() =
    runTest {
      launch {
        // Arrange
        // Mocked Fixtures
        val mockDescriptor = MockBluetoothGattDescriptor.Builder().build()
        val mockCharacteristic =
          MockBluetoothGattCharacteristic.Builder()
            .setDescriptors(listOf(mockDescriptor))
            .build()
        val mockService =
          MockBluetoothGattService.Builder()
            .setCharacteristics(listOf(mockCharacteristic))
            .build()
        val mockBleEvent =
          BleEvent.OnDescriptorWrite(
            gatt = mockk<BluetoothGatt>(relaxed = true),
            descriptor = mockDescriptor,
            status = BluetoothGatt.GATT_SUCCESS,
          )
        val mockedCallbackHandler =
          MockBluetoothGattCallback.Builder()
            .setCoroutineScope(this)
            .setCallbackResponse(mockBleEvent)
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
        peripheral.writeDescriptor(
          characteristicUUID = DEFAULT_UUID,
          descriptorUUID = DEFAULT_UUID,
          data = byteArrayOf(),
        ) { bleEvent ->
          // Assert
          Assert.assertTrue(
            bleEvent is BleEvent.OnDescriptorWrite,
          )
        }
      }
    }
}
