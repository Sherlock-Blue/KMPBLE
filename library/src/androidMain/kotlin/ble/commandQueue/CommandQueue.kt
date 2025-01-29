package com.sherlockblue.kmpble.ble.commandQueue

import com.sherlockblue.kmpble.ble.commandQueue.commands.BleCommand
import java.util.concurrent.ConcurrentLinkedQueue

class CommandQueue(
  private val bleCommandQueue: ConcurrentLinkedQueue<BleCommand> =
    ConcurrentLinkedQueue<BleCommand>(),
) : BleQueue {
  override fun enqueue(bleOperation: BleCommand) {
    bleCommandQueue.add(bleOperation)
    if (bleCommandQueue.size == 1) {
      doNext()
    }
  }

  private fun doNext() {
    peek()?.execute()
  }

  override fun completeBleOperation() {
    if (bleCommandQueue.isNotEmpty()) {
      bleCommandQueue.remove()
      doNext()
    }
  }

  override fun peek(): BleCommand? {
    return bleCommandQueue.peek()
  }

  override fun clear() {
    peek()?.cleanup()
    bleCommandQueue.clear()
  }
}
