package com.sherlockblue.kmpble.fixtures

import platform.CoreBluetooth.CBUUID

class MockCBUUID(val toStringOverride: String = DEFAULT_UUID) : CBUUID() {
  override fun UUIDString(): String {
    return toStringOverride
  }
}
