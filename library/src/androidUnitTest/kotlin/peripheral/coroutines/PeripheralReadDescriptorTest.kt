package peripheral.coroutines

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.content.Context
import com.sherlockblue.kmpble.ble.BleResponse
import com.sherlockblue.kmpble.ble.NativeBleEvent
import com.sherlockblue.kmpble.ble.callbacks.GattCallbackHandler
import com.sherlockblue.kmpble.ble.callbacks.OnDescriptorRead
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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class PeripheralReadDescriptorTest {
  // readDescriptor

  @Test
  fun `ReadDescriptor with null Gatt returns an Error BleResponse`() =
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

        // Assert
        Assert.assertTrue(
          peripheral.readDescriptor(
            characteristicUUID = DEFAULT_UUID,
            descriptorUUID = DEFAULT_UUID,
          ) is BleResponse.Error,
        )

        this.cancel()
      }
    }

  @Test
  fun `ReadDescriptor with invalid Descriptor UUID returns an Error BleResponse`() =
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

        // Assert
        Assert.assertTrue(
          peripheral.readDescriptor(
            characteristicUUID = DEFAULT_UUID,
            descriptorUUID = "Invalid UUID",
          ) is BleResponse.Error,
        )

        this.cancel()
      }
    }

  @Test
  fun `ReadDescriptor with invalid Characteristic UUID returns an Error BleResponse`() =
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

        // Assert
        Assert.assertTrue(
          peripheral.readDescriptor(
            characteristicUUID = "Invalid UUID",
            descriptorUUID = DEFAULT_UUID,
          ) is BleResponse.Error,
        )

        this.cancel()
      }
    }

  @Test
  fun `ReadDescriptor returns DescriptorRead BleResponse`() =
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
        MockMutableSharedFlow<NativeBleEvent>(
          events =
            listOf(
              OnDescriptorRead(
                gatt = mockGatt,
                descriptor = mockDescriptor,
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

        // Assert
        Assert.assertTrue(
          peripheral.readDescriptor(
            characteristicUUID = DEFAULT_UUID,
            descriptorUUID = DEFAULT_UUID,
          ) is BleResponse.DescriptorRead,
        )

        this.cancel()
      }
    }

  @Test
  fun `ReadDescriptor with Invalid UUID returns ErrorCallback BleResponse`() =
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

        // Assert
        Assert.assertTrue(
          peripheral.readDescriptor(
            characteristicUUID = DEFAULT_UUID,
            descriptorUUID = "Invalid UUID",
          ) is BleResponse.Error,
        )

        this.cancel()
      }
    }

  @Test
  fun `ReadDescriptor returns an DescriptorRead BleResponse`() =
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
        val mockedCallbackHandler =
          MockBluetoothGattCallback.Builder()
            .setCoroutineScope(this)
            .setCallbackResponse(
              OnDescriptorRead(
                gatt = MockBluetoothGatt.Builder().build(),
                descriptor = mockDescriptor,
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

        // Assert
        Assert.assertTrue(peripheral.readDescriptor(DEFAULT_UUID, DEFAULT_UUID) is BleResponse.DescriptorRead)

        this.cancel()
      }
    }
}
