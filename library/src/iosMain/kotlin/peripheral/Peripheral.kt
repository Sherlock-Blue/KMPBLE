package com.sherlockblue.kmpble.peripheral

import com.sherlockblue.kmpble.ble.BleResponse
import com.sherlockblue.kmpble.callbacks.BleEvent
import com.sherlockblue.kmpble.callbacks.CentralManagerCallbacks
import com.sherlockblue.kmpble.callbacks.PeripheralCallbacks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBService
import utils.toNSData
import kotlin.coroutines.resume

actual class Peripheral(
  private val coroutineScope: CoroutineScope,
  private val centralManager: CBCentralManager,
  private val peripheral: CBPeripheral,
) {
  private val centralManagerDelegate: CentralManagerCallbacks = centralManager.delegate as CentralManagerCallbacks
  private var peripheralDelegate: PeripheralCallbacks = peripheral.delegate as PeripheralCallbacks

  actual fun eventBus(): Flow<BleResponse> = merge(centralManagerDelegate.eventBus(), peripheralDelegate.eventBus())

  actual fun connect(callback: (BleResponse) -> Unit) {
    coroutineScope.launch {
      callback(connect())
    }
  }

  actual suspend fun connect(): BleResponse {
    return suspendCancellableCoroutine { continuation ->
      coroutineScope.launch {
        centralManagerDelegate.eventBus().collect { bleEvent ->
          if (bleEvent is BleEvent.OnPeripheralConnect) {
            continuation.resume(bleEvent)
            cancel()
          }
        }
      }
      centralManager.connectPeripheral(peripheral, null)
    }
  }

  actual fun disconnect(callback: (BleResponse) -> Unit) {
    coroutineScope.launch {
      callback(disconnect())
    }
  }

  actual suspend fun disconnect(): BleResponse {
    return suspendCancellableCoroutine { continuation ->
      coroutineScope.launch {
        centralManagerDelegate.eventBus().collect { bleEvent ->
          if (bleEvent is BleEvent.OnPeripheralDisconnect) {
            continuation.resume(bleEvent)
            cancel()
          }
        }
      }
      centralManager.cancelPeripheralConnection(peripheral)
    }
  }

  actual fun discoverServices(callback: (BleResponse) -> Unit) {
    coroutineScope.launch {
      callback(discoverServices())
    }
  }

  actual suspend fun discoverServices(): BleResponse {
    return suspendCancellableCoroutine { continuation ->
      coroutineScope.launch {
        peripheralDelegate.eventBus().collect { bleEvent ->
          if (bleEvent is BleEvent.OnServicesDiscovered) {
            continuation.resume(bleEvent)
            cancel()
          }
        }
      }
      peripheral.discoverServices(null)
    }
  }

  fun discoverCharacteristicsForService(
    uuid: String,
    callback: (BleResponse) -> Unit,
  ) {
    coroutineScope.launch {
      callback(discoverCharacteristicsForService(uuid))
    }
  }

  suspend fun discoverCharacteristicsForService(uuid: String): BleResponse {
    return suspendCancellableCoroutine { continuation ->
      coroutineScope.launch {
        peripheralDelegate.eventBus().collect { bleEvent ->
          if (bleEvent is BleEvent.OnCharacteristicsDiscovered) {
            continuation.resume(bleEvent)
            cancel()
          }
        }
      }
      getService(uuid)?.let { service ->
        peripheral.discoverCharacteristics(null, service)
      }
    }
  }

  fun discoverDescriptors(
    uuid: String,
    callback: (BleResponse) -> Unit,
  ) {
    coroutineScope.launch {
      callback(discoverDescriptors(uuid))
    }
  }

  suspend fun discoverDescriptors(uuid: String): BleResponse {
    return suspendCancellableCoroutine { continuation ->
      coroutineScope.launch {
        peripheralDelegate.eventBus().collect { bleEvent ->
          if (bleEvent is BleEvent.OnDescriptorsDiscovered) {
            continuation.resume(bleEvent)
            cancel()
          }
        }
      }
      getCharacteristic(uuid)?.let { characteristic ->
        peripheral.discoverDescriptorsForCharacteristic(characteristic)
      }
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
      coroutineScope.launch {
        peripheralDelegate.eventBus().collect { bleEvent ->
          if (bleEvent is BleEvent.OnCharacteristicUpdated) {
            continuation.resume(bleEvent)
            cancel()
          }
        }
      }
      peripheral.readValueForCharacteristic(getCharacteristic(uuid)!!)
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
      coroutineScope.launch {
        peripheralDelegate.eventBus().collect { bleEvent ->
          if (bleEvent is BleEvent.OnCharacteristicWrite) {
            continuation.resume(bleEvent)
            cancel()
          }
        }
      }
      peripheral.writeValue(data.toNSData(), getCharacteristic(uuid)!!, 0L)
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
      coroutineScope.launch {
        peripheralDelegate.eventBus().collect { bleEvent ->
          if (bleEvent is BleEvent.OnDescriptorUpdated) {
            continuation.resume(bleEvent)
            cancel()
          }
        }
      }
      peripheral.readValueForDescriptor(getDescriptor(characteristicUUID, descriptorUUID)!!)
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

  @OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)
  actual suspend fun writeDescriptor(
    characteristicUUID: String,
    descriptorUUID: String,
    data: ByteArray,
  ): BleResponse {
    return suspendCancellableCoroutine { continuation ->
      coroutineScope.launch {
        peripheralDelegate.eventBus().collect { bleEvent ->
          if (bleEvent is BleEvent.OnDescriptorWrite) {
            continuation.resume(bleEvent)
            cancel()
          }
        }
      }
      peripheral.writeValue(data.toNSData(), getDescriptor(characteristicUUID, descriptorUUID)!!)
    }
  }

  private fun getService(uuid: String): CBService? {
    var result: CBService? = null
    peripheral.services?.forEach { service ->
      if ((service as CBService).UUID.UUIDString().contains(uuid, ignoreCase = true)) {
        result = service
      }
    }
    return result
  }

  private fun getCharacteristic(uuid: String): CBCharacteristic? {
    var result: CBCharacteristic? = null
    peripheral.services?.forEach { service ->
      (service as CBService).characteristics?.forEach { characteristic ->
        val char = characteristic as CBCharacteristic
        if (char.UUID.UUIDString().contains(uuid, ignoreCase = true)) {
          result = characteristic
        }
      }
    }
    return result
  }

  private fun getDescriptor(uuid: String): CBDescriptor? {
    var result: CBDescriptor? = null
    peripheral.services?.forEach { service ->
      (service as CBService).characteristics?.forEach { characteristic ->
        val char = characteristic as CBCharacteristic
        char.descriptors?.forEach { descriptor ->
          val desc = characteristic as CBDescriptor
          if (desc.UUID.UUIDString().contains(uuid, ignoreCase = true)) {
            result = desc
          }
        }
      }
    }
    return result
  }

  private fun getDescriptor(
    characteristicUUID: String,
    descriptorUUID: String,
  ): CBDescriptor? {
    var result: CBDescriptor? = null
    getCharacteristic(characteristicUUID)?.descriptors?.forEach { descriptor ->
      val desc = descriptor as CBDescriptor
      if (desc.UUID.UUIDString().contains(descriptorUUID, ignoreCase = true)) {
        result = desc
      }
    }
    return result
  }
}
