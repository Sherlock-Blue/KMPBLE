package com.sherlockblue.kmpble.ble.commandQueue.commands

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.os.Build
import com.sherlockblue.kmpble.ble.callbacks.BleEvent
import com.sherlockblue.kmpble.ble.callbacks.GattCallbackHandler
import com.sherlockblue.kmpble.ble.commandQueue.CommandQueue
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGatt
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGattCharacteristic
import com.sherlockblue.kmpble.ble.fixtures.MockSharedFlow
import io.mockk.mockk
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class WriteCharacteristicQueueingTest {
  @Test
  fun `Command enqueues itself on creation`() =
    runTest {
      // Arrange
      launch {
        val commandQueue = CommandQueue()
        val command =
          WriteCharacteristic(
            gatt = mockk<BluetoothGatt>(relaxed = true),
            characteristic = MockBluetoothGattCharacteristic.Builder().build(),
            data = byteArrayOf(),
            bleQueue = commandQueue,
            coroutineScope = this,
            gattCallbackEventBus = GattCallbackHandler(this).eventBus(),
          ) { }

        // Assert
        Assert.assertTrue(commandQueue.peek() === command) // Same instance

        this.cancel()
      }
    }

  @Test
  fun `Command enqueues itself on creationX`() =
    runTest {
      // Arrange
      val commandQueue = CommandQueue()
      launch {
        val command =
          WriteCharacteristic(
            gatt = mockk<BluetoothGatt>(relaxed = true),
            characteristic = MockBluetoothGattCharacteristic.Builder().build(),
            data = byteArrayOf(),
            bleQueue = commandQueue,
            coroutineScope = this,
            gattCallbackEventBus = GattCallbackHandler(this).eventBus(),
          ) { }
        this.cancel()
      }.join()

      // Assert
      Assert.assertTrue(commandQueue.peek() != null) // Same instance
      // Assert.assertTrue(commandQueue.peek() == null) // Same instance
    }

  @Test
  fun `Command dequeues itself on OnCharacteristicWrite BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockEventBus =
        MockSharedFlow<BleEvent>(
          events =
            listOf(
              BleEvent.OnCharacteristicWrite(
                gatt = MockBluetoothGatt.Builder().build(),
                characteristic = MockBluetoothGattCharacteristic.Builder().build(),
                status = BluetoothGatt.GATT_SUCCESS,
              ),
            ),
        )

      // Act
      val commandQueue = CommandQueue()
      launch {
        WriteCharacteristic(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          characteristic = MockBluetoothGattCharacteristic.Builder().build(),
          data = byteArrayOf(),
          bleQueue = commandQueue,
          coroutineScope = this,
          gattCallbackEventBus = mockEventBus,
        ) { }
      }.join()

      // Assert
      Assert.assertTrue(commandQueue.peek() == null)
    }

  @Test
  fun `Command executes callback with OnCharacteristicWrite BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleEvent =
        BleEvent.OnCharacteristicWrite(
          gatt = MockBluetoothGatt.Builder().build(),
          characteristic = MockBluetoothGattCharacteristic.Builder().build(),
          status = BluetoothGatt.GATT_SUCCESS,
        )
      val mockEventBus =
        MockSharedFlow<BleEvent>(
          events =
            listOf(mockBleEvent),
        )

      // Act
      launch {
        WriteCharacteristic(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          characteristic = MockBluetoothGattCharacteristic.Builder().build(),
          data = byteArrayOf(),
          bleQueue = CommandQueue(),
          coroutineScope = this,
          gattCallbackEventBus = mockEventBus,
        ) { event ->
          // Assert
          Assert.assertTrue(event === mockBleEvent) // Same instance
        }
      }.join()
    }

  // integration with GattCallbackHandler

  @Test
  fun `Command dequeues itself on onCharacteristicWrite callback`() =
    runTest {
      // Arrange
      val commandQueue = CommandQueue()
      launch {
        val gattCallbackHandler = GattCallbackHandler(this)
        WriteCharacteristic(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          characteristic = MockBluetoothGattCharacteristic.Builder().build(),
          data = byteArrayOf(),
          bleQueue = commandQueue,
          coroutineScope = this,
          gattCallbackEventBus = gattCallbackHandler.eventBus(),
          osVersion = Build.VERSION_CODES.TIRAMISU,
        ) { }

        gattCallbackHandler.onCharacteristicWrite(
          gatt = MockBluetoothGatt.Builder().build(),
          characteristic = MockBluetoothGattCharacteristic.Builder().build(),
          status = BluetoothGatt.GATT_SUCCESS,
        )
      }.join()

      // Assert
      Assert.assertTrue(commandQueue.peek() == null)
    }

  @Test
  fun `DEPRECATED Command dequeues itself on onCharacteristicWrite callback`() =
    runTest {
      // Arrange
      val commandQueue = CommandQueue()
      launch {
        val gattCallbackHandler = GattCallbackHandler(this)
        WriteCharacteristic(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          characteristic = MockBluetoothGattCharacteristic.Builder().build(),
          data = byteArrayOf(),
          bleQueue = commandQueue,
          coroutineScope = this,
          gattCallbackEventBus = gattCallbackHandler.eventBus(),
        ) { }

        gattCallbackHandler.onCharacteristicWrite(
          gatt = MockBluetoothGatt.Builder().build(),
          characteristic = MockBluetoothGattCharacteristic.Builder().build(),
          status = BluetoothGatt.GATT_SUCCESS,
        )
      }.join()

      // Assert
      Assert.assertTrue(commandQueue.peek() == null)
    }

  @Test
  fun `Command only recognizes onCharacteristicWrite callbacks`() =
    runTest {
      // Arrange
      launch {
        val gattCallbackHandler = GattCallbackHandler(this)
        WriteCharacteristic(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          characteristic = MockBluetoothGattCharacteristic.Builder().build(),
          data = byteArrayOf(),
          bleQueue = CommandQueue(),
          coroutineScope = this,
          gattCallbackEventBus = gattCallbackHandler.eventBus(),
        ) { event ->
          // Assert
          Assert.assertTrue(event is BleEvent.OnCharacteristicWrite)
        }

        gattCallbackHandler.onServiceChanged(
          gatt = MockBluetoothGatt.Builder().build(),
        )

        gattCallbackHandler.onCharacteristicWrite(
          gatt = MockBluetoothGatt.Builder().build(),
          characteristic = MockBluetoothGattCharacteristic.Builder().build(),
          status = BluetoothGatt.GATT_SUCCESS,
        )
      }.join()
    }

  @Test
  fun `Command only processes first onCharacteristicWrite callback`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt1: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockBleGatt2: BluetoothGatt = MockBluetoothGatt.Builder().build()

      val mockBleCharacteristic1: BluetoothGattCharacteristic = MockBluetoothGattCharacteristic.Builder().build()
      val mockBleCharacteristic2: BluetoothGattCharacteristic = MockBluetoothGattCharacteristic.Builder().build()

      // Act
      launch {
        val gattCallbackHandler = GattCallbackHandler(this)
        WriteCharacteristic(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          characteristic = MockBluetoothGattCharacteristic.Builder().build(),
          data = byteArrayOf(),
          bleQueue = CommandQueue(),
          coroutineScope = this,
          gattCallbackEventBus = gattCallbackHandler.eventBus(),
        ) { event ->
          // Assert
          Assert.assertTrue((event as BleEvent.OnCharacteristicWrite).gatt === mockBleGatt1) // Same instance
          Assert.assertTrue((event as BleEvent.OnCharacteristicWrite).characteristic === mockBleCharacteristic1) // Same instance
          Assert.assertTrue((event as BleEvent.OnCharacteristicWrite).status == BluetoothGatt.STATE_DISCONNECTED)
        }

        gattCallbackHandler.onServiceChanged(
          gatt = MockBluetoothGatt.Builder().build(),
        )

        gattCallbackHandler.onCharacteristicWrite(
          gatt = mockBleGatt1,
          characteristic = mockBleCharacteristic1,
          status = BluetoothGatt.STATE_DISCONNECTED,
        )

        gattCallbackHandler.onCharacteristicWrite(
          gatt = mockBleGatt2,
          characteristic = mockBleCharacteristic2,
          status = BluetoothGatt.GATT_SUCCESS,
        )
      }.join()
    }

  @Test
  fun `Command filters multiple UUIDs`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt1: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockBleGatt2: BluetoothGatt = MockBluetoothGatt.Builder().build()

      val mockBleCharacteristic1: BluetoothGattCharacteristic =
        MockBluetoothGattCharacteristic.Builder()
          .setUUID("12345678-0000-1000-8000-00805F9B34FB")
          .build()
      val mockBleCharacteristic2: BluetoothGattCharacteristic =
        MockBluetoothGattCharacteristic.Builder()
          .setUUID("87654321-0000-1000-8000-00805F9B34FB")
          .build()

      // Act
      launch {
        val gattCallbackHandler = GattCallbackHandler(this)
        WriteCharacteristic(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          characteristic = mockBleCharacteristic2,
          data = byteArrayOf(),
          bleQueue = CommandQueue(),
          coroutineScope = this,
          gattCallbackEventBus = gattCallbackHandler.eventBus(),
        ) { event ->
          // Assert
          Assert.assertTrue((event as BleEvent.OnCharacteristicWrite).gatt === mockBleGatt2) // Same instance
          Assert.assertTrue((event as BleEvent.OnCharacteristicWrite).characteristic === mockBleCharacteristic2) // Same instance
          Assert.assertTrue((event as BleEvent.OnCharacteristicWrite).status == BluetoothGatt.STATE_DISCONNECTED)
        }

        gattCallbackHandler.onServiceChanged(
          gatt = MockBluetoothGatt.Builder().build(),
        )

        gattCallbackHandler.onCharacteristicWrite(
          gatt = mockBleGatt1,
          characteristic = mockBleCharacteristic1,
          status = BluetoothGatt.GATT_SUCCESS,
        )

        gattCallbackHandler.onCharacteristicWrite(
          gatt = mockBleGatt2,
          characteristic = mockBleCharacteristic2,
          status = BluetoothGatt.STATE_DISCONNECTED,
        )
      }.join()
    }
}
