package peripheral

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.content.Context
import com.sherlockblue.kmpble.ble.BleResponse
import com.sherlockblue.kmpble.ble.NativeBleEvent
import com.sherlockblue.kmpble.ble.callbacks.GattCallbackHandler
import com.sherlockblue.kmpble.ble.callbacks.OnConnectionStateChange
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothDevice
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGatt
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGattCharacteristic
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGattDescriptor
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGattService
import com.sherlockblue.kmpble.ble.fixtures.MockMutableSharedFlow
import com.sherlockblue.kmpble.peripheral.Peripheral
import io.mockk.mockk
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class PeripheralServicesTest {
  @Test
  fun `Services function returns Correct Services structure`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockContext: Context = mockk()
      val mockDevice: BluetoothDevice =
        MockBluetoothDevice.Builder()
          .setCallbackHandler(mockk<GattCallbackHandler>(relaxed = true))
          .build()
      val mockGatt: BluetoothGatt =
        MockBluetoothGatt.Builder()
          .setServices(
            listOf(
              MockBluetoothGattService.Builder()
                .setCharacteristics(
                  listOf(
                    MockBluetoothGattCharacteristic.Builder().setDescriptors(listOf(MockBluetoothGattDescriptor.Builder().build())).build(),
                  ),
                )
                .build(),
            ),
          )
          .build()
      val mockEventBus =
        MockMutableSharedFlow<NativeBleEvent>(
          events =
            listOf(
              OnConnectionStateChange(
                gatt = mockGatt,
                status = BluetoothGatt.GATT_SUCCESS,
                newState = BluetoothGatt.STATE_CONNECTED,
              ),
            ),
          subscriptionCount = mockk<StateFlow<Int>>(),
        )

      launch {
        // Prepare object under test
        val gattCallbackHandler = GattCallbackHandler(this)
        gattCallbackHandler._nativeEventBus = mockEventBus
        val peripheral =
          Peripheral(device = mockDevice, coroutineScope = this, context = mockContext, gattCallbackHandler = gattCallbackHandler)

        // Act
        Assert.assertTrue(peripheral.getServices().isEmpty())
        peripheral.connect { bleEvent ->
          Assert.assertTrue(bleEvent is BleResponse.ConnectionStateChange)
          Assert.assertTrue(peripheral.getServices().first().characteristics.first().descriptors.size == 1)
          this.cancel()
        }
      }
    }

  @Test
  fun `Gatt with null services returns empty Services structure`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockContext: Context = mockk()
      val mockDevice: BluetoothDevice =
        MockBluetoothDevice.Builder()
          .setCallbackHandler(mockk<GattCallbackHandler>(relaxed = true))
          .build()
      val mockGatt: BluetoothGatt =
        MockBluetoothGatt.Builder()
          .setServices(null)
          .build()
      val mockEventBus =
        MockMutableSharedFlow<NativeBleEvent>(
          events =
            listOf(
              OnConnectionStateChange(
                gatt = mockGatt,
                status = BluetoothGatt.GATT_SUCCESS,
                newState = BluetoothGatt.STATE_CONNECTED,
              ),
            ),
          subscriptionCount = mockk<StateFlow<Int>>(),
        )

      launch {
        // Prepare object under test
        val gattCallbackHandler = GattCallbackHandler(this)
        gattCallbackHandler._nativeEventBus = mockEventBus
        val peripheral =
          Peripheral(device = mockDevice, coroutineScope = this, context = mockContext, gattCallbackHandler = gattCallbackHandler)

        // Act
        Assert.assertTrue(peripheral.getServices().isEmpty())
        peripheral.connect { bleEvent ->
          Assert.assertTrue(bleEvent is BleResponse.ConnectionStateChange)
          Assert.assertTrue(peripheral.getServices().isEmpty())
          this.cancel()
        }
      }
    }

  @Test
  fun `Null Gatt returns empty Services structure`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockContext: Context = mockk()
      val mockDevice: BluetoothDevice =
        MockBluetoothDevice.Builder()
          .setCallbackHandler(mockk<GattCallbackHandler>(relaxed = true))
          .build()
      val mockGatt: BluetoothGatt =
        MockBluetoothGatt.Builder()
          .setServices(null)
          .build()
      val mockEventBus =
        MockMutableSharedFlow<NativeBleEvent>(
          events =
            listOf(
              OnConnectionStateChange(
                gatt = null,
                status = BluetoothGatt.GATT_SUCCESS,
                newState = BluetoothGatt.STATE_CONNECTED,
              ),
            ),
          subscriptionCount = mockk<StateFlow<Int>>(),
        )

      launch {
        // Prepare object under test
        val gattCallbackHandler = GattCallbackHandler(this)
        gattCallbackHandler._nativeEventBus = mockEventBus
        val peripheral =
          Peripheral(device = mockDevice, coroutineScope = this, context = mockContext, gattCallbackHandler = gattCallbackHandler)

        // Act
        Assert.assertTrue(peripheral.getServices().isEmpty())
        peripheral.connect { bleEvent ->
          Assert.assertTrue(bleEvent is BleResponse.ConnectionStateChange)
          Assert.assertTrue(peripheral.getServices().isEmpty())
          this.cancel()
        }
      }
    }
}
