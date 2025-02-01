package com.sherlockblue.kmpble.ble.commandQueue.commands

import android.bluetooth.BluetoothGatt
import com.sherlockblue.kmpble.ble.NativeBleEvent
import com.sherlockblue.kmpble.ble.callbacks.GattCallbackHandler
import com.sherlockblue.kmpble.ble.callbacks.OnServicesDiscovered
import com.sherlockblue.kmpble.ble.commandQueue.CommandQueue
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGatt
import com.sherlockblue.kmpble.ble.fixtures.MockSharedFlow
import io.mockk.mockk
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class DiscoverServicesQueueingTest {
  @Test
  fun `Command enqueues itself on creation`() =
    runTest {
      // Arrange
      launch {
        val commandQueue = CommandQueue()
        val command =
          DiscoverServices(
            gatt = mockk<BluetoothGatt>(relaxed = true),
            bleQueue = commandQueue,
            coroutineScope = this,
            gattCallbackEventBus = GattCallbackHandler(this).nativeEventBus(),
          ) { }

        // Assert
        Assert.assertTrue(commandQueue.peek() === command) // Same instance

        this.cancel()
      }
    }

  @Test
  fun `Command dequeues itself on OnServicesDiscovered BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockEventBus =
        MockSharedFlow<NativeBleEvent>(
          events =
            listOf(
              OnServicesDiscovered(
                gatt = MockBluetoothGatt.Builder().build(),
                status = BluetoothGatt.GATT_SUCCESS,
              ),
            ),
        )

      // Act
      val commandQueue = CommandQueue()
      launch {
        DiscoverServices(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          bleQueue = commandQueue,
          coroutineScope = this,
          gattCallbackEventBus = mockEventBus,
        ) { }
      }.join()

      // Assert
      Assert.assertTrue(commandQueue.peek() == null)
    }

  @Test
  fun `Command executes callback with OnServicesDiscovered BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleEvent =
        OnServicesDiscovered(
          gatt = MockBluetoothGatt.Builder().build(),
          status = BluetoothGatt.GATT_SUCCESS,
        )
      val mockEventBus =
        MockSharedFlow<NativeBleEvent>(
          events =
            listOf(mockBleEvent),
        )

      // Act
      val commandQueue = CommandQueue()
      launch {
        DiscoverServices(
          gatt = mockk<BluetoothGatt>(relaxed = true),
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
  fun `Command dequeues itself on onServicesDiscovered callback`() =
    runTest {
      // Arrange
      val commandQueue = CommandQueue()
      launch {
        val gattCallbackHandler = GattCallbackHandler(this)
        DiscoverServices(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          bleQueue = commandQueue,
          coroutineScope = this,
          gattCallbackEventBus = gattCallbackHandler.nativeEventBus(),
        ) { }

        gattCallbackHandler.onServicesDiscovered(
          gatt = MockBluetoothGatt.Builder().build(),
          status = BluetoothGatt.GATT_SUCCESS,
        )
      }.join()

      // Assert
      Assert.assertTrue(commandQueue.peek() == null)
    }

  @Test
  fun `Command only recognizes onServicesDiscovered callbacks`() =
    runTest {
      // Arrange
      launch {
        val gattCallbackHandler = GattCallbackHandler(this)
        DiscoverServices(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          bleQueue = CommandQueue(),
          coroutineScope = this,
          gattCallbackEventBus = gattCallbackHandler.nativeEventBus(),
        ) { event ->
          // Assert
          Assert.assertTrue(event is OnServicesDiscovered)
        }

        gattCallbackHandler.onServiceChanged(
          gatt = MockBluetoothGatt.Builder().build(),
        )

        gattCallbackHandler.onServicesDiscovered(
          gatt = MockBluetoothGatt.Builder().build(),
          status = BluetoothGatt.GATT_SUCCESS,
        )
      }.join()
    }

  @Test
  fun `Command only processes first onServicesDiscovered callback`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt1: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockBleGatt2: BluetoothGatt = MockBluetoothGatt.Builder().build()

      // Act
      launch {
        val gattCallbackHandler = GattCallbackHandler(this)
        DiscoverServices(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          bleQueue = CommandQueue(),
          coroutineScope = this,
          gattCallbackEventBus = gattCallbackHandler.nativeEventBus(),
        ) { event ->
          // Assert
          Assert.assertTrue((event as OnServicesDiscovered).gatt === mockBleGatt1) // Same instance
          Assert.assertTrue((event as OnServicesDiscovered).status == BluetoothGatt.STATE_DISCONNECTED)
        }

        gattCallbackHandler.onServiceChanged(
          gatt = MockBluetoothGatt.Builder().build(),
        )

        gattCallbackHandler.onServicesDiscovered(
          gatt = mockBleGatt1,
          status = BluetoothGatt.STATE_DISCONNECTED,
        )

        gattCallbackHandler.onServicesDiscovered(
          gatt = mockBleGatt2,
          status = BluetoothGatt.GATT_SUCCESS,
        )
      }.join()
    }
}
