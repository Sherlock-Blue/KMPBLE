package com.sherlockblue.kmpble.ble.fixtures

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import io.mockk.every
import io.mockk.mockk
import java.util.UUID

class MockBluetoothGattService {
  class Builder() {
    private var mockUUID: UUID = UUID.fromString(TEST_UUID)
    private var mockCharacteristics: List<BluetoothGattCharacteristic> = listOf()

    fun setUUID(newUUID: String): Builder {
      this.mockUUID = UUID.fromString(newUUID)
      return this
    }

    fun setCharacteristics(characteristics: List<BluetoothGattCharacteristic>): Builder {
      this.mockCharacteristics = characteristics
      return this
    }

    fun build(): BluetoothGattService =
      mockk<BluetoothGattService>().apply {
        every { uuid } returns mockUUID
        every { characteristics } returns mockCharacteristics
        every { getCharacteristic(any<UUID>()) } answers {
          val uuid = firstArg<UUID>()
          mockCharacteristics.firstOrNull { it.uuid.equals(uuid) }
        }
      }
  }
}
