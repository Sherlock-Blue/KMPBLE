package com.sherlockblue.kmpble.ble.commandQueue

import com.sherlockblue.kmpble.ble.commandQueue.commands.BleCommand

interface BleQueue {
  fun enqueue(bleOperation: BleCommand)

  fun completeBleOperation()

  fun peek(): BleCommand?

  fun clear()
}
