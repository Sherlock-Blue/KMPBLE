package com.sherlockblue.kmpble.peripheral

import com.sherlockblue.kmpble.ble.BleResponse
import com.sherlockblue.kmpble.ble.NativeBleEvent
import com.sherlockblue.kmpble.ble.Service
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

expect class Peripheral {
  fun nativeEventBus(): Flow<NativeBleEvent>

  fun eventBus(): Flow<BleResponse>

  fun connected(): StateFlow<Boolean>

  fun getServices(): List<Service>

  // Callback Commands
  fun connect(callback: (BleResponse) -> Unit)

  fun disconnect(callback: (BleResponse) -> Unit)

  fun discoverServices(callback: (BleResponse) -> Unit)

  fun readCharacteristic(
    uuid: String,
    callback: (BleResponse) -> Unit,
  )

  fun writeCharacteristic(
    uuid: String,
    data: ByteArray,
    callback: (BleResponse) -> Unit,
  )

  fun readDescriptor(
    characteristicUUID: String,
    descriptorUUID: String,
    callback: (BleResponse) -> Unit,
  )

  fun writeDescriptor(
    characteristicUUID: String,
    descriptorUUID: String,
    data: ByteArray,
    callback: (BleResponse) -> Unit,
  )

  // Coroutine Commands
  suspend fun connect(): BleResponse

  suspend fun disconnect(): BleResponse

  suspend fun discoverServices(): BleResponse

  suspend fun readCharacteristic(uuid: String): BleResponse

  suspend fun writeCharacteristic(
    uuid: String,
    data: ByteArray,
  ): BleResponse

  suspend fun readDescriptor(
    characteristicUUID: String,
    descriptorUUID: String,
  ): BleResponse

  suspend fun writeDescriptor(
    characteristicUUID: String,
    descriptorUUID: String,
    data: ByteArray,
  ): BleResponse
}
