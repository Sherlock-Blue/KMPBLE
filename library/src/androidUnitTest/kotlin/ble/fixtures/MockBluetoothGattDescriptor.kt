package com.sherlockblue.kmpble.ble.fixtures

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import io.mockk.every
import io.mockk.mockk
import java.util.UUID

class MockBluetoothGattDescriptor {
  class Builder() {
    private var mockUUID: UUID = UUID.fromString(DEFAULT_UUID)
    private var mockCharacteristic: BluetoothGattCharacteristic = MockBluetoothGattCharacteristic.Builder().build()
    private var mockValue: ByteArray = byteArrayOf()

    fun setUUID(newUUID: String): Builder {
      this.mockUUID = UUID.fromString(newUUID)
      return this
    }

    fun setCharacteristic(newCharacteristic: BluetoothGattCharacteristic): Builder {
      this.mockCharacteristic = newCharacteristic
      return this
    }

    fun setValue(newValue: ByteArray): Builder {
      this.mockValue = newValue
      return this
    }

    fun build(): BluetoothGattDescriptor =
      mockk<BluetoothGattDescriptor>().apply {
        every { uuid } returns mockUUID
        every { characteristic } returns mockCharacteristic
        every { value } returns mockValue
      }
  }
}
