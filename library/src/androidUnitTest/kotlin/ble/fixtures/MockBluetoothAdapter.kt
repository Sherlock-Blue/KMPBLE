package com.sherlockblue.kmpble.ble.fixtures

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import io.mockk.every
import io.mockk.mockkClass

class MockBluetoothAdapter() {
  class Builder {
    private var mockBluetoothLeScanner: BluetoothLeScanner = MockBluetoothLeScanner.Builder().build()

    fun setBluetoothLeScanner(newBluetoothLeScanner: BluetoothLeScanner): Builder {
      this.mockBluetoothLeScanner = newBluetoothLeScanner
      return this
    }

    fun build(): BluetoothAdapter =
      mockkClass<BluetoothAdapter>(BluetoothAdapter::class).apply {
        val mockBluetoothLeScanner: BluetoothLeScanner = mockBluetoothLeScanner
        every { bluetoothLeScanner } returns (mockBluetoothLeScanner)
      }
  }
}
