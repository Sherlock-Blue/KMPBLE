package com.sherlockblue.kmpble.fixtures

import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBService
import platform.CoreBluetooth.CBUUID

class MockCBService(
  private val uuidOverride: CBUUID = MockCBUUID(),
  private val characteristicsOverride: List<CBCharacteristic> = listOf(MockCBCharacteristic()),
) : CBService() {
  override fun UUID(): CBUUID {
    return uuidOverride
  }

  override fun characteristics(): List<*> {
    return characteristicsOverride
  }
}
