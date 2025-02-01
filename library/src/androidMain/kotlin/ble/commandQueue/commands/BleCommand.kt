package com.sherlockblue.kmpble.ble.commandQueue.commands

import com.sherlockblue.kmpble.ble.NativeBleEvent
import com.sherlockblue.kmpble.ble.commandQueue.BleQueue

typealias CommandCallback = (NativeBleEvent) -> Unit

sealed class BleCommand(private val bleQueue: BleQueue) {
  fun enqueue() {
    bleQueue.enqueue(this)
  }

  abstract fun execute()

  fun cleanup() {
    bleQueue.completeBleOperation()
  }
}
