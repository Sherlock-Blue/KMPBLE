package com.sherlockblue.kmpble.ble.commandQueue.commands

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.os.Build
import com.sherlockblue.kmpble.ble.NativeBleEvent
import com.sherlockblue.kmpble.ble.callbacks.OnCharacteristicRead
import com.sherlockblue.kmpble.ble.commandQueue.BleQueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
class ReadCharacteristic(
  private val gatt: BluetoothGatt,
  private val characteristic: BluetoothGattCharacteristic,
  private val bleQueue: BleQueue,
  private val coroutineScope: CoroutineScope,
  private val gattCallbackEventBus: SharedFlow<NativeBleEvent>,
  private val osVersion: Int = Build.VERSION.SDK_INT,
  private val commandCallback: CommandCallback,
) : BleCommand(bleQueue = bleQueue) {
  init {
    enqueue()
  }

  override fun execute() {
    coroutineScope.launch {
      gattCallbackEventBus.collect { bleEvent ->
        if ((bleEvent is OnCharacteristicRead) && (
            bleEvent.characteristic.uuid.equals(
              characteristic.uuid,
            )
          )
        ) {
          commandCallback(bleEvent)
          cleanup()
          cancel()
        }
      }
    }
    gatt.readCharacteristic(characteristic)
  }
}
