package peripheral.coroutines

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.content.Context
import com.sherlockblue.kmpble.ble.BleResponse
import com.sherlockblue.kmpble.ble.NativeBleEvent
import com.sherlockblue.kmpble.ble.callbacks.GattCallbackHandler
import com.sherlockblue.kmpble.ble.callbacks.OnConnectionStateChange
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

class PeripheralConnectTest {
  // Connect

  @Test
  fun `Connect returns ConnectionStateChanged BleResponse`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockContext: Context = mockk()
      val mockDevice: BluetoothDevice =
        MockBluetoothDevice.Builder()
          .setCallbackHandler(mockk<GattCallbackHandler>(relaxed = true))
          .build()
      val mockGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
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
        val eventBus = peripheral.eventBus()

        // Assert
        Assert.assertTrue(peripheral.connect() is BleResponse.ConnectionStateChange)

        this.cancel()
      }
    }

  @Test
  fun `Connect returns an ConnectionStateChange BleResponse`() =
    runTest {
      launch {
        // Arrange
        // Mocked Fixtures
        val mockGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
        val mockGattCallbackHandler =
          MockBluetoothGattCallback.Builder()
            .setCoroutineScope(this)
            .setCallbackResponse(
              OnConnectionStateChange(
                gatt = mockGatt,
                status = BluetoothGatt.GATT_SUCCESS,
                newState = BluetoothGatt.STATE_CONNECTED,
              ),
            )
            .build()
        val mockContext: Context = mockk()
        val mockDevice: BluetoothDevice =
          MockBluetoothDevice.Builder()
            .setCallbackHandler(mockGattCallbackHandler)
            .build()
        // Prepare object under test
        val peripheral =
          Peripheral(
            device = mockDevice,
            coroutineScope = this,
            context = mockContext,
            gattCallbackHandler = mockGattCallbackHandler,
          )

        // Assert
        Assert.assertTrue(peripheral.connect() is BleResponse.ConnectionStateChange)

        this.cancel()
      }
    }
}
