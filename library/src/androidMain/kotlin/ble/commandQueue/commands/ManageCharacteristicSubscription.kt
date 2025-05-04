package com.sherlockblue.kmpble.ble.commandQueue.commands

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import com.sherlockblue.kmpble.ble.NativeBleEvent
import com.sherlockblue.kmpble.ble.callbacks.CallbackError
import com.sherlockblue.kmpble.ble.callbacks.OnCharacteristicWrite
import com.sherlockblue.kmpble.ble.commandQueue.BleQueue
import com.sherlockblue.kmpble.constants.INVALID_CHARACTERISTIC_ERROR
import com.sherlockblue.kmpble.constants.getErrorMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow

@SuppressLint("MissingPermission", "NewApi")
class ManageCharacteristicSubscription(
  private val gatt: BluetoothGatt,
  private val characteristic: BluetoothGattCharacteristic,
  private val subscribe: Boolean = false,
  private val bleQueue: BleQueue,
  private val coroutineScope: CoroutineScope,
  private val gattCallbackEventBus: SharedFlow<NativeBleEvent>,
  private val commandCallback: CommandCallback,
) : BleCommand(bleQueue = bleQueue) {
  init {
    enqueue()
  }

  override fun execute() {
    commandCallback(
      if (gatt.setCharacteristicNotification(characteristic, subscribe)) {
        CallbackError(
          message = getErrorMessage(INVALID_CHARACTERISTIC_ERROR),
          status = INVALID_CHARACTERISTIC_ERROR,
        )
      } else {
        OnCharacteristicWrite(
          gatt = gatt,
          characteristic = characteristic,
          status = BluetoothGatt.GATT_SUCCESS,
        )
      }
    )
    cleanup()
  }
}
