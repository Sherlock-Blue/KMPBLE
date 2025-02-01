package com.sherlockblue.kmpble.ble.fixtures

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import com.sherlockblue.kmpble.ble.NativeBleEvent
import com.sherlockblue.kmpble.ble.callbacks.GattCallbackHandler
import com.sherlockblue.kmpble.ble.callbacks.OnCharacteristicChanged
import com.sherlockblue.kmpble.ble.callbacks.OnCharacteristicRead
import com.sherlockblue.kmpble.ble.callbacks.OnCharacteristicWrite
import com.sherlockblue.kmpble.ble.callbacks.OnConnectionStateChange
import com.sherlockblue.kmpble.ble.callbacks.OnDescriptorRead
import com.sherlockblue.kmpble.ble.callbacks.OnDescriptorWrite
import com.sherlockblue.kmpble.ble.callbacks.OnMtuChanged
import com.sherlockblue.kmpble.ble.callbacks.OnPhyRead
import com.sherlockblue.kmpble.ble.callbacks.OnPhyUpdate
import com.sherlockblue.kmpble.ble.callbacks.OnReadRemoteRssi
import com.sherlockblue.kmpble.ble.callbacks.OnReliableWriteCompleted
import com.sherlockblue.kmpble.ble.callbacks.OnServiceChanged
import com.sherlockblue.kmpble.ble.callbacks.OnServicesDiscovered
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class MockBluetoothGattCallback {
  class Builder() {
    private var coroutineScope: CoroutineScope? = null
    private val callbackEvents = mutableMapOf<String, NativeBleEvent>()
    private var mockEventBus: MutableSharedFlow<NativeBleEvent>? = null

    fun setCallbackResponse(newEvent: NativeBleEvent): Builder {
      this.callbackEvents[newEvent.javaClass.simpleName] = newEvent
      return this
    }

    fun setCoroutineScope(newScope: CoroutineScope): Builder {
      this.coroutineScope = newScope
      return this
    }

    fun setEventBus(newEventBus: MutableSharedFlow<NativeBleEvent>): Builder {
      this.mockEventBus = newEventBus
      return this
    }

    fun build(): GattCallbackHandler =
      mockk<GattCallbackHandler>().apply {
        val _mockEventBus = mockEventBus ?: MutableSharedFlow<NativeBleEvent>()

        every { nativeEventBus() } answers {
          _mockEventBus as SharedFlow<NativeBleEvent>
        }

        fun publishMockEvent(bleEvent: NativeBleEvent) {
          coroutineScope!!.launch {
            _mockEventBus.emit(bleEvent)
          }
        }

        every { onConnectionStateChange(any<BluetoothGatt>(), any<Int>(), any<Int>()) } answers {
          publishMockEvent(
            callbackEvents[OnConnectionStateChange::class.simpleName] ?: OnConnectionStateChange(
              gatt = firstArg<BluetoothGatt>(),
              status = secondArg<Int>(),
              newState = thirdArg<Int>(),
            ),
          )
        }

        every { onServicesDiscovered(any<BluetoothGatt>(), any<Int>()) } answers {
          publishMockEvent(
            callbackEvents[OnServicesDiscovered::class.simpleName] ?: OnServicesDiscovered(
              gatt = firstArg<BluetoothGatt>(),
              status = secondArg<Int>(),
            ),
          )
        }

        every { onMtuChanged(any<BluetoothGatt>(), any<Int>(), any<Int>()) } answers {
          publishMockEvent(
            callbackEvents[OnMtuChanged::class.simpleName] ?: OnMtuChanged(
              gatt = firstArg<BluetoothGatt>(),
              mtu = secondArg<Int>(),
              status = thirdArg<Int>(),
            ),
          )
        }

        every { onCharacteristicWrite(any<BluetoothGatt>(), any<BluetoothGattCharacteristic>(), any<Int>()) } answers {
          publishMockEvent(
            callbackEvents[OnCharacteristicWrite::class.simpleName] ?: OnCharacteristicWrite(
              gatt = firstArg<BluetoothGatt>(),
              characteristic = secondArg<BluetoothGattCharacteristic>(),
              status = thirdArg<Int>(),
            ),
          )
        }

        every { onCharacteristicRead(any<BluetoothGatt>(), any<BluetoothGattCharacteristic>(), any<Int>()) } answers {
          publishMockEvent(
            callbackEvents[OnCharacteristicRead::class.simpleName] ?: OnCharacteristicRead(
              gatt = firstArg<BluetoothGatt>(),
              characteristic = secondArg<BluetoothGattCharacteristic>(),
              value = secondArg<BluetoothGattCharacteristic>().value,
              status = thirdArg<Int>(),
            ),
          )
        }

        every { onCharacteristicRead(any<BluetoothGatt>(), any<BluetoothGattCharacteristic>(), any<ByteArray>(), any<Int>()) } answers {
          publishMockEvent(
            callbackEvents[OnCharacteristicRead::class.simpleName] ?: OnCharacteristicRead(
              gatt = firstArg<BluetoothGatt>(),
              characteristic = secondArg<BluetoothGattCharacteristic>(),
              value = thirdArg<ByteArray>(),
              status = BluetoothGatt.GATT_SUCCESS,
            ),
          )
        }

        every { onCharacteristicChanged(any<BluetoothGatt>(), any<BluetoothGattCharacteristic>(), any<ByteArray>()) } answers {
          publishMockEvent(
            callbackEvents[OnCharacteristicChanged::class.simpleName] ?: OnCharacteristicChanged(
              gatt = firstArg<BluetoothGatt>(),
              characteristic = secondArg<BluetoothGattCharacteristic>(),
              value = thirdArg<ByteArray>(),
            ),
          )
        }

        every { onCharacteristicChanged(any<BluetoothGatt>(), any<BluetoothGattCharacteristic>()) } answers {
          publishMockEvent(
            callbackEvents[OnCharacteristicChanged::class.simpleName] ?: OnCharacteristicChanged(
              gatt = firstArg<BluetoothGatt>(),
              characteristic = secondArg<BluetoothGattCharacteristic>(),
              value = secondArg<BluetoothGattCharacteristic>().value,
            ),
          )
        }

        every { onCharacteristicRead(any<BluetoothGatt>(), any<BluetoothGattCharacteristic>(), any<ByteArray>(), any<Int>()) } answers {
          publishMockEvent(
            callbackEvents[OnCharacteristicRead::class.simpleName] ?: OnCharacteristicRead(
              gatt = firstArg<BluetoothGatt>(),
              characteristic = secondArg<BluetoothGattCharacteristic>(),
              value = thirdArg<ByteArray>(),
              status = BluetoothGatt.GATT_SUCCESS,
            ),
          )
        }

        every { onDescriptorRead(any<BluetoothGatt>(), any<BluetoothGattDescriptor>(), any<Int>()) } answers {
          publishMockEvent(
            callbackEvents[OnDescriptorRead::class.simpleName] ?: OnDescriptorRead(
              gatt = firstArg<BluetoothGatt>(),
              descriptor = secondArg<BluetoothGattDescriptor>(),
              value = secondArg<BluetoothGattDescriptor>().value,
              status = thirdArg<Int>(),
            ),
          )
        }

        every { onDescriptorRead(any<BluetoothGatt>(), any<BluetoothGattDescriptor>(), any<Int>(), any<ByteArray>()) } answers {
          publishMockEvent(
            callbackEvents[OnDescriptorRead::class.simpleName] ?: OnDescriptorRead(
              gatt = firstArg<BluetoothGatt>(),
              descriptor = secondArg<BluetoothGattDescriptor>(),
              status = thirdArg<Int>(),
              value = byteArrayOf(),
            ),
          )
        }

        every { onDescriptorWrite(any<BluetoothGatt>(), any<BluetoothGattDescriptor>(), any<Int>()) } answers {
          publishMockEvent(
            callbackEvents[OnDescriptorWrite::class.simpleName] ?: OnDescriptorWrite(
              gatt = firstArg<BluetoothGatt>(),
              descriptor = secondArg<BluetoothGattDescriptor>(),
              status = thirdArg<Int>(),
            ),
          )
        }

        every { onReadRemoteRssi(any<BluetoothGatt>(), any<Int>(), any<Int>()) } answers {
          publishMockEvent(
            callbackEvents[OnReadRemoteRssi::class.simpleName] ?: OnReadRemoteRssi(
              gatt = firstArg<BluetoothGatt>(),
              rssi = secondArg<Int>(),
              status = thirdArg<Int>(),
            ),
          )
        }

        every { onReliableWriteCompleted(any<BluetoothGatt>(), any<Int>()) } answers {
          publishMockEvent(
            callbackEvents[OnReliableWriteCompleted::class.simpleName] ?: OnReliableWriteCompleted(
              gatt = firstArg<BluetoothGatt>(),
              status = secondArg<Int>(),
            ),
          )
        }

        every { onPhyRead(any<BluetoothGatt>(), any<Int>(), any<Int>(), any<Int>()) } answers {
          publishMockEvent(
            callbackEvents[OnPhyRead::class.simpleName] ?: OnPhyRead(
              gatt = firstArg<BluetoothGatt>(),
              txPhy = secondArg<Int>(),
              rxPhy = thirdArg<Int>(),
              status = thirdArg<Int>(),
            ),
          )
        }

        every { onPhyUpdate(any<BluetoothGatt>(), any<Int>(), any<Int>(), any<Int>()) } answers {
          publishMockEvent(
            callbackEvents[OnPhyUpdate::class.simpleName] ?: OnPhyUpdate(
              gatt = firstArg<BluetoothGatt>(),
              txPhy = secondArg<Int>(),
              rxPhy = thirdArg<Int>(),
              status = thirdArg<Int>(),
            ),
          )
        }

        every { onServiceChanged(any<BluetoothGatt>()) } answers {
          publishMockEvent(
            callbackEvents[OnServiceChanged::class.simpleName] ?: OnServiceChanged(
              gatt = firstArg<BluetoothGatt>(),
            ),
          )
        }
      }
  }
}
