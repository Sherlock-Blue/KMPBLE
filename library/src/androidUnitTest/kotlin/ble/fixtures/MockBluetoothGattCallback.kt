package com.sherlockblue.kmpble.ble.fixtures

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import com.sherlockblue.kmpble.ble.callbacks.BleEvent
import com.sherlockblue.kmpble.ble.callbacks.BleEvent.OnConnectionStateChange
import com.sherlockblue.kmpble.ble.callbacks.GattCallbackHandler
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class MockBluetoothGattCallback {
  class Builder() {
    private var coroutineScope: CoroutineScope? = null
    private val callbackEvents = mutableMapOf<String, BleEvent>()
    private var mockEventBus: MutableSharedFlow<BleEvent>? = null

    fun setCallbackResponse(newEvent: BleEvent): Builder {
      this.callbackEvents[newEvent.javaClass.simpleName] = newEvent
      return this
    }

    fun setCoroutineScope(newScope: CoroutineScope): Builder {
      this.coroutineScope = newScope
      return this
    }

    fun setEventBus(newEventBus: MutableSharedFlow<BleEvent>): Builder {
      this.mockEventBus = newEventBus
      return this
    }

    fun build(): GattCallbackHandler =
      mockk<GattCallbackHandler>().apply {
        val _mockEventBus = mockEventBus ?: MutableSharedFlow<BleEvent>()

        every { eventBus() } answers {
          _mockEventBus as SharedFlow<BleEvent>
        }

        fun publishMockEvent(bleEvent: BleEvent) {
          coroutineScope!!.launch {
            _mockEventBus.emit(bleEvent)
          }
        }

        every { onConnectionStateChange(any<BluetoothGatt>(), any<Int>(), any<Int>()) } answers {
          publishMockEvent(
            callbackEvents[BleEvent.OnConnectionStateChange::class.simpleName] ?: OnConnectionStateChange(
              gatt = firstArg<BluetoothGatt>(),
              status = secondArg<Int>(),
              newState = thirdArg<Int>(),
            ),
          )
        }

        every { onServicesDiscovered(any<BluetoothGatt>(), any<Int>()) } answers {
          publishMockEvent(
            callbackEvents[BleEvent.OnServicesDiscovered::class.simpleName] ?: BleEvent.OnServicesDiscovered(
              gatt = firstArg<BluetoothGatt>(),
              status = secondArg<Int>(),
            ),
          )
        }

        every { onMtuChanged(any<BluetoothGatt>(), any<Int>(), any<Int>()) } answers {
          publishMockEvent(
            callbackEvents[BleEvent.OnMtuChanged::class.simpleName] ?: BleEvent.OnMtuChanged(
              gatt = firstArg<BluetoothGatt>(),
              mtu = secondArg<Int>(),
              status = thirdArg<Int>(),
            ),
          )
        }

        every { onCharacteristicWrite(any<BluetoothGatt>(), any<BluetoothGattCharacteristic>(), any<Int>()) } answers {
          publishMockEvent(
            callbackEvents[BleEvent.OnCharacteristicWrite::class.simpleName] ?: BleEvent.OnCharacteristicWrite(
              gatt = firstArg<BluetoothGatt>(),
              characteristic = secondArg<BluetoothGattCharacteristic>(),
              status = thirdArg<Int>(),
            ),
          )
        }

        every { onCharacteristicRead(any<BluetoothGatt>(), any<BluetoothGattCharacteristic>(), any<Int>()) } answers {
          publishMockEvent(
            callbackEvents[BleEvent.OnCharacteristicRead::class.simpleName] ?: BleEvent.OnCharacteristicRead(
              gatt = firstArg<BluetoothGatt>(),
              characteristic = secondArg<BluetoothGattCharacteristic>(),
              value = secondArg<BluetoothGattCharacteristic>().value,
              status = thirdArg<Int>(),
            ),
          )
        }

        every { onCharacteristicRead(any<BluetoothGatt>(), any<BluetoothGattCharacteristic>(), any<ByteArray>(), any<Int>()) } answers {
          publishMockEvent(
            callbackEvents[BleEvent.OnCharacteristicRead::class.simpleName] ?: BleEvent.OnCharacteristicRead(
              gatt = firstArg<BluetoothGatt>(),
              characteristic = secondArg<BluetoothGattCharacteristic>(),
              value = thirdArg<ByteArray>(),
              status = BluetoothGatt.GATT_SUCCESS,
            ),
          )
        }

        every { onCharacteristicChanged(any<BluetoothGatt>(), any<BluetoothGattCharacteristic>(), any<ByteArray>()) } answers {
          publishMockEvent(
            callbackEvents[BleEvent.OnCharacteristicChanged::class.simpleName] ?: BleEvent.OnCharacteristicChanged(
              gatt = firstArg<BluetoothGatt>(),
              characteristic = secondArg<BluetoothGattCharacteristic>(),
              value = thirdArg<ByteArray>(),
            ),
          )
        }

        every { onCharacteristicChanged(any<BluetoothGatt>(), any<BluetoothGattCharacteristic>()) } answers {
          publishMockEvent(
            callbackEvents[BleEvent.OnCharacteristicChanged::class.simpleName] ?: BleEvent.OnCharacteristicChanged(
              gatt = firstArg<BluetoothGatt>(),
              characteristic = secondArg<BluetoothGattCharacteristic>(),
              value = secondArg<BluetoothGattCharacteristic>().value,
            ),
          )
        }

        every { onCharacteristicRead(any<BluetoothGatt>(), any<BluetoothGattCharacteristic>(), any<ByteArray>(), any<Int>()) } answers {
          publishMockEvent(
            callbackEvents[BleEvent.OnCharacteristicRead::class.simpleName] ?: BleEvent.OnCharacteristicRead(
              gatt = firstArg<BluetoothGatt>(),
              characteristic = secondArg<BluetoothGattCharacteristic>(),
              value = thirdArg<ByteArray>(),
              status = BluetoothGatt.GATT_SUCCESS,
            ),
          )
        }

        every { onDescriptorRead(any<BluetoothGatt>(), any<BluetoothGattDescriptor>(), any<Int>()) } answers {
          publishMockEvent(
            callbackEvents[BleEvent.OnDescriptorRead::class.simpleName] ?: BleEvent.OnDescriptorRead(
              gatt = firstArg<BluetoothGatt>(),
              descriptor = secondArg<BluetoothGattDescriptor>(),
              value = secondArg<BluetoothGattDescriptor>().value,
              status = thirdArg<Int>(),
            ),
          )
        }

        every { onDescriptorRead(any<BluetoothGatt>(), any<BluetoothGattDescriptor>(), any<Int>(), any<ByteArray>()) } answers {
          publishMockEvent(
            callbackEvents[BleEvent.OnDescriptorRead::class.simpleName] ?: BleEvent.OnDescriptorRead(
              gatt = firstArg<BluetoothGatt>(),
              descriptor = secondArg<BluetoothGattDescriptor>(),
              status = thirdArg<Int>(),
              value = byteArrayOf(),
            ),
          )
        }

        every { onDescriptorWrite(any<BluetoothGatt>(), any<BluetoothGattDescriptor>(), any<Int>()) } answers {
          publishMockEvent(
            callbackEvents[BleEvent.OnDescriptorWrite::class.simpleName] ?: BleEvent.OnDescriptorWrite(
              gatt = firstArg<BluetoothGatt>(),
              descriptor = secondArg<BluetoothGattDescriptor>(),
              status = thirdArg<Int>(),
            ),
          )
        }

        every { onReadRemoteRssi(any<BluetoothGatt>(), any<Int>(), any<Int>()) } answers {
          publishMockEvent(
            callbackEvents[BleEvent.OnReadRemoteRssi::class.simpleName] ?: BleEvent.OnReadRemoteRssi(
              gatt = firstArg<BluetoothGatt>(),
              rssi = secondArg<Int>(),
              status = thirdArg<Int>(),
            ),
          )
        }

        every { onReliableWriteCompleted(any<BluetoothGatt>(), any<Int>()) } answers {
          publishMockEvent(
            callbackEvents[BleEvent.OnReliableWriteCompleted::class.simpleName] ?: BleEvent.OnReliableWriteCompleted(
              gatt = firstArg<BluetoothGatt>(),
              status = secondArg<Int>(),
            ),
          )
        }

        every { onPhyRead(any<BluetoothGatt>(), any<Int>(), any<Int>(), any<Int>()) } answers {
          publishMockEvent(
            callbackEvents[BleEvent.OnPhyRead::class.simpleName] ?: BleEvent.OnPhyRead(
              gatt = firstArg<BluetoothGatt>(),
              txPhy = secondArg<Int>(),
              rxPhy = thirdArg<Int>(),
              status = thirdArg<Int>(),
            ),
          )
        }

        every { onPhyUpdate(any<BluetoothGatt>(), any<Int>(), any<Int>(), any<Int>()) } answers {
          publishMockEvent(
            callbackEvents[BleEvent.OnPhyUpdate::class.simpleName] ?: BleEvent.OnPhyUpdate(
              gatt = firstArg<BluetoothGatt>(),
              txPhy = secondArg<Int>(),
              rxPhy = thirdArg<Int>(),
              status = thirdArg<Int>(),
            ),
          )
        }

        every { onServiceChanged(any<BluetoothGatt>()) } answers {
          publishMockEvent(
            callbackEvents[BleEvent.OnServiceChanged::class.simpleName] ?: BleEvent.OnServiceChanged(
              gatt = firstArg<BluetoothGatt>(),
            ),
          )
        }
      }
  }
}
