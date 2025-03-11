package com.sherlockblue.kmpble.peripheral

import com.sherlockblue.kmpble.ble.BleResponse
import com.sherlockblue.kmpble.ble.NativeBleEvent
import com.sherlockblue.kmpble.ble.Service
import com.sherlockblue.kmpble.callbacks.CallbackError
import com.sherlockblue.kmpble.callbacks.CentralManagerCallbacks
import com.sherlockblue.kmpble.callbacks.OnCharacteristicRead
import com.sherlockblue.kmpble.callbacks.OnCharacteristicWrite
import com.sherlockblue.kmpble.callbacks.OnCharacteristicsDiscovered
import com.sherlockblue.kmpble.callbacks.OnDescriptorRead
import com.sherlockblue.kmpble.callbacks.OnDescriptorWrite
import com.sherlockblue.kmpble.callbacks.OnDescriptorsDiscovered
import com.sherlockblue.kmpble.callbacks.OnPeripheralConnect
import com.sherlockblue.kmpble.callbacks.OnPeripheralDisconnect
import com.sherlockblue.kmpble.callbacks.OnServicesDiscovered
import com.sherlockblue.kmpble.callbacks.PeripheralCallbacks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBService
import utils.getServices
import utils.toNSData
import kotlin.coroutines.resume

actual class Peripheral(
  private val coroutineScope: CoroutineScope,
  private val centralManager: CBCentralManager,
  private val peripheral: CBPeripheral,
) {
  private val centralManagerDelegate: CentralManagerCallbacks = centralManager.delegate as CentralManagerCallbacks
  private var peripheralDelegate: PeripheralCallbacks = peripheral.delegate as PeripheralCallbacks

  actual fun nativeEventBus(): Flow<com.sherlockblue.kmpble.ble.NativeBleEvent> =
    merge(
      centralManagerDelegate.nativeEventBus(),
      peripheralDelegate.nativeEventBus(),
    )

  actual fun eventBus(): Flow<BleResponse> = merge(centralManagerDelegate.eventBus(), peripheralDelegate.eventBus())

  private val _connected = MutableStateFlow<Boolean>(false)

  actual fun connected() = _connected as StateFlow<Boolean>

  private var _services: List<Service> = listOf()

  actual fun getServices(): List<Service> {
    // Obeying Law of Demeter
    val nativeServices = peripheral.services
    val xPlatformServices = nativeServices?.getServices()
    return xPlatformServices ?: listOf<Service>()
  }

  private val monitorConnectionStatus =
    coroutineScope.launch {
      centralManagerDelegate.nativeEventBus().collect { event ->
        when (event) {
          is OnPeripheralConnect -> _connected.value = true
          is OnPeripheralDisconnect -> _connected.value = false
        }
      }
    }

  actual fun connect(callback: (BleResponse) -> Unit) {
    coroutineScope.launch {
      callback(connect())
    }
  }

  actual suspend fun connect(): BleResponse {
    return suspendCancellableCoroutine { continuation ->
      coroutineScope.launch {
        centralManagerDelegate.nativeEventBus().collect {
          if (it is OnPeripheralConnect) {
            monitorConnectionStatus.start()
            continuation.resume(it.toBleResponse())
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
        centralManagerDelegate.nativeEventBus().collect { bleEvent ->
          if (bleEvent is OnPeripheralDisconnect) {
            monitorConnectionStatus.cancel()
            continuation.resume(bleEvent.toBleResponse())
            cancel()
          }
        }
      }
      centralManager.cancelPeripheralConnection(peripheral)
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
        peripheralDelegate.nativeEventBus().collect { bleEvent ->
          if (bleEvent is OnCharacteristicRead) {
            continuation.resume(bleEvent.toBleResponse())
            cancel()
          }
        }
      }
      getCBCharacteristic(uuid)?.let { characteristic ->
        peripheral.readValueForCharacteristic(characteristic)
      }
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
        peripheralDelegate.nativeEventBus().collect { bleEvent ->
          if (bleEvent is OnCharacteristicWrite) {
            continuation.resume(bleEvent.toBleResponse())
            cancel()
          }
        }
      }
      getCBCharacteristic(uuid)?.let { characteristic ->
        peripheral.writeValue(
          data = data.toNSData(),
          forCharacteristic = characteristic,
          type = 0L,
        ) // CBCharacteristicWriteType.withResponse = 0
      }
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
        peripheralDelegate.nativeEventBus().collect { bleEvent ->
          if (bleEvent is OnDescriptorRead) {
            continuation.resume(bleEvent.toBleResponse())
            cancel()
          }
        }
      }
      peripheral.readValueForDescriptor(getCBDescriptor(characteristicUUID, descriptorUUID)!!)
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
        peripheralDelegate.nativeEventBus().collect { bleEvent ->
          if (bleEvent is OnDescriptorWrite) {
            continuation.resume(bleEvent.toBleResponse())
            cancel()
          }
        }
      }
      peripheral.writeValue(data.toNSData(), getCBDescriptor(characteristicUUID, descriptorUUID)!!)
    }
  }

  private fun getCBService(uuid: String): CBService? {
    var result: CBService? = null
    peripheral.services?.forEach { service ->
      if ((service as CBService).UUID.UUIDString().contains(uuid, ignoreCase = true)) {
        result = service
      }
    }
    return result
  }

  private fun getCBCharacteristic(uuid: String): CBCharacteristic? {
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

  private fun getCBDescriptor(uuid: String): CBDescriptor? {
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

  private fun getCBDescriptor(
    characteristicUUID: String,
    descriptorUUID: String,
  ): CBDescriptor? {
    var result: CBDescriptor? = null
    getCBCharacteristic(characteristicUUID)?.descriptors?.forEach { descriptor ->
      val desc = descriptor as CBDescriptor
      if (desc.UUID.UUIDString().contains(descriptorUUID, ignoreCase = true)) {
        result = desc
      }
    }
    return result
  }

  // Service Discovery
  actual fun discoverServices(callback: (BleResponse) -> Unit) {
    coroutineScope.launch {
      callback(discoverServices())
    }
  }

  actual suspend fun discoverServices(): BleResponse {
    return discoverAllAttributes()
  }

  private suspend fun discoverAllAttributes(): BleResponse {
    val discoverServicesResult = discoverCBServices()
    if (discoverServicesResult !is CallbackError) {
      peripheral.services?.forEach { service ->
        discoverCBCharacteristics(service as CBService).also {
          if (it is CallbackError) {
            return (it.toBleResponse())
          }
          service.characteristics?.forEach { characteristic ->
            discoverCBDescriptors(characteristic as CBCharacteristic).also {
              if (it is CallbackError) {
                return (it.toBleResponse())
              }
            }
          }
        }
      }
    }
    return (discoverServicesResult.toBleResponse())
  }

  suspend fun discoverCBServices(): NativeBleEvent {
    return suspendCancellableCoroutine { continuation ->
      coroutineScope.launch {
        peripheralDelegate.nativeEventBus().collect { bleEvent ->
          if ((bleEvent is OnServicesDiscovered) || (bleEvent is CallbackError)) {
            continuation.resume(bleEvent)
            cancel()
          }
        }
      }
      peripheral.discoverServices(null)
    }
  }

  suspend fun discoverCBCharacteristics(service: CBService): NativeBleEvent {
    return suspendCancellableCoroutine { continuation ->
      coroutineScope.launch {
        peripheralDelegate.nativeEventBus().collect { bleEvent ->
          if ((bleEvent is OnCharacteristicsDiscovered) || (bleEvent is CallbackError)) {
            continuation.resume(bleEvent)
          }
          cancel()
        }
      }
      peripheral.discoverCharacteristics(null, service)
    }
  }

  suspend fun discoverCBDescriptors(characteristic: CBCharacteristic): NativeBleEvent {
    return suspendCancellableCoroutine { continuation ->
      coroutineScope.launch {
        peripheralDelegate.nativeEventBus().collect { bleEvent ->
          if ((bleEvent is OnDescriptorsDiscovered) || (bleEvent is CallbackError)) {
            continuation.resume(bleEvent)
          }
          cancel()
        }
      }
      peripheral.discoverDescriptorsForCharacteristic(characteristic)
    }
  }
}
