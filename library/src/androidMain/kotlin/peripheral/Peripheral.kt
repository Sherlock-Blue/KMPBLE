package com.sherlockblue.kmpble.peripheral

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattDescriptor
import android.content.Context
import com.sherlockblue.kmpble.ble.BleResponse
import com.sherlockblue.kmpble.ble.Service
import com.sherlockblue.kmpble.ble.callbacks.GattCallbackHandler
import com.sherlockblue.kmpble.ble.callbacks.OnConnectionStateChange
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
import com.sherlockblue.kmpble.ble.extensions.getServices
import com.sherlockblue.kmpble.constants.INVALID_CHARACTERISTIC_ERROR
import com.sherlockblue.kmpble.constants.INVALID_DESCRIPTOR_ERROR
import com.sherlockblue.kmpble.constants.NULL_GATT_ERROR
import com.sherlockblue.kmpble.constants.getErrorMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

actual class Peripheral(
  val device: BluetoothDevice,
  private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO),
  private val context: Context,
  private val gattCallbackHandler: GattCallbackHandler = GattCallbackHandler(coroutineScope = coroutineScope),
  private val commandQueue: CommandQueue = CommandQueue(),
) {
  actual fun nativeEventBus(): Flow<com.sherlockblue.kmpble.ble.NativeBleEvent> = gattCallbackHandler.nativeEventBus()

  actual fun eventBus(): Flow<BleResponse> = gattCallbackHandler.eventBus()

  private val _connected = MutableStateFlow<Boolean>(false)

  actual fun connected() = _connected as StateFlow<Boolean>

  private fun setConnectionStatus(status: Int) {
    _connected.value = status == BluetoothGatt.STATE_CONNECTED
  }

  actual fun getServices(): List<Service> {
    // Obeying Law of Demeter
    val nativeServices = gatt?.services
    val xPlatformServices = nativeServices?.getServices()
    return xPlatformServices ?: listOf<Service>()
  }

  internal var gatt: BluetoothGatt? = null

  actual fun connect(callback: (BleResponse) -> Unit) {
    coroutineScope.launch {
      callback(connect())
    }
  }

  actual suspend fun connect(): BleResponse {
    return suspendCancellableCoroutine { continuation ->
      Connect(
        peripheral = device,
        context = context,
        gattCallbackHandler = gattCallbackHandler,
        bleQueue = commandQueue,
        coroutineScope = coroutineScope,
      ) {
        gatt = (it as OnConnectionStateChange).gatt
        setConnectionStatus((it as OnConnectionStateChange).newState)
        continuation.resume(it.toBleResponse())
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
          gattCallbackEventBus = gattCallbackHandler.nativeEventBus(),
        ) {
          setConnectionStatus((it as OnConnectionStateChange).newState)
          continuation.resume(it.toBleResponse())
        }
      } ?: continuation.resume(BleResponse.Error(message = getErrorMessage(NULL_GATT_ERROR), status = NULL_GATT_ERROR))
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
          gattCallbackEventBus = gattCallbackHandler.nativeEventBus(),
        ) {
          continuation.resume(it.toBleResponse())
        }
      } ?: continuation.resume(
        BleResponse.Error(message = getErrorMessage(NULL_GATT_ERROR), status = NULL_GATT_ERROR),
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
            gattCallbackEventBus = gattCallbackHandler.nativeEventBus(),
          ) {
            continuation.resume(it.toBleResponse())
          }
        } ?: continuation.resume(
          BleResponse.Error(message = getErrorMessage(INVALID_CHARACTERISTIC_ERROR), status = INVALID_CHARACTERISTIC_ERROR),
        )
      } ?: continuation.resume(
        BleResponse.Error(message = getErrorMessage(NULL_GATT_ERROR), status = NULL_GATT_ERROR),
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
            gattCallbackEventBus = gattCallbackHandler.nativeEventBus(),
          ) {
            continuation.resume(it.toBleResponse())
          }
        } ?: continuation.resume(
          BleResponse.Error(message = getErrorMessage(INVALID_CHARACTERISTIC_ERROR), status = INVALID_CHARACTERISTIC_ERROR),
        )
      } ?: continuation.resume(
        BleResponse.Error(message = getErrorMessage(NULL_GATT_ERROR), status = NULL_GATT_ERROR),
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
              gattCallbackEventBus = gattCallbackHandler.nativeEventBus(),
            ) {
              continuation.resume(it.toBleResponse())
            }
          } ?: continuation.resume(
            BleResponse.Error(message = getErrorMessage(INVALID_DESCRIPTOR_ERROR), status = INVALID_DESCRIPTOR_ERROR),
          )
        } ?: continuation.resume(
          BleResponse.Error(message = getErrorMessage(INVALID_CHARACTERISTIC_ERROR), status = INVALID_CHARACTERISTIC_ERROR),
        )
      } ?: continuation.resume(
        BleResponse.Error(message = getErrorMessage(NULL_GATT_ERROR), status = NULL_GATT_ERROR),
      )
    }
  }

  // Native function for development not yet exposed in common
  @SuppressLint("MissingPermission")
  fun manageSubscription(
    characteristicUUID: String,
    subscribe: Boolean,
    callback: (BleResponse) -> Unit
  ) {
    gatt?.let { gatt ->
      gatt.getCharacteristic(characteristicUUID)?.let { characteristic ->
        if (gatt.setCharacteristicNotification(characteristic, subscribe)) {
          writeDescriptor(
            characteristicUUID, "00002902-0000-1000-8000-00805f9b34fb",
            if (subscribe) {
              BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            } else {
              BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
            }, callback
          )
        }
      }
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
              gattCallbackEventBus = gattCallbackHandler.nativeEventBus(),
            ) {
              continuation.resume(it.toBleResponse())
            }
          } ?: continuation.resume(
            BleResponse.Error(
              message = getErrorMessage(INVALID_DESCRIPTOR_ERROR),
              status = INVALID_DESCRIPTOR_ERROR,
            ),
          )
        } ?: continuation.resume(
          BleResponse.Error(
            message = getErrorMessage(INVALID_CHARACTERISTIC_ERROR),
            status = INVALID_CHARACTERISTIC_ERROR,
          ),
        )
      } ?: continuation.resume(
        BleResponse.Error(
          message = getErrorMessage(NULL_GATT_ERROR),
          status = NULL_GATT_ERROR,
        ),
      )
    }
  }
}
