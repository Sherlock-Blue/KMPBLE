package com.sherlockblue.kmpble.fixtures

import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBUUID
import platform.Foundation.NSData
import utils.toNSData

class MockCBDescriptor(private val uuidOverride: CBUUID = MockCBUUID()) : CBDescriptor() {
  override fun UUID(): CBUUID {
    return uuidOverride
  }

  override fun value(): NSData = byteArrayOf(0.toByte(), 1.toByte()).toNSData()
}
