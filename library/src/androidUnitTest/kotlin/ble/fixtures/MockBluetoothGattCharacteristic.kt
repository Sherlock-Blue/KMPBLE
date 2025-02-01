package com.sherlockblue.kmpble.ble.fixtures

import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import io.mockk.every
import io.mockk.mockk
import java.util.UUID

class MockBluetoothGattCharacteristic {
  class Builder() {
    private var mockUUID: UUID = UUID.fromString(TEST_UUID)
    private var mockService: BluetoothGattService = MockBluetoothGattService.Builder().build()
    private var mockDescriptors: List<BluetoothGattDescriptor> = listOf()
    private var mockValue: ByteArray = byteArrayOf()

    fun setUUID(newUUID: String): Builder {
      this.mockUUID = UUID.fromString(newUUID)
      return this
    }

    fun setService(newService: BluetoothGattService): Builder {
      this.mockService = newService
      return this
    }

    fun setDescriptors(descriptors: List<BluetoothGattDescriptor>): Builder {
      this.mockDescriptors = descriptors
      return this
    }

    fun setValue(newValue: ByteArray): Builder {
      this.mockValue = newValue
      return this
    }

    fun build(): BluetoothGattCharacteristic =
      mockk<BluetoothGattCharacteristic>().apply {
        every { uuid } returns mockUUID
        every { service } returns mockService
        every { descriptors } returns mockDescriptors
        every { value } returns mockValue
        every { getDescriptor(any<UUID>()) } answers {
          val uuid = firstArg<UUID>()
          mockDescriptors.firstOrNull { it.uuid.equals(uuid) }
        }
      }
  }
}
