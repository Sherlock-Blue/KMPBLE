package com.sherlockblue.kmpble.peripheral

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.content.Context
import com.sherlockblue.kmpble.INVALID_CHARACTERISTIC_ERROR
import com.sherlockblue.kmpble.INVALID_DESCRIPTOR_ERROR
import com.sherlockblue.kmpble.NULL_GATT_ERROR
import com.sherlockblue.kmpble.ble.BleResponse
import com.sherlockblue.kmpble.ble.callbacks.BleEvent
import com.sherlockblue.kmpble.ble.callbacks.GattCallbackHandler
import com.sherlockblue.kmpble.ble.commandQueue.CommandQueue
import com.sherlockblue.kmpble.ble.commandQueue.commands.Connect
import com.sherlockblue.kmpble.ble.commandQueue.commands.Disconnect
import com.sherlockblue.kmpble.ble.commandQueue.commands.DiscoverServices
import com.sherlockblue.kmpble.ble.commandQueue.commands.ReadCharacteristic
import com.sherlockblue.kmpble.ble.commandQueue.commands.ReadDescriptor
import com.sherlockblue.kmpble.ble.commandQueue.commands.WriteCharacteristic
import com.sherlockblue.kmpble.ble.commandQueue.commands.WriteDescriptor
import com.sherlockblue.kmpble.ble.extensions.getCharacteristic
import com.sherlockblue.kmpble.ble.extensions.getDescriptor
import com.sherlockblue.kmpble.constants.getErrorMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

actual class Peripheral(
  public val device: BluetoothDevice,
  private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO),
  private val context: Context,
  private val gattCallbackHandler: GattCallbackHandler = GattCallbackHandler(coroutineScope = coroutineScope),
  private val commandQueue: CommandQueue = CommandQueue(),
) {
  actual fun eventBus(): Flow<BleResponse> = gattCallbackHandler.eventBus()

  internal var gatt: BluetoothGatt? = null

  actual fun connect(callback: (BleResponse) -> Unit) {
    coroutineScope.launch {
      callback(connect())
    }
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  actual suspend fun connect(): BleResponse {
    return suspendCancellableCoroutine { continuation ->
      Connect(
        peripheral = device,
        context = context,
        gattCallbackHandler = gattCallbackHandler,
        bleQueue = commandQueue,
        coroutineScope = coroutineScope,
      ) {
        gatt = (it as BleEvent.OnConnectionStateChange).gatt
        continuation.resume(it)
      }
    }
  }

  actual fun disconnect(callback: (BleResponse) -> Unit) {
    coroutineScope.launch {
      callback(disconnect())
    }
  }

  actual suspend fun disconnect(): BleResponse {
    return suspendCancellableCoroutine { continuation ->
      gatt?.let { gatt ->
        Disconnect(
          gatt = gatt,
          bleQueue = commandQueue,
          coroutineScope = coroutineScope,
          gattCallbackEventBus = gattCallbackHandler.eventBus(),
        ) {
          continuation.resume(it)
        }
      } ?: continuation.resume(
        BleEvent.CallbackError(message = getErrorMessage(NULL_GATT_ERROR), status = NULL_GATT_ERROR),
      )
    }
  }

  actual fun discoverServices(callback: (BleResponse) -> Unit) {
    coroutineScope.launch {
      callback(discoverServices())
    }
  }

  actual suspend fun discoverServices(): BleResponse {
    return suspendCancellableCoroutine { continuation ->
      gatt?.let { gatt ->
        DiscoverServices(
          gatt = gatt,
          bleQueue = commandQueue,
          coroutineScope = coroutineScope,
          gattCallbackEventBus = gattCallbackHandler.eventBus(),
        ) {
          continuation.resume(it)
        }
      } ?: continuation.resume(
        BleEvent.CallbackError(message = getErrorMessage(NULL_GATT_ERROR), status = NULL_GATT_ERROR),
      )
    }
  }

  actual fun readCharacteristic(
    uuid: String,
    callback: (BleResponse) -> Unit,
  ) {
    coroutineScope.launch {
      callback(readCharacteristic(uuid))
    }
  }

  actual suspend fun readCharacteristic(uuid: String): BleResponse {
    return suspendCancellableCoroutine { continuation ->
      gatt?.let { gatt ->
        gatt.getCharacteristic(uuid)?.let { characteristic ->
          ReadCharacteristic(
            gatt = gatt,
            characteristic = characteristic,
            bleQueue = commandQueue,
            coroutineScope = coroutineScope,
            gattCallbackEventBus = gattCallbackHandler.eventBus(),
          ) {
            continuation.resume(it)
          }
        } ?: continuation.resume(
          BleEvent.CallbackError(message = getErrorMessage(INVALID_CHARACTERISTIC_ERROR), status = INVALID_CHARACTERISTIC_ERROR),
        )
      } ?: continuation.resume(
        BleEvent.CallbackError(message = getErrorMessage(NULL_GATT_ERROR), status = NULL_GATT_ERROR),
      )
    }
  }

  actual fun writeCharacteristic(
    uuid: String,
    data: ByteArray,
    callback: (BleResponse) -> Unit,
  ) {
    coroutineScope.launch {
      callback(writeCharacteristic(uuid, data))
    }
  }

  actual suspend fun writeCharacteristic(
    uuid: String,
    data: ByteArray,
  ): BleResponse {
    return suspendCancellableCoroutine { continuation ->
      gatt?.let { gatt ->
        gatt.getCharacteristic(uuid)?.let { characteristic ->
          WriteCharacteristic(
            gatt = gatt,
            characteristic = characteristic,
            data = data,
            bleQueue = commandQueue,
            coroutineScope = coroutineScope,
            gattCallbackEventBus = gattCallbackHandler.eventBus(),
          ) {
            continuation.resume(it)
          }
        } ?: continuation.resume(
          BleEvent.CallbackError(message = getErrorMessage(INVALID_CHARACTERISTIC_ERROR), status = INVALID_CHARACTERISTIC_ERROR),
        )
      } ?: continuation.resume(
        BleEvent.CallbackError(message = getErrorMessage(NULL_GATT_ERROR), status = NULL_GATT_ERROR),
      )
    }
  }

  actual fun readDescriptor(
    characteristicUUID: String,
    descriptorUUID: String,
    callback: (BleResponse) -> Unit,
  ) {
    coroutineScope.launch {
      callback(readDescriptor(characteristicUUID, descriptorUUID))
    }
  }

  actual suspend fun readDescriptor(
    characteristicUUID: String,
    descriptorUUID: String,
  ): BleResponse {
    return suspendCancellableCoroutine { continuation ->
      gatt?.let { gatt ->
        gatt.getCharacteristic(characteristicUUID)?.let { characteristic ->
          characteristic.getDescriptor(descriptorUUID)?.let { descriptor ->
            ReadDescriptor(
              gatt = gatt,
              descriptor = descriptor,
              bleQueue = commandQueue,
              coroutineScope = coroutineScope,
              gattCallbackEventBus = gattCallbackHandler.eventBus(),
            ) {
              continuation.resume(it)
            }
          } ?: continuation.resume(
            BleEvent.CallbackError(message = getErrorMessage(INVALID_DESCRIPTOR_ERROR), status = INVALID_DESCRIPTOR_ERROR),
          )
        } ?: continuation.resume(
          BleEvent.CallbackError(message = getErrorMessage(INVALID_CHARACTERISTIC_ERROR), status = INVALID_CHARACTERISTIC_ERROR),
        )
      } ?: continuation.resume(
        BleEvent.CallbackError(message = getErrorMessage(NULL_GATT_ERROR), status = NULL_GATT_ERROR),
      )
    }
  }

  actual fun writeDescriptor(
    characteristicUUID: String,
    descriptorUUID: String,
    data: ByteArray,
    callback: (BleResponse) -> Unit,
  ) {
    coroutineScope.launch {
      callback(writeDescriptor(characteristicUUID, descriptorUUID, data))
    }
  }

  actual suspend fun writeDescriptor(
    characteristicUUID: String,
    descriptorUUID: String,
    data: ByteArray,
  ): BleResponse {
    return suspendCancellableCoroutine { continuation ->
      gatt?.let { gatt ->
        gatt.getCharacteristic(characteristicUUID)?.let { characteristic ->
          characteristic.getDescriptor(descriptorUUID)?.let { descriptor ->
            WriteDescriptor(
              gatt = gatt,
              descriptor = descriptor,
              data = data,
              bleQueue = commandQueue,
              coroutineScope = coroutineScope,
              gattCallbackEventBus = gattCallbackHandler.eventBus(),
            ) {
              continuation.resume(it)
            }
          } ?: continuation.resume(
            BleEvent.CallbackError(
              message = getErrorMessage(INVALID_DESCRIPTOR_ERROR),
              status = INVALID_DESCRIPTOR_ERROR,
            ),
          )
        } ?: continuation.resume(
          BleEvent.CallbackError(
            message = getErrorMessage(INVALID_CHARACTERISTIC_ERROR),
            status = INVALID_CHARACTERISTIC_ERROR,
          ),
        )
      } ?: continuation.resume(
        BleEvent.CallbackError(
          message = getErrorMessage(NULL_GATT_ERROR),
          status = NULL_GATT_ERROR,
        ),
      )
    }
  }
}
