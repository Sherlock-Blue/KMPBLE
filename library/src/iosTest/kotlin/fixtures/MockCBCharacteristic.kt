package com.sherlockblue.kmpble.fixtures

import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBUUID

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
}
