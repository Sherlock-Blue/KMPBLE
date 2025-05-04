package com.sherlockblue.kmpble.ble.commandQueue.commands

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.os.Build
import com.sherlockblue.kmpble.ble.NativeBleEvent
import com.sherlockblue.kmpble.ble.callbacks.OnDescriptorWrite
import com.sherlockblue.kmpble.ble.commandQueue.BleQueue
import com.sherlockblue.kmpble.ble.extensions.getDescriptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission", "NewApi")
class ManageSubscriptions(
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

  private val CCCD_UUID = "00002902-0000-1000-8000-00805f9b34fb"

  override fun execute() {
    if (gatt.setCharacteristicNotification(characteristic, subscribe)) {
      characteristic.getDescriptor(CCCD_UUID)?.let { descriptor ->
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
        val subscriptionFlag = if (subscribe) {
          BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        } else {
          BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
        }
        if (osVersion < Build.VERSION_CODES.TIRAMISU) {
          gatt.writeDescriptor(descriptor.apply { value = subscriptionFlag })
        } else {
          gatt.writeDescriptor(descriptor, subscriptionFlag)
        }
      }
    }
  }
}
