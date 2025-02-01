package com.sherlockblue.kmpble.ble.commandQueue.commands

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattDescriptor
import com.sherlockblue.kmpble.ble.NativeBleEvent
import com.sherlockblue.kmpble.ble.callbacks.OnDescriptorRead
import com.sherlockblue.kmpble.ble.commandQueue.BleQueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
class ReadDescriptor(
  private val gatt: BluetoothGatt,
  private val descriptor: BluetoothGattDescriptor,
  private val bleQueue: BleQueue,
  private val coroutineScope: CoroutineScope,
  private val gattCallbackEventBus: SharedFlow<NativeBleEvent>,
  private val commandCallback: CommandCallback,
) : BleCommand(bleQueue = bleQueue) {
  init {
    enqueue()
  }

  override fun execute() {
    coroutineScope.launch {
      gattCallbackEventBus.collect { bleEvent ->
        if ((bleEvent is OnDescriptorRead) && (
            bleEvent.descriptor.uuid.equals(
              descriptor.uuid,
            )
          )
        ) {
          commandCallback(bleEvent)
          cleanup()
          cancel()
        }
      }
    }
    gatt.readDescriptor(descriptor)
  }
}
