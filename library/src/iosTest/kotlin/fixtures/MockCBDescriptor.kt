package com.sherlockblue.kmpble.fixtures

import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBUUID

class MockCBDescriptor(private val uuidOverride: CBUUID = MockCBUUID()) : CBDescriptor() {
  override fun UUID(): CBUUID {
    return uuidOverride
  }
}
