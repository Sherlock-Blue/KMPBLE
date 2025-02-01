package peripheral.callbacks

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.content.Context
import com.sherlockblue.kmpble.ble.BleResponse
import com.sherlockblue.kmpble.ble.NativeBleEvent
import com.sherlockblue.kmpble.ble.callbacks.GattCallbackHandler
import com.sherlockblue.kmpble.ble.callbacks.OnServicesDiscovered
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothDevice
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGatt
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGattCallback
import com.sherlockblue.kmpble.ble.fixtures.MockMutableSharedFlow
import com.sherlockblue.kmpble.peripheral.Peripheral
import io.mockk.mockk
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class PeripheralDiscoverServicesTest {
  // disconnect

  @Test
  fun `DiscoverServices with null Gatt returns an Error BleResponse`() =
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
        peripheral.discoverServices { bleEvent ->
          // Assert
          Assert.assertTrue(bleEvent is BleResponse.Error)
          this.cancel()
        }
      }
    }

  @Test
  fun `DiscoverServices returns ServicesDiscovered BleResponse`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockContext: Context = mockk()
      val mockDevice: BluetoothDevice =
        MockBluetoothDevice.Builder()
          .setCallbackHandler(mockk<GattCallbackHandler>(relaxed = true))
          .build()
      val mockEventBus =
        MockMutableSharedFlow<NativeBleEvent>(
          events =
            listOf(
              OnServicesDiscovered(
                gatt = MockBluetoothGatt.Builder().build(),
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
            gatt = mockk<BluetoothGatt>(relaxed = true)
          }

        // Act
        peripheral.discoverServices { bleEvent ->
          // Assert
          Assert.assertTrue(bleEvent is BleResponse.ServicesDiscovered)
          this.cancel()
        }
      }
    }

  @Test
  fun `DiscoverServices returns an ServicesDiscovered BleResponse`() =
    runTest {
      launch {
        // Arrange
        // Mocked Fixtures
        val mockedCallbackHandler =
          MockBluetoothGattCallback.Builder()
            .setCoroutineScope(this)
            .setCallbackResponse(
              OnServicesDiscovered(
                gatt = MockBluetoothGatt.Builder().build(),
                status = BluetoothGatt.GATT_SUCCESS,
              ),
            )
            .build()
        val mockGatt: BluetoothGatt =
          MockBluetoothGatt.Builder()
            .setCallbackHandler(
              mockedCallbackHandler,
            )
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
        peripheral.discoverServices { bleEvent ->
          // Assert
          Assert.assertTrue(bleEvent is BleResponse.ServicesDiscovered)
          this.cancel()
        }
      }
    }
}
