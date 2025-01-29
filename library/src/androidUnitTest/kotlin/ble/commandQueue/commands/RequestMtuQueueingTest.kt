package com.sherlockblue.kmpble.ble.commandQueue.commands

import android.bluetooth.BluetoothGatt
import com.sherlockblue.kmpble.DEFAULT_MTU_SIZE
import com.sherlockblue.kmpble.MAX_MTU_SIZE
import com.sherlockblue.kmpble.ble.callbacks.BleEvent
import com.sherlockblue.kmpble.ble.callbacks.GattCallbackHandler
import com.sherlockblue.kmpble.ble.commandQueue.CommandQueue
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGatt
import com.sherlockblue.kmpble.ble.fixtures.MockSharedFlow
import io.mockk.mockk
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class RequestMtuQueueingTest {
  @Test
  fun `Command enqueues itself on creation`() =
    runTest {
      launch {
        // Arrange
        val commandQueue = CommandQueue()
        val command =
          RequestMtu(
            gatt = mockk<BluetoothGatt>(relaxed = true),
            mtu = MAX_MTU_SIZE,
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
  fun `Command dequeues itself on OnMtuChanged BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockEventBus =
        MockSharedFlow<BleEvent>(
          events =
            listOf(
              BleEvent.OnMtuChanged(
                gatt = MockBluetoothGatt.Builder().build(),
                status = BluetoothGatt.STATE_CONNECTED,
                mtu = MAX_MTU_SIZE,
              ),
            ),
        )

      // Act
      val commandQueue = CommandQueue()
      launch {
        RequestMtu(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          mtu = MAX_MTU_SIZE,
          bleQueue = commandQueue,
          coroutineScope = this,
          gattCallbackEventBus = mockEventBus,
        ) { }
      }.join()

      // Assert
      Assert.assertTrue(commandQueue.peek() == null)
    }

  @Test
  fun `Command executes callback with OnMtuChanged BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleEvent =
        BleEvent.OnMtuChanged(
          gatt = MockBluetoothGatt.Builder().build(),
          status = BluetoothGatt.STATE_CONNECTED,
          mtu = MAX_MTU_SIZE,
        )
      val mockEventBus =
        MockSharedFlow<BleEvent>(
          events =
            listOf(mockBleEvent),
        )

      // Act
      val commandQueue = CommandQueue()
      launch {
        RequestMtu(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          mtu = MAX_MTU_SIZE,
          bleQueue = commandQueue,
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
  fun `Command dequeues itself on onMtuChanged callback`() =
    runTest {
      // Arrange
      val commandQueue = CommandQueue()
      launch {
        val gattCallbackHandler = GattCallbackHandler(this)
        RequestMtu(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          mtu = MAX_MTU_SIZE,
          bleQueue = commandQueue,
          coroutineScope = this,
          gattCallbackEventBus = gattCallbackHandler.eventBus(),
        ) { }

        gattCallbackHandler.onMtuChanged(
          gatt = MockBluetoothGatt.Builder().build(),
          status = BluetoothGatt.GATT_SUCCESS,
          mtu = MAX_MTU_SIZE,
        )
      }.join()

      // Assert
      Assert.assertTrue(commandQueue.peek() == null)
    }

  @Test
  fun `Command only recognizes onMtuChanged callbacks`() =
    runTest {
      launch {
        // Arrange
        val gattCallbackHandler = GattCallbackHandler(this)
        RequestMtu(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          mtu = MAX_MTU_SIZE,
          bleQueue = CommandQueue(),
          coroutineScope = this,
          gattCallbackEventBus = gattCallbackHandler.eventBus(),
        ) { event ->
          // Assert
          Assert.assertTrue(event is BleEvent.OnMtuChanged)
        }

        gattCallbackHandler.onServiceChanged(
          gatt = MockBluetoothGatt.Builder().build(),
        )

        gattCallbackHandler.onMtuChanged(
          gatt = MockBluetoothGatt.Builder().build(),
          status = BluetoothGatt.GATT_SUCCESS,
          mtu = MAX_MTU_SIZE,
        )
      }.join()
    }

  @Test
  fun `Command only processes first onMtuChanged callback`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt1: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockBleGatt2: BluetoothGatt = MockBluetoothGatt.Builder().build()

      // Act
      launch {
        val gattCallbackHandler = GattCallbackHandler(this)
        RequestMtu(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          mtu = MAX_MTU_SIZE,
          bleQueue = CommandQueue(),
          coroutineScope = this,
          gattCallbackEventBus = gattCallbackHandler.eventBus(),
        ) { event ->
          // Assert
          Assert.assertTrue((event as BleEvent.OnMtuChanged).gatt === mockBleGatt1) // Same instance
          Assert.assertTrue((event as BleEvent.OnMtuChanged).status == BluetoothGatt.STATE_DISCONNECTING)
          Assert.assertTrue((event as BleEvent.OnMtuChanged).mtu == DEFAULT_MTU_SIZE)
        }

        gattCallbackHandler.onServiceChanged(
          gatt = MockBluetoothGatt.Builder().build(),
        )

        gattCallbackHandler.onMtuChanged(
          gatt = mockBleGatt1,
          status = BluetoothGatt.STATE_DISCONNECTING,
          mtu = DEFAULT_MTU_SIZE,
        )

        gattCallbackHandler.onMtuChanged(
          gatt = mockBleGatt2,
          status = BluetoothGatt.STATE_DISCONNECTED,
          mtu = MAX_MTU_SIZE,
        )
      }.join()
    }
}
