package peripheral.coroutines

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.content.Context
import com.sherlockblue.kmpble.ble.callbacks.BleEvent
import com.sherlockblue.kmpble.ble.callbacks.GattCallbackHandler
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
  fun `Connect returns OnConnectionStateChanged BleEvent`() =
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
        MockMutableSharedFlow<BleEvent>(
          events =
            listOf(
              BleEvent.OnConnectionStateChange(
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
        gattCallbackHandler._eventBus = mockEventBus
        val peripheral =
          Peripheral(device = mockDevice, coroutineScope = this, context = mockContext, gattCallbackHandler = gattCallbackHandler)

        // Assert
        Assert.assertTrue(peripheral.connect() is BleEvent.OnConnectionStateChange)

        this.cancel()
      }
    }

  @Test
  fun `Connect saves the returned Gatt instance`() =
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
        MockMutableSharedFlow<BleEvent>(
          events =
            listOf(
              BleEvent.OnConnectionStateChange(
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
        gattCallbackHandler._eventBus = mockEventBus
        val peripheral =
          Peripheral(device = mockDevice, coroutineScope = this, context = mockContext, gattCallbackHandler = gattCallbackHandler)

        // Act
        peripheral.connect()

        // Assert
        Assert.assertTrue(peripheral.gatt === mockGatt) // Same instance

        this.cancel()
      }
    }

  @Test
  fun `Connect returns an OnConnectionStateChange BleEvent`() =
    runTest {
      launch {
        // Arrange
        // Mocked Fixtures
        val mockGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
        val mockGattCallbackHandler =
          MockBluetoothGattCallback.Builder()
            .setCoroutineScope(this)
            .setCallbackResponse(
              BleEvent.OnConnectionStateChange(
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
        Assert.assertTrue(peripheral.connect() is BleEvent.OnConnectionStateChange)

        this.cancel()
      }
    }
}
