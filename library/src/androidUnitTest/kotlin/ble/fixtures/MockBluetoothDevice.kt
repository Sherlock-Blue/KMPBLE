package com.sherlockblue.kmpble.ble.fixtures

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.content.Context
import io.mockk.every
import io.mockk.mockk

class MockBluetoothDevice {
  class Builder() {
    private var mockName = TEST_DEVICE_NAME
    private var mockAddress = TEST_DEVICE_ADDRESS
    private var mockGatt: BluetoothGatt? = null
    private var mockCallbackHandler: BluetoothGattCallback = mockk()

    fun setName(newName: String): Builder {
      this.mockName = newName
      return this
    }

    fun setAddress(newAddress: String): Builder {
      this.mockAddress = newAddress
      return this
    }

    fun setGatt(newGatt: BluetoothGatt): Builder {
      this.mockGatt = newGatt
      return this
    }

    fun setCallbackHandler(newCallbackHandler: BluetoothGattCallback): Builder {
      this.mockCallbackHandler = newCallbackHandler
      return this
    }

    fun build(): BluetoothDevice =
      mockk<BluetoothDevice>().apply {
        val mockCallbackHandler: BluetoothGattCallback = mockCallbackHandler
        val mockGatt: BluetoothGatt? = mockGatt
        every { name } returns mockName
        every { address } returns mockAddress
        every { connectGatt(any<Context>(), any<Boolean>(), any<BluetoothGattCallback>()) } answers {
          val gattCallback = thirdArg<BluetoothGattCallback>()
          (
            mockGatt ?: MockBluetoothGatt.Builder()
              .setCallbackHandler(gattCallback).build()
          ).also { gatt ->
            mockCallbackHandler.onConnectionStateChange(gatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_CONNECTED)
          }
        }
      }
  }
}
