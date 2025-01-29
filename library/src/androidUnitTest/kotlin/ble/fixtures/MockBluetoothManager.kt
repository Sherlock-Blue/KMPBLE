package com.sherlockblue.kmpble.ble.fixtures

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import io.mockk.every
import io.mockk.mockkClass

class MockBluetoothManager {
  class Builder() {
    private var mockBluetoothAdapter: BluetoothAdapter = MockBluetoothAdapter.Builder().build()

    fun setBluetoothAdapter(newBluetoothAdapter: BluetoothAdapter): Builder {
      this.mockBluetoothAdapter = newBluetoothAdapter
      return this
    }

    fun build(): BluetoothManager =
      mockkClass<BluetoothManager>(BluetoothManager::class).apply {
        val mockBluetoothAdapter: BluetoothAdapter = mockBluetoothAdapter
        every { adapter } returns (mockBluetoothAdapter)
      }
  }
}
