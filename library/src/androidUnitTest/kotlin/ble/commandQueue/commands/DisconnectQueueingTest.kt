package com.sherlockblue.kmpble.ble.commandQueue.commands

import android.bluetooth.BluetoothGatt
import com.sherlockblue.kmpble.ble.NativeBleEvent
import com.sherlockblue.kmpble.ble.callbacks.GattCallbackHandler
import com.sherlockblue.kmpble.ble.callbacks.OnConnectionStateChange
import com.sherlockblue.kmpble.ble.commandQueue.CommandQueue
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGatt
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGattCallback
import com.sherlockblue.kmpble.ble.fixtures.MockMutableSharedFlow
import io.mockk.mockk
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class DisconnectQueueingTest {
  @Test
  fun `Command enqueues itself on creation`() =
    runTest {
      // Arrange
      launch {
        val commandQueue = CommandQueue()

        // Act
        val command =
          Disconnect(
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
  fun `Command dequeues itself on OnConnectionStateChange BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleEvent =
        OnConnectionStateChange(
          gatt = MockBluetoothGatt.Builder().build(),
          status = BluetoothGatt.GATT_SUCCESS,
          newState = BluetoothGatt.STATE_DISCONNECTED,
        )
      val mockEventBus =
        MockMutableSharedFlow<NativeBleEvent>(
          events =
            listOf(mockBleEvent),
          subscriptionCount = mockk<StateFlow<Int>>(),
        )
      val mockGattCallbackHandler =
        MockBluetoothGattCallback.Builder()
          .setCoroutineScope(this)
          .setCallbackResponse(
            OnConnectionStateChange(
              gatt = MockBluetoothGatt.Builder().build(),
              status = BluetoothGatt.GATT_SUCCESS,
              newState = BluetoothGatt.STATE_CONNECTED,
            ),
          )
          .setEventBus(mockEventBus)
          .build()

      // Act
      val commandQueue = CommandQueue()
      launch {
        Disconnect(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          bleQueue = commandQueue,
          coroutineScope = this,
          gattCallbackEventBus = mockGattCallbackHandler.nativeEventBus(),
        ) { }
      }.join()

      // Assert
      Assert.assertTrue(commandQueue.peek() == null)
    }

  @Test
  fun `Command executes callback with OnConnectionStateChange BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleEvent =
        OnConnectionStateChange(
          gatt = MockBluetoothGatt.Builder().build(),
          status = BluetoothGatt.GATT_SUCCESS,
          newState = BluetoothGatt.STATE_DISCONNECTED,
        )
      val mockEventBus =
        MockMutableSharedFlow<NativeBleEvent>(
          events =
            listOf(mockBleEvent),
          subscriptionCount = mockk<StateFlow<Int>>(),
        )
      val mockGattCallbackHandler =
        MockBluetoothGattCallback.Builder()
          .setCoroutineScope(this)
          .setCallbackResponse(
            OnConnectionStateChange(
              gatt = MockBluetoothGatt.Builder().build(),
              status = BluetoothGatt.GATT_SUCCESS,
              newState = BluetoothGatt.STATE_CONNECTED,
            ),
          )
          .setEventBus(mockEventBus)
          .build()

      val commandQueue = CommandQueue()

      // Act
      launch {
        Disconnect(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          bleQueue = commandQueue,
          coroutineScope = this,
          gattCallbackEventBus = mockGattCallbackHandler.nativeEventBus(),
        ) { event ->
          // Assert
          Assert.assertTrue(event === mockBleEvent) // Same instance
        }
      }.join()
    }

  // integration with GattCallbackHandler

  @Test
  fun `Command dequeues itself on onConnectionStateChange callback`() =
    runTest {
      // Arrange
      val commandQueue = CommandQueue()
      launch {
        val gattCallbackHandler = GattCallbackHandler(this)
        Disconnect(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          bleQueue = commandQueue,
          coroutineScope = this,
          gattCallbackEventBus = gattCallbackHandler.nativeEventBus(),
        ) { }

        gattCallbackHandler.onConnectionStateChange(
          gatt = MockBluetoothGatt.Builder().build(),
          status = BluetoothGatt.GATT_SUCCESS,
          newState = BluetoothGatt.STATE_DISCONNECTED,
        )
      }.join()

      // Assert
      Assert.assertTrue(commandQueue.peek() == null)
    }

  @Test
  fun `Command only recognizes onConnectionStateChange callbacks`() =
    runTest {
      launch {
        // Arrange
        val gattCallbackHandler = GattCallbackHandler(this)
        Disconnect(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          bleQueue = CommandQueue(),
          coroutineScope = this,
          gattCallbackEventBus = gattCallbackHandler.nativeEventBus(),
        ) { event ->
          // Assert
          Assert.assertTrue(event is OnConnectionStateChange)
        }

        gattCallbackHandler.onServiceChanged(
          gatt = MockBluetoothGatt.Builder().build(),
        )

        gattCallbackHandler.onConnectionStateChange(
          gatt = MockBluetoothGatt.Builder().build(),
          status = BluetoothGatt.GATT_SUCCESS,
          newState = BluetoothGatt.STATE_DISCONNECTED,
        )
      }.join()
    }

  @Test
  fun `Command only processes first onConnectionStateChange callback`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt1: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockBleGatt2: BluetoothGatt = MockBluetoothGatt.Builder().build()

      // Act
      launch {
        val gattCallbackHandler = GattCallbackHandler(this)
        Disconnect(
          gatt = mockk<BluetoothGatt>(relaxed = true),
          bleQueue = CommandQueue(),
          coroutineScope = this,
          gattCallbackEventBus = gattCallbackHandler.nativeEventBus(),
        ) { event ->
          // Assert
          Assert.assertTrue((event as OnConnectionStateChange).gatt === mockBleGatt1) // Same instance
          Assert.assertTrue((event as OnConnectionStateChange).status == BluetoothGatt.GATT_SUCCESS)
          Assert.assertTrue((event as OnConnectionStateChange).newState == BluetoothGatt.STATE_CONNECTED)
        }

        gattCallbackHandler.onServiceChanged(
          gatt = MockBluetoothGatt.Builder().build(),
        )

        gattCallbackHandler.onConnectionStateChange(
          gatt = mockBleGatt1,
          status = BluetoothGatt.GATT_SUCCESS,
          newState = BluetoothGatt.STATE_CONNECTED,
        )

        gattCallbackHandler.onConnectionStateChange(
          gatt = mockBleGatt2,
          status = BluetoothGatt.STATE_DISCONNECTING,
          newState = BluetoothGatt.STATE_DISCONNECTED,
        )
      }.join()
    }
}
