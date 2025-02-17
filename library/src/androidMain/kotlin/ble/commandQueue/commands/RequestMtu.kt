package com.sherlockblue.kmpble.ble.commandQueue.commands

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import com.sherlockblue.kmpble.ble.NativeBleEvent
import com.sherlockblue.kmpble.ble.callbacks.OnMtuChanged
import com.sherlockblue.kmpble.ble.commandQueue.BleQueue
import com.sherlockblue.kmpble.constants.MAX_MTU_SIZE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
class RequestMtu(
  private val gatt: BluetoothGatt,
// Android OS > Tiramisu always requests MAX_MTU = 512
  private val mtu: Int = MAX_MTU_SIZE,
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
        if (bleEvent is OnMtuChanged) {
          commandCallback(bleEvent)
          cleanup()
          cancel()
        }
      }
    }
    gatt.requestMtu(mtu)
  }
}
