package com.sherlockblue.kmpble.ble.callbacks

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import com.sherlockblue.kmpble.NULL_CHARACTERISTIC_ERROR
import com.sherlockblue.kmpble.NULL_DESCRIPTOR_ERROR
import com.sherlockblue.kmpble.NULL_GATT_ERROR
import com.sherlockblue.kmpble.ble.callbacks.BleEvent.CallbackError
import com.sherlockblue.kmpble.ble.callbacks.BleEvent.OnCharacteristicChanged
import com.sherlockblue.kmpble.ble.callbacks.BleEvent.OnCharacteristicRead
import com.sherlockblue.kmpble.ble.callbacks.BleEvent.OnCharacteristicWrite
import com.sherlockblue.kmpble.ble.callbacks.BleEvent.OnConnectionStateChange
import com.sherlockblue.kmpble.ble.callbacks.BleEvent.OnDescriptorRead
import com.sherlockblue.kmpble.ble.callbacks.BleEvent.OnDescriptorWrite
import com.sherlockblue.kmpble.ble.callbacks.BleEvent.OnMtuChanged
import com.sherlockblue.kmpble.ble.callbacks.BleEvent.OnPhyRead
import com.sherlockblue.kmpble.ble.callbacks.BleEvent.OnPhyUpdate
import com.sherlockblue.kmpble.ble.callbacks.BleEvent.OnReadRemoteRssi
import com.sherlockblue.kmpble.ble.callbacks.BleEvent.OnReliableWriteCompleted
import com.sherlockblue.kmpble.ble.callbacks.BleEvent.OnServiceChanged
import com.sherlockblue.kmpble.ble.callbacks.BleEvent.OnServicesDiscovered
import com.sherlockblue.kmpble.constants.getErrorMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

class GattCallbackHandler(private val coroutineScope: CoroutineScope) : BluetoothGattCallback() {
  internal var _eventBus = MutableSharedFlow<BleEvent>()

  fun eventBus() = _eventBus as SharedFlow<BleEvent>

  private fun publishEvent(event: BleEvent) {
    coroutineScope.launch {
      _eventBus.emit(event)
    }
  }

  override fun onServiceChanged(gatt: BluetoothGatt) {
    publishEvent(OnServiceChanged(gatt = gatt))
  }

  override fun onMtuChanged(
    gatt: BluetoothGatt?,
    mtu: Int,
    status: Int,
  ) {
    publishEvent(OnMtuChanged(gatt = gatt, mtu = mtu, status = status))
  }

  override fun onReadRemoteRssi(
    gatt: BluetoothGatt?,
    rssi: Int,
    status: Int,
  ) {
    publishEvent(OnReadRemoteRssi(gatt = gatt, rssi = rssi, status = status))
  }

  override fun onReliableWriteCompleted(
    gatt: BluetoothGatt?,
    status: Int,
  ) {
    publishEvent(OnReliableWriteCompleted(gatt = gatt, status = status))
  }

  override fun onDescriptorWrite(
    gatt: BluetoothGatt?,
    descriptor: BluetoothGattDescriptor?,
    status: Int,
  ) {
    if (gatt != null) {
      if (descriptor != null) {
        publishEvent(OnDescriptorWrite(gatt = gatt, descriptor = descriptor, status = status))
      } else {
        publishEvent(CallbackError(message = getErrorMessage(NULL_DESCRIPTOR_ERROR), status = status))
      }
    } else {
      publishEvent(CallbackError(message = getErrorMessage(NULL_GATT_ERROR), status = status))
    }
  }

  @Deprecated("Deprecated in Java")
  override fun onDescriptorRead(
    gatt: BluetoothGatt?,
    descriptor: BluetoothGattDescriptor?,
    status: Int,
  ) {
    if (gatt != null) {
      if (descriptor != null) {
        onDescriptorRead(
          gatt = gatt,
          descriptor = descriptor,
          status = status,
          value = descriptor.value.copyOf(),
        )
      } else {
        publishEvent(CallbackError(message = getErrorMessage(NULL_DESCRIPTOR_ERROR), status = status))
      }
    } else {
      publishEvent(CallbackError(message = getErrorMessage(NULL_GATT_ERROR), status = status))
    }
  }

  override fun onDescriptorRead(
    gatt: BluetoothGatt,
    descriptor: BluetoothGattDescriptor,
    status: Int,
    value: ByteArray,
  ) {
    publishEvent(
      OnDescriptorRead(
        gatt = gatt,
        descriptor = descriptor,
        status = status,
        value = value.copyOf(),
      ),
    )
  }

  @Deprecated("Deprecated in Java")
  override fun onCharacteristicChanged(
    gatt: BluetoothGatt?,
    characteristic: BluetoothGattCharacteristic?,
  ) {
    if (gatt != null) {
      if (characteristic != null) {
        onCharacteristicChanged(
          gatt = gatt,
          characteristic = characteristic,
          value = characteristic.value.copyOf(),
        )
      } else {
        publishEvent(CallbackError(message = getErrorMessage(NULL_CHARACTERISTIC_ERROR), status = NULL_CHARACTERISTIC_ERROR))
      }
    } else {
      publishEvent(CallbackError(message = getErrorMessage(NULL_GATT_ERROR), status = NULL_GATT_ERROR))
    }
  }

  override fun onCharacteristicChanged(
    gatt: BluetoothGatt,
    characteristic: BluetoothGattCharacteristic,
    value: ByteArray,
  ) {
    publishEvent(
      OnCharacteristicChanged(
        gatt = gatt,
        characteristic = characteristic,
        value = value.copyOf(),
      ),
    )
  }

  override fun onCharacteristicWrite(
    gatt: BluetoothGatt?,
    characteristic: BluetoothGattCharacteristic?,
    status: Int,
  ) {
    if (gatt != null) {
      if (characteristic != null) {
        publishEvent(
          OnCharacteristicWrite(
            gatt = gatt,
            characteristic = characteristic,
            status = status,
          ),
        )
      } else {
        publishEvent(CallbackError(message = getErrorMessage(NULL_CHARACTERISTIC_ERROR), status = status))
      }
    } else {
      publishEvent(CallbackError(message = getErrorMessage(NULL_GATT_ERROR), status = status))
    }
  }

  @Deprecated("Deprecated in Java")
  override fun onCharacteristicRead(
    gatt: BluetoothGatt?,
    characteristic: BluetoothGattCharacteristic?,
    status: Int,
  ) {
    if (gatt != null) {
      if (characteristic != null) {
        onCharacteristicRead(
          gatt = gatt,
          characteristic = characteristic,
          value = characteristic.value.copyOf(),
          status = status,
        )
      } else {
        publishEvent(CallbackError(message = getErrorMessage(NULL_CHARACTERISTIC_ERROR), status = status))
      }
    } else {
      publishEvent(CallbackError(message = getErrorMessage(NULL_GATT_ERROR), status = status))
    }
  }

  override fun onCharacteristicRead(
    gatt: BluetoothGatt,
    characteristic: BluetoothGattCharacteristic,
    value: ByteArray,
    status: Int,
  ) {
    publishEvent(
      OnCharacteristicRead(
        gatt = gatt,
        characteristic = characteristic,
        status = status,
        value = value.copyOf(),
      ),
    )
  }

  override fun onServicesDiscovered(
    gatt: BluetoothGatt?,
    status: Int,
  ) {
    publishEvent(
      OnServicesDiscovered(
        gatt = gatt,
        status = status,
      ),
    )
  }

  override fun onConnectionStateChange(
    gatt: BluetoothGatt?,
    status: Int,
    newState: Int,
  ) {
    publishEvent(
      OnConnectionStateChange(
        gatt = gatt,
        status = status,
        newState = newState,
      ),
    )
  }

  override fun onPhyRead(
    gatt: BluetoothGatt?,
    txPhy: Int,
    rxPhy: Int,
    status: Int,
  ) {
    publishEvent(
      OnPhyRead(
        gatt = gatt,
        txPhy = txPhy,
        rxPhy = rxPhy,
        status = status,
      ),
    )
  }

  override fun onPhyUpdate(
    gatt: BluetoothGatt?,
    txPhy: Int,
    rxPhy: Int,
    status: Int,
  ) {
    publishEvent(
      OnPhyUpdate(
        gatt = gatt,
        txPhy = txPhy,
        rxPhy = rxPhy,
        status = status,
      ),
    )
  }
}
