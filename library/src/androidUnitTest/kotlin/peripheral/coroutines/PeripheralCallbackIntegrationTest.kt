package peripheral.coroutines

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.content.Context
import com.sherlockblue.kmpble.ble.callbacks.GattCallbackHandler
import com.sherlockblue.kmpble.ble.callbacks.OnConnectionStateChange
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGatt
import com.sherlockblue.kmpble.peripheral.Peripheral
import io.mockk.mockk
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class PeripheralCallbackIntegrationTest {
  // integration with GattCallbackHandler

  @Test
  fun `BleEvents are passed from the GattCallbackHandler to the eventBus`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockContext: Context = mockk()
      val mockDevice: BluetoothDevice = mockk()

      launch {
        // Prepare object under test
        val gattCallbackHandler = GattCallbackHandler(this)
        val peripheral =
          Peripheral(device = mockDevice, coroutineScope = this, context = mockContext, gattCallbackHandler = gattCallbackHandler)

        // Act
        gattCallbackHandler.onConnectionStateChange(
          gatt = MockBluetoothGatt.Builder().build(),
          status = BluetoothGatt.GATT_SUCCESS,
          newState = BluetoothGatt.STATE_CONNECTED,
        )

        // Assert
        Assert.assertTrue(peripheral.nativeEventBus().first() is OnConnectionStateChange)

        this.cancel()
      }
    }
}
