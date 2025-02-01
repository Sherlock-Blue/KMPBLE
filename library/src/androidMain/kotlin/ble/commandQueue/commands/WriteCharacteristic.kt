package com.sherlockblue.kmpble.ble.commandQueue.commands

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.os.Build
import com.sherlockblue.kmpble.ble.NativeBleEvent
import com.sherlockblue.kmpble.ble.callbacks.OnCharacteristicWrite
import com.sherlockblue.kmpble.ble.commandQueue.BleQueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission", "NewApi")
class WriteCharacteristic(
  private val gatt: BluetoothGatt,
  private val characteristic: BluetoothGattCharacteristic,
  private val data: ByteArray,
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
        if ((bleEvent is OnCharacteristicWrite) && (
            bleEvent.characteristic.uuid!! == characteristic.uuid
          )
        ) {
          commandCallback(bleEvent)
          cleanup()
          cancel()
        }
      }
    }
    if (osVersion < Build.VERSION_CODES.TIRAMISU) {
      gatt.writeCharacteristic(characteristic)
    } else {
      gatt.writeCharacteristic(characteristic, data, BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT)
    }
  }
}
