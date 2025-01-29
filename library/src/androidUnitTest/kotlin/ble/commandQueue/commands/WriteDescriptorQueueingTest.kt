package com.sherlockblue.kmpble.ble.commandQueue.commands

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattDescriptor
import android.os.Build
import com.sherlockblue.kmpble.ble.callbacks.BleEvent
import com.sherlockblue.kmpble.ble.callbacks.GattCallbackHandler
import com.sherlockblue.kmpble.ble.commandQueue.CommandQueue
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGatt
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGattDescriptor
import com.sherlockblue.kmpble.ble.fixtures.MockSharedFlow
import io.mockk.mockk
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class WriteDescriptorQueueingTest {
  @Test
  fun `Command enqueues itself on creation`() =
    runTest {
      // Arrange
      launch {
        val commandQueue = CommandQueue()
        val command =
          WriteDescriptor(
            gatt = mockk<BluetoothGatt>(relaxed = true),
            descriptor = MockBluetoothGattDescriptor.Builder().build(),
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
  fun `Command dequeues itself on OnDescriptorWrite BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockEventBus =
        MockSharedFlow<BleEvent>(
          events =
            listOf(
              BleEvent.OnDescriptorWrite(
                gatt = MockBluetoothGatt.Builder().build(),
                descriptor = MockBluetoothGattDescriptor.Builder().build(),
                status = BluetoothGatt.GATT_SUCCESS,
              ),
            ),
        )

      // Act
      val commandQueue = CommandQueue()
      launch {
        WriteDescriptor(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          descriptor = MockBluetoothGattDescriptor.Builder().build(),
          data = byteArrayOf(),
          bleQueue = commandQueue,
          coroutineScope = this,
          gattCallbackEventBus = mockEventBus,
          osVersion = Build.VERSION_CODES.TIRAMISU,
        ) { }
      }.join()

      // Assert
      Assert.assertTrue(commandQueue.peek() == null)
    }

  @Test
  fun `DEPRECATED Command dequeues itself on OnDescriptorWrite BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockEventBus =
        MockSharedFlow<BleEvent>(
          events =
            listOf(
              BleEvent.OnDescriptorWrite(
                gatt = MockBluetoothGatt.Builder().build(),
                descriptor = MockBluetoothGattDescriptor.Builder().build(),
                status = BluetoothGatt.GATT_SUCCESS,
              ),
            ),
        )

      // Act
      val commandQueue = CommandQueue()
      launch {
        WriteDescriptor(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          descriptor = MockBluetoothGattDescriptor.Builder().build(),
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
  fun `Command executes callback with OnDescriptorWrite BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleEvent =
        BleEvent.OnDescriptorWrite(
          gatt = MockBluetoothGatt.Builder().build(),
          descriptor = MockBluetoothGattDescriptor.Builder().build(),
          status = BluetoothGatt.GATT_SUCCESS,
        )
      val mockEventBus =
        MockSharedFlow<BleEvent>(
          events =
            listOf(mockBleEvent),
        )

      // Act
      launch {
        WriteDescriptor(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          descriptor = MockBluetoothGattDescriptor.Builder().build(),
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
  fun `Command dequeues itself on onDescriptorWrite callback`() =
    runTest {
      // Arrange
      val commandQueue = CommandQueue()
      launch {
        val gattCallbackHandler = GattCallbackHandler(this)
        WriteDescriptor(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          descriptor = MockBluetoothGattDescriptor.Builder().build(),
          data = byteArrayOf(),
          bleQueue = commandQueue,
          coroutineScope = this,
          gattCallbackEventBus = gattCallbackHandler.eventBus(),
        ) { }

        gattCallbackHandler.onDescriptorWrite(
          gatt = MockBluetoothGatt.Builder().build(),
          descriptor = MockBluetoothGattDescriptor.Builder().build(),
          status = BluetoothGatt.GATT_SUCCESS,
        )
      }.join()

      // Assert
      Assert.assertTrue(commandQueue.peek() == null)
    }

  @Test
  fun `Command only recognizes onDescriptorWrite callbacks`() =
    runTest {
      // Arrange
      launch {
        val gattCallbackHandler = GattCallbackHandler(this)
        WriteDescriptor(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          descriptor = MockBluetoothGattDescriptor.Builder().build(),
          data = byteArrayOf(),
          bleQueue = CommandQueue(),
          coroutineScope = this,
          gattCallbackEventBus = gattCallbackHandler.eventBus(),
        ) { event ->
          // Assert
          Assert.assertTrue(event is BleEvent.OnDescriptorWrite)
        }

        gattCallbackHandler.onServiceChanged(
          gatt = MockBluetoothGatt.Builder().build(),
        )

        gattCallbackHandler.onDescriptorWrite(
          gatt = MockBluetoothGatt.Builder().build(),
          descriptor = MockBluetoothGattDescriptor.Builder().build(),
          status = BluetoothGatt.GATT_SUCCESS,
        )
      }.join()
    }

  @Test
  fun `Command only processes first onDescriptorWrite callback`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt1: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockBleGatt2: BluetoothGatt = MockBluetoothGatt.Builder().build()

      val mockBleDescriptor1: BluetoothGattDescriptor = MockBluetoothGattDescriptor.Builder().build()
      val mockBleDescriptor2: BluetoothGattDescriptor = MockBluetoothGattDescriptor.Builder().build()

      // Act
      launch {
        val gattCallbackHandler = GattCallbackHandler(this)
        WriteDescriptor(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          descriptor = MockBluetoothGattDescriptor.Builder().build(),
          data = byteArrayOf(),
          bleQueue = CommandQueue(),
          coroutineScope = this,
          gattCallbackEventBus = gattCallbackHandler.eventBus(),
        ) { event ->
          // Assert
          Assert.assertTrue((event as BleEvent.OnDescriptorWrite).gatt === mockBleGatt1) // Same instance
          Assert.assertTrue((event as BleEvent.OnDescriptorWrite).descriptor === mockBleDescriptor1) // Same instance
          Assert.assertTrue((event as BleEvent.OnDescriptorWrite).status == BluetoothGatt.STATE_DISCONNECTED)
        }

        gattCallbackHandler.onServiceChanged(
          gatt = MockBluetoothGatt.Builder().build(),
        )

        gattCallbackHandler.onDescriptorWrite(
          gatt = mockBleGatt1,
          descriptor = mockBleDescriptor1,
          status = BluetoothGatt.STATE_DISCONNECTED,
        )

        gattCallbackHandler.onDescriptorWrite(
          gatt = mockBleGatt2,
          descriptor = mockBleDescriptor2,
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

      val mockBleDescriptor1: BluetoothGattDescriptor =
        MockBluetoothGattDescriptor.Builder()
          .setUUID("12345678-0000-1000-8000-00805F9B34FB")
          .build()
      val mockBleDescriptor2: BluetoothGattDescriptor =
        MockBluetoothGattDescriptor.Builder()
          .setUUID("87654321-0000-1000-8000-00805F9B34FB")
          .build()

      // Act
      launch {
        val gattCallbackHandler = GattCallbackHandler(this)
        WriteDescriptor(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          descriptor = mockBleDescriptor2,
          data = byteArrayOf(),
          bleQueue = CommandQueue(),
          coroutineScope = this,
          gattCallbackEventBus = gattCallbackHandler.eventBus(),
        ) { event ->
          // Assert
          Assert.assertTrue((event as BleEvent.OnDescriptorWrite).gatt === mockBleGatt2) // Same instance
          Assert.assertTrue((event as BleEvent.OnDescriptorWrite).descriptor === mockBleDescriptor2) // Same instance
          Assert.assertTrue((event as BleEvent.OnDescriptorWrite).status == BluetoothGatt.STATE_DISCONNECTED)
        }

        gattCallbackHandler.onServiceChanged(
          gatt = MockBluetoothGatt.Builder().build(),
        )

        gattCallbackHandler.onDescriptorWrite(
          gatt = mockBleGatt1,
          descriptor = mockBleDescriptor1,
          status = BluetoothGatt.GATT_SUCCESS,
        )

        gattCallbackHandler.onDescriptorWrite(
          gatt = mockBleGatt2,
          descriptor = mockBleDescriptor2,
          status = BluetoothGatt.STATE_DISCONNECTED,
        )
      }.join()
    }
}
