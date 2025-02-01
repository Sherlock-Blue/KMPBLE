package com.sherlockblue.kmpble.ble.commandQueue.commands

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import com.sherlockblue.kmpble.ble.NativeBleEvent
import com.sherlockblue.kmpble.ble.callbacks.OnServicesDiscovered
import com.sherlockblue.kmpble.ble.commandQueue.BleQueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
class DiscoverServices(
  private val gatt: BluetoothGatt,
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
        if (bleEvent is OnServicesDiscovered) {
          commandCallback(bleEvent)
          cleanup()
          cancel()
        }
      }
    }
    gatt.discoverServices()
  }
}
