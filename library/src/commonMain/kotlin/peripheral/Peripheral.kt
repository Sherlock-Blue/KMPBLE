package com.sherlockblue.kmpble.peripheral

import com.sherlockblue.kmpble.ble.BleResponse
import kotlinx.coroutines.flow.Flow

expect class Peripheral {
  fun eventBus(): Flow<BleResponse>

  suspend fun connect(): BleResponse

  fun connect(callback: (BleResponse) -> Unit)

  suspend fun disconnect(): BleResponse

  fun disconnect(callback: (BleResponse) -> Unit)

  suspend fun discoverServices(): BleResponse

  fun discoverServices(callback: (BleResponse) -> Unit)

  suspend fun readCharacteristic(uuid: String): BleResponse

  fun readCharacteristic(
    uuid: String,
    callback: (BleResponse) -> Unit,
  )

  suspend fun writeCharacteristic(
    uuid: String,
    data: ByteArray,
  ): BleResponse

  fun writeCharacteristic(
    uuid: String,
    data: ByteArray,
    callback: (BleResponse) -> Unit,
  )

  suspend fun readDescriptor(
    characteristicUUID: String,
    descriptorUUID: String,
  ): BleResponse

  fun readDescriptor(
    characteristicUUID: String,
    descriptorUUID: String,
    callback: (BleResponse) -> Unit,
  )

  suspend fun writeDescriptor(
    characteristicUUID: String,
    descriptorUUID: String,
    data: ByteArray,
  ): BleResponse

  fun writeDescriptor(
    characteristicUUID: String,
    descriptorUUID: String,
    data: ByteArray,
    callback: (BleResponse) -> Unit,
  )
}
