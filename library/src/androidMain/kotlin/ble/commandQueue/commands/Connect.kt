package com.sherlockblue.kmpble.ble.commandQueue.commands

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import com.sherlockblue.kmpble.ble.callbacks.BleEvent
import com.sherlockblue.kmpble.ble.callbacks.GattCallbackHandler
import com.sherlockblue.kmpble.ble.commandQueue.BleQueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
class Connect(
  private val peripheral: BluetoothDevice,
  private val context: Context,
  private val gattCallbackHandler: GattCallbackHandler,
  private val bleQueue: BleQueue,
  private val coroutineScope: CoroutineScope,
  private val commandCallback: CommandCallback,
) : BleCommand(bleQueue = bleQueue) {
  init {
    enqueue()
  }

  override fun execute() {
    coroutineScope.launch {
      gattCallbackHandler.eventBus().collect { bleEvent ->
        if (bleEvent is BleEvent.OnConnectionStateChange) {
          commandCallback(bleEvent)
          cleanup()
          cancel()
        }
      }
    }
    peripheral.connectGatt(context, true, gattCallbackHandler)
  }
}
