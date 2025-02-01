package com.sherlockblue.kmpble.ble.commandQueue.commands

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattDescriptor
import android.os.Build
import com.sherlockblue.kmpble.ble.NativeBleEvent
import com.sherlockblue.kmpble.ble.callbacks.OnDescriptorWrite
import com.sherlockblue.kmpble.ble.commandQueue.BleQueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission", "NewApi")
class WriteDescriptor(
  private val gatt: BluetoothGatt,
  private val descriptor: BluetoothGattDescriptor,
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
        if ((bleEvent is OnDescriptorWrite) && (
            bleEvent.descriptor!!.uuid!! == descriptor.uuid
          )
        ) {
          commandCallback(bleEvent)
          cleanup()
          cancel()
        }
      }
    }
    if (osVersion < Build.VERSION_CODES.TIRAMISU) {
      gatt.writeDescriptor(descriptor)
    } else {
      gatt.writeDescriptor(descriptor, data)
    }
  }
}
