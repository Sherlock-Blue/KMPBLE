package com.sherlockblue.kmpble.ble.commandQueue.commands

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
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

class ReadCharacteristicQueueingTest {
  @Test
  fun `Command enqueues itself on creation`() =
    runTest {
      // Arrange
      launch {
        val commandQueue = CommandQueue()
        val command =
          ReadCharacteristic(
            gatt = mockk<BluetoothGatt>(relaxed = true),
            characteristic = MockBluetoothGattCharacteristic.Builder().build(),
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
  fun `Command dequeues itself on OnCharacteristicRead BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockEventBus =
        MockSharedFlow<BleEvent>(
          events =
            listOf(
              BleEvent.OnCharacteristicRead(
                gatt = MockBluetoothGatt.Builder().build(),
                characteristic = MockBluetoothGattCharacteristic.Builder().build(),
                value = byteArrayOf(),
                status = BluetoothGatt.GATT_SUCCESS,
              ),
            ),
        )

      // Act
      val commandQueue = CommandQueue()
      launch {
        ReadCharacteristic(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          characteristic = MockBluetoothGattCharacteristic.Builder().build(),
          bleQueue = commandQueue,
          coroutineScope = this,
          gattCallbackEventBus = mockEventBus,
        ) { }
      }.join()

      // Assert
      Assert.assertTrue(commandQueue.peek() == null)
    }

  @Test
  fun `Command executes callback with OnCharacteristicRead BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleEvent =
        BleEvent.OnCharacteristicRead(
          gatt = MockBluetoothGatt.Builder().build(),
          characteristic = MockBluetoothGattCharacteristic.Builder().build(),
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
        ReadCharacteristic(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          characteristic = MockBluetoothGattCharacteristic.Builder().build(),
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
  fun `Command dequeues itself on onCharacteristicRead callback`() =
    runTest {
      // Arrange
      val commandQueue = CommandQueue()
      launch {
        val gattCallbackHandler = GattCallbackHandler(this)
        ReadCharacteristic(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          characteristic = MockBluetoothGattCharacteristic.Builder().build(),
          bleQueue = commandQueue,
          coroutineScope = this,
          gattCallbackEventBus = gattCallbackHandler.eventBus(),
        ) { }

        gattCallbackHandler.onCharacteristicRead(
          gatt = MockBluetoothGatt.Builder().build(),
          characteristic = MockBluetoothGattCharacteristic.Builder().build(),
          value = byteArrayOf(),
          status = BluetoothGatt.GATT_SUCCESS,
        )
      }.join()

      // Assert
      Assert.assertTrue(commandQueue.peek() == null)
    }

  @Test
  fun `Command only recognizes onCharacteristicRead callbacks`() =
    runTest {
      launch {
        // Arrange
        val gattCallbackHandler = GattCallbackHandler(this)
        ReadCharacteristic(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          characteristic = MockBluetoothGattCharacteristic.Builder().build(),
          bleQueue = CommandQueue(),
          coroutineScope = this,
          gattCallbackEventBus = gattCallbackHandler.eventBus(),
        ) { event ->
          // Assert
          Assert.assertTrue(event is BleEvent.OnCharacteristicRead)
        }

        gattCallbackHandler.onServiceChanged(
          gatt = MockBluetoothGatt.Builder().build(),
        )

        gattCallbackHandler.onCharacteristicRead(
          gatt = MockBluetoothGatt.Builder().build(),
          characteristic = MockBluetoothGattCharacteristic.Builder().build(),
          value = byteArrayOf(),
          status = BluetoothGatt.GATT_SUCCESS,
        )
      }.join()
    }

  @Test
  fun `Command only processes first onCharacteristicRead callback`() =
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
        ReadCharacteristic(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          characteristic = MockBluetoothGattCharacteristic.Builder().build(),
          bleQueue = CommandQueue(),
          coroutineScope = this,
          gattCallbackEventBus = gattCallbackHandler.eventBus(),
        ) { event ->
          // Assert
          Assert.assertTrue((event as BleEvent.OnCharacteristicRead).gatt === mockBleGatt1) // Same instance
          Assert.assertTrue((event as BleEvent.OnCharacteristicRead).characteristic === mockBleCharacteristic1) // Same instance
          Assert.assertTrue((event as BleEvent.OnCharacteristicRead).status == BluetoothGatt.STATE_DISCONNECTED)
        }

        gattCallbackHandler.onServiceChanged(
          gatt = MockBluetoothGatt.Builder().build(),
        )

        gattCallbackHandler.onCharacteristicRead(
          gatt = mockBleGatt1,
          characteristic = mockBleCharacteristic1,
          value = byteArrayOf(),
          status = BluetoothGatt.STATE_DISCONNECTED,
        )

        gattCallbackHandler.onCharacteristicRead(
          gatt = mockBleGatt2,
          characteristic = mockBleCharacteristic2,
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
        ReadCharacteristic(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          characteristic = mockBleCharacteristic2,
          bleQueue = CommandQueue(),
          coroutineScope = this,
          gattCallbackEventBus = gattCallbackHandler.eventBus(),
        ) { event ->
          // Assert
          Assert.assertTrue((event as BleEvent.OnCharacteristicRead).gatt === mockBleGatt2) // Same instance
          Assert.assertTrue((event as BleEvent.OnCharacteristicRead).characteristic === mockBleCharacteristic2) // Same instance
          Assert.assertTrue((event as BleEvent.OnCharacteristicRead).status == BluetoothGatt.STATE_DISCONNECTED)
        }

        gattCallbackHandler.onServiceChanged(
          gatt = MockBluetoothGatt.Builder().build(),
        )

        gattCallbackHandler.onCharacteristicRead(
          gatt = mockBleGatt1,
          characteristic = mockBleCharacteristic1,
          value = byteArrayOf(),
          status = BluetoothGatt.GATT_SUCCESS,
        )

        gattCallbackHandler.onCharacteristicRead(
          gatt = mockBleGatt2,
          characteristic = mockBleCharacteristic2,
          value = byteArrayOf(),
          status = BluetoothGatt.STATE_DISCONNECTED,
        )
      }.join()
    }
}
