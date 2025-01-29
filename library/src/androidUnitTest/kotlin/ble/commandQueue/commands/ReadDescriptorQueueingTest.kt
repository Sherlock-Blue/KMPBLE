package com.sherlockblue.kmpble.ble.commandQueue.commands

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattDescriptor
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

class ReadDescriptorQueueingTest {
  @Test
  fun `Command enqueues itself on creation`() =
    runTest {
      launch {
        // Arrange
        val commandQueue = CommandQueue()
        val command =
          ReadDescriptor(
            gatt = mockk<BluetoothGatt>(relaxed = true),
            descriptor = MockBluetoothGattDescriptor.Builder().build(),
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
  fun `Command dequeues itself on OnDescriptorRead BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockEventBus =
        MockSharedFlow<BleEvent>(
          events =
            listOf(
              BleEvent.OnDescriptorRead(
                gatt = MockBluetoothGatt.Builder().build(),
                descriptor = MockBluetoothGattDescriptor.Builder().build(),
                value = byteArrayOf(),
                status = BluetoothGatt.GATT_SUCCESS,
              ),
            ),
        )

      // Act
      val commandQueue = CommandQueue()
      launch {
        ReadDescriptor(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          descriptor = MockBluetoothGattDescriptor.Builder().build(),
          bleQueue = commandQueue,
          coroutineScope = this,
          gattCallbackEventBus = mockEventBus,
        ) { }
      }.join()

      // Assert
      Assert.assertTrue(commandQueue.peek() == null)
    }

  @Test
  fun `Command executes callback with OnDescriptorRead BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleEvent =
        BleEvent.OnDescriptorRead(
          gatt = MockBluetoothGatt.Builder().build(),
          descriptor = MockBluetoothGattDescriptor.Builder().build(),
          value = byteArrayOf(),
          status = BluetoothGatt.GATT_SUCCESS,
        )
      val mockEventBus =
        MockSharedFlow<BleEvent>(
          events =
            listOf(mockBleEvent),
        )

      // Act
      launch {
        ReadDescriptor(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          descriptor = MockBluetoothGattDescriptor.Builder().build(),
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
  fun `Command dequeues itself on onDescriptorRead callback`() =
    runTest {
      // Arrange
      val commandQueue = CommandQueue()
      launch {
        val gattCallbackHandler = GattCallbackHandler(this)
        ReadDescriptor(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          descriptor = MockBluetoothGattDescriptor.Builder().build(),
          bleQueue = commandQueue,
          coroutineScope = this,
          gattCallbackEventBus = gattCallbackHandler.eventBus(),
        ) { }

        gattCallbackHandler.onDescriptorRead(
          gatt = MockBluetoothGatt.Builder().build(),
          descriptor = MockBluetoothGattDescriptor.Builder().build(),
          value = byteArrayOf(),
          status = BluetoothGatt.GATT_SUCCESS,
        )
      }.join()

      // Assert
      Assert.assertTrue(commandQueue.peek() == null)
    }

  @Test
  fun `Command only recognizes onDescriptorRead callbacks`() =
    runTest {
      launch {
        // Arrange
        val gattCallbackHandler = GattCallbackHandler(this)
        ReadDescriptor(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          descriptor = MockBluetoothGattDescriptor.Builder().build(),
          bleQueue = CommandQueue(),
          coroutineScope = this,
          gattCallbackEventBus = gattCallbackHandler.eventBus(),
        ) { event ->
          // Assert
          Assert.assertTrue(event is BleEvent.OnDescriptorRead)
        }

        gattCallbackHandler.onServiceChanged(
          gatt = MockBluetoothGatt.Builder().build(),
        )

        gattCallbackHandler.onDescriptorRead(
          gatt = MockBluetoothGatt.Builder().build(),
          descriptor = MockBluetoothGattDescriptor.Builder().build(),
          value = byteArrayOf(),
          status = BluetoothGatt.GATT_SUCCESS,
        )
      }.join()
    }

  @Test
  fun `Command only processes first onDescriptorRead callback`() =
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
        ReadDescriptor(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          descriptor = MockBluetoothGattDescriptor.Builder().build(),
          bleQueue = CommandQueue(),
          coroutineScope = this,
          gattCallbackEventBus = gattCallbackHandler.eventBus(),
        ) { event ->
          // Assert
          Assert.assertTrue((event as BleEvent.OnDescriptorRead).gatt === mockBleGatt1) // Same instance
          Assert.assertTrue((event as BleEvent.OnDescriptorRead).descriptor === mockBleDescriptor1) // Same instance
          Assert.assertTrue((event as BleEvent.OnDescriptorRead).status == BluetoothGatt.STATE_DISCONNECTED)
        }

        gattCallbackHandler.onServiceChanged(
          gatt = MockBluetoothGatt.Builder().build(),
        )

        gattCallbackHandler.onDescriptorRead(
          gatt = mockBleGatt1,
          descriptor = mockBleDescriptor1,
          value = byteArrayOf(),
          status = BluetoothGatt.STATE_DISCONNECTED,
        )

        gattCallbackHandler.onDescriptorRead(
          gatt = mockBleGatt2,
          descriptor = mockBleDescriptor2,
          value = byteArrayOf(),
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
        ReadDescriptor(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          descriptor = mockBleDescriptor2,
          bleQueue = CommandQueue(),
          coroutineScope = this,
          gattCallbackEventBus = gattCallbackHandler.eventBus(),
        ) { event ->
          // Assert
          Assert.assertTrue((event as BleEvent.OnDescriptorRead).gatt === mockBleGatt2) // Same instance
          Assert.assertTrue((event as BleEvent.OnDescriptorRead).descriptor === mockBleDescriptor2) // Same instance
          Assert.assertTrue((event as BleEvent.OnDescriptorRead).status == BluetoothGatt.STATE_DISCONNECTED)
        }

        gattCallbackHandler.onServiceChanged(
          gatt = MockBluetoothGatt.Builder().build(),
        )

        gattCallbackHandler.onDescriptorRead(
          gatt = mockBleGatt1,
          descriptor = mockBleDescriptor1,
          value = byteArrayOf(),
          status = BluetoothGatt.GATT_SUCCESS,
        )

        gattCallbackHandler.onDescriptorRead(
          gatt = mockBleGatt2,
          descriptor = mockBleDescriptor2,
          value = byteArrayOf(),
          status = BluetoothGatt.STATE_DISCONNECTED,
        )
      }.join()
    }
}
