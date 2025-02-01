package com.sherlockblue.kmpble.fixtures

import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBUUID
import platform.Foundation.NSData
import utils.toNSData

class MockCBCharacteristic(
  private val uuidOverride: CBUUID = MockCBUUID(),
  private val descriptorsOverride: List<CBDescriptor> = listOf(MockCBDescriptor()),
) : CBCharacteristic() {
  override fun UUID(): CBUUID {
    return uuidOverride
  }

  override fun descriptors(): List<*> {
    return descriptorsOverride
  }

  override fun value(): NSData = byteArrayOf(0.toByte(), 1.toByte()).toNSData()
}
