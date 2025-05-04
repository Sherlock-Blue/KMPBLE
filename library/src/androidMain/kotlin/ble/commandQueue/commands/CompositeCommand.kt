package com.sherlockblue.kmpble.ble.commandQueue.commands

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.os.Build
import com.sherlockblue.kmpble.ble.NativeBleEvent
import com.sherlockblue.kmpble.ble.callbacks.CallbackError
import com.sherlockblue.kmpble.ble.commandQueue.BleQueue
import com.sherlockblue.kmpble.ble.commandQueue.CommandQueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharedFlow

@SuppressLint("MissingPermission", "NewApi")
class CompositeCommand(
  private val gatt: BluetoothGatt,
  private val characteristic: BluetoothGattCharacteristic,
  private val subscribe: Boolean = false,
  private val bleQueue: BleQueue,
  private val coroutineScope: CoroutineScope,
  private val gattCallbackEventBus: SharedFlow<NativeBleEvent>,
  private val osVersion: Int = Build.VERSION.SDK_INT,
  private val commandCallback: CommandCallback,
) : BleCommand(bleQueue = bleQueue) {
  init {
    enqueue()
  }

  // Can't use the application's command queue unless you like waiting ;-)
  private val compositeCommandQueue: CommandQueue = CommandQueue()

  override fun execute() {
    ManageCharacteristicSubscription(
      gatt = gatt,
      characteristic = characteristic,
      subscribe = subscribe,
      bleQueue = compositeCommandQueue,
      coroutineScope = coroutineScope,
      gattCallbackEventBus = gattCallbackEventBus
    ) { characteristicSubscriptionResponse ->

      when (characteristicSubscriptionResponse) {
        is CallbackError -> {
          commandCallback(characteristicSubscriptionResponse)
          cleanup()
        }
        else -> {
          ManageDescriptorSubscriptions(
            gatt = gatt,
            characteristic = characteristic,
            subscribe = subscribe,
            bleQueue = compositeCommandQueue,
            coroutineScope = coroutineScope,
            gattCallbackEventBus = gattCallbackEventBus
          ) { descriptorSubscriptionResponse ->
            commandCallback(descriptorSubscriptionResponse)
            cleanup()
          }
        }
      }
    }
  }
}
