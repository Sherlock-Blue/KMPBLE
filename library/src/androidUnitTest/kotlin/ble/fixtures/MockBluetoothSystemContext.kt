package com.sherlockblue.kmpble.ble.fixtures

import android.bluetooth.BluetoothManager
import android.content.Context
import io.mockk.every
import io.mockk.mockkClass

class MockBluetoothSystemContext {
  class Builder() {
    private var mockBluetoothManager: BluetoothManager = MockBluetoothManager.Builder().build()

    fun setBluetoothManager(newBluetoothManager: BluetoothManager): Builder {
      this.mockBluetoothManager = newBluetoothManager
      return this
    }

    fun build(): Context =
      mockkClass<Context>(Context::class).apply {
        val mockBluetoothManager: BluetoothManager = mockBluetoothManager
        every { getSystemService(Context.BLUETOOTH_SERVICE) } returns (mockBluetoothManager)
      }
  }
}
