package com.sherlockblue.kmpble.ble.commandQueue.commands

import com.sherlockblue.kmpble.ble.callbacks.BleEvent
import com.sherlockblue.kmpble.ble.commandQueue.BleQueue

typealias CommandCallback = (BleEvent) -> Unit

sealed class BleCommand(private val bleQueue: BleQueue) {
  fun enqueue() {
    bleQueue.enqueue(this)
  }

  abstract fun execute()

  fun cleanup() {
    bleQueue.completeBleOperation()
  }
}
