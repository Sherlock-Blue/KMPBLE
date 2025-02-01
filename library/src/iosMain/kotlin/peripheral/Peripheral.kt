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

  actual fun discoverServices(callback: (BleResponse) -> Unit) {
    coroutineScope.launch {
      callback(discoverServices())
    }
  }

  actual suspend fun discoverServices(): BleResponse {
    return suspendCancellableCoroutine { continuation ->
      coroutineScope.launch {
        peripheralDelegate.nativeEventBus().collect { bleEvent ->
          if (bleEvent is OnServicesDiscovered) {
            continuation.resume(bleEvent.toBleResponse())
            cancel()
          }
        }
      }
      peripheral.discoverServices(null)
    }
  }

  private suspend fun getCharacteristicsForAllServices(services: List<CBService>): BleResponse {
    services.forEach { service ->
      getCharacteristicsForService((service as CBService)).also { bleResponse ->
        if (bleResponse is BleResponse.Error) {
          return bleResponse
        }
      }
    }
    return BleResponse.ServicesDiscovered(status = 0)
  }

  fun discoverCharacteristicsForService(
    uuid: String,
    callback: (BleResponse) -> Unit,
  ) {
    coroutineScope.launch {
      callback(discoverCharacteristicsForService(uuid).toBleResponse())
    }
  }

  private suspend fun getCharacteristicsForService(service: CBService): BleResponse {
    service.characteristics?.forEach { characteristic ->
      discoverCharacteristicsForService((characteristic as CBCharacteristic).UUID.UUIDString).also { nativeBleEvent ->
        if (nativeBleEvent is CallbackError) {
          return nativeBleEvent.toBleResponse()
        }
      }
    }
    return BleResponse.ServicesDiscovered(status = 0)
  }

  suspend fun discoverCharacteristicsForService(uuid: String): NativeBleEvent {
    return suspendCancellableCoroutine { continuation ->
      coroutineScope.launch {
        peripheralDelegate.nativeEventBus().collect { bleEvent ->
          if (bleEvent is OnCharacteristicsDiscovered) {
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
      callback(discoverDescriptors(uuid).toBleResponse())
    }
  }

  private suspend fun getDescriptorsForCharacteristic(characteristics: CBCharacteristic): BleResponse {
    characteristics.descriptors?.forEach { descriptor ->
      discoverDescriptors((descriptor as CBDescriptor).UUID.UUIDString).also { nativeBleEvent ->
        if (nativeBleEvent is CallbackError) {
          return nativeBleEvent.toBleResponse()
        }
      }
    }
    return BleResponse.ServicesDiscovered(status = 0)
  }

  suspend fun discoverDescriptors(uuid: String): NativeBleEvent {
    return suspendCancellableCoroutine { continuation ->
      coroutineScope.launch {
        peripheralDelegate.nativeEventBus().collect { bleEvent ->
          if (bleEvent is OnDescriptorsDiscovered) {
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
        peripheralDelegate.nativeEventBus().collect { bleEvent ->
          if (bleEvent is OnCharacteristicRead) {
            continuation.resume(bleEvent.toBleResponse())
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
        peripheralDelegate.nativeEventBus().collect { bleEvent ->
          if (bleEvent is OnCharacteristicWrite) {
            continuation.resume(bleEvent.toBleResponse())
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
        peripheralDelegate.nativeEventBus().collect { bleEvent ->
          if (bleEvent is OnDescriptorRead) {
            continuation.resume(bleEvent.toBleResponse())
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
        peripheralDelegate.nativeEventBus().collect { bleEvent ->
          if (bleEvent is OnDescriptorWrite) {
            continuation.resume(bleEvent.toBleResponse())
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
