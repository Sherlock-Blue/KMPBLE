package com.sherlockblue.kmpble.ble.fixtures

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattService
import io.mockk.every
import io.mockk.mockk
import java.util.UUID

class MockBluetoothGatt {
  class Builder() {
    private var mockDevice: BluetoothDevice = MockBluetoothDevice.Builder().build()
    private var mockCallbackHandler: BluetoothGattCallback = mockk(relaxed = true)
    private var mockServices: List<BluetoothGattService> = listOf()

    fun setDevice(newDevice: BluetoothDevice): Builder {
      this.mockDevice = newDevice
      return this
    }

    fun setCallbackHandler(newCallbackHandler: BluetoothGattCallback): Builder {
      this.mockCallbackHandler = newCallbackHandler
      return this
    }

    fun setServices(characteristics: List<BluetoothGattService>): Builder {
      this.mockServices = characteristics
      return this
    }

    fun build(): BluetoothGatt =
      mockk<BluetoothGatt>().apply {
        val thisMockGatt = this
        val mockCallbackHandler: BluetoothGattCallback = mockCallbackHandler
        every { device } returns mockDevice
        every { services } returns mockServices

        every { getService(any<UUID>()) } answers {
          val uuid = firstArg<UUID>()
          mockServices.firstOrNull { it.uuid.equals(uuid) }
        }

        every { disconnect() } answers {
          mockCallbackHandler.onConnectionStateChange(thisMockGatt, BluetoothGatt.GATT_SUCCESS, BluetoothGatt.STATE_DISCONNECTED)
        }

        every { discoverServices() } answers {
          mockCallbackHandler.onServicesDiscovered(thisMockGatt, BluetoothGatt.GATT_SUCCESS)
          true
        }

        every { readCharacteristic(any<BluetoothGattCharacteristic>()) } answers {
          mockCallbackHandler.onCharacteristicRead(
            thisMockGatt,
            firstArg<BluetoothGattCharacteristic>(),
            firstArg<BluetoothGattCharacteristic>().value,
            BluetoothGatt.GATT_SUCCESS,
          )
          true
        }

        every { writeCharacteristic(any<BluetoothGattCharacteristic>()) } answers {
          mockCallbackHandler.onCharacteristicWrite(
            thisMockGatt,
            firstArg<BluetoothGattCharacteristic>(),
            BluetoothGatt.GATT_SUCCESS,
          )
          true
        }

        every { writeCharacteristic(any<BluetoothGattCharacteristic>(), any<ByteArray>(), any<Int>()) } answers {
          firstArg<BluetoothGattCharacteristic>().value = secondArg<ByteArray>()
          mockCallbackHandler.onCharacteristicWrite(
            thisMockGatt,
            firstArg<BluetoothGattCharacteristic>(),
            BluetoothGatt.GATT_SUCCESS,
          )
          BluetoothGatt.GATT_SUCCESS
        }

        every { readDescriptor(any<BluetoothGattDescriptor>()) } answers {
          mockCallbackHandler.onDescriptorRead(
            thisMockGatt,
            firstArg<BluetoothGattDescriptor>(),
            BluetoothGatt.GATT_SUCCESS,
            firstArg<BluetoothGattDescriptor>().value,
          )
          true
        }

        every { writeDescriptor(any<BluetoothGattDescriptor>()) } answers {
          mockCallbackHandler.onDescriptorWrite(
            thisMockGatt,
            firstArg<BluetoothGattDescriptor>(),
            BluetoothGatt.GATT_SUCCESS,
          )
          true
        }

        every { writeDescriptor(any<BluetoothGattDescriptor>(), any<ByteArray>()) } answers {
          firstArg<BluetoothGattDescriptor>().value = secondArg<ByteArray>()
          mockCallbackHandler.onDescriptorWrite(
            thisMockGatt,
            firstArg<BluetoothGattDescriptor>(),
            BluetoothGatt.GATT_SUCCESS,
          )
          BluetoothGatt.GATT_SUCCESS
        }
      }
  }
}
