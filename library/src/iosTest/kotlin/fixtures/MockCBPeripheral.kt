package com.sherlockblue.kmpble.fixtures

import com.sherlockblue.kmpble.callbacks.PeripheralCallbacks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBPeripheralDelegateProtocol
import platform.CoreBluetooth.CBPeripheralState
import platform.CoreBluetooth.CBService
import platform.Foundation.NSData
import platform.Foundation.NSNumber
import platform.Foundation.NSUUID

val mockCBPeripheral: MockCBPeripheral = MockCBPeripheral() // There can be only one

class MockCBPeripheral(
  var peripheralDelegateOverride: CBPeripheralDelegateProtocol? = PeripheralCallbacks(CoroutineScope(Dispatchers.IO)),
  private val nameOverride: String = "",
  private val identifierOverride: NSUUID = NSUUID(uUIDString = DEFAULT_UUID),
  private val rssiOverride: Int = 0,
) : CBPeripheral() {
  private val calledFunctions: MutableList<String> = mutableListOf()

  fun verify(
    atLeast: Int = 0,
    atMost: Int = Int.MAX_VALUE,
    exactly: Int = -1,
    functionName: String,
  ) = (calledFunctions.count { it.contains(functionName, ignoreCase = true) } in maxOf(atLeast, exactly)..minOf(atMost, exactly))

  private fun logFunctionCall(functionName: String) {
    calledFunctions.add(functionName)
  }

  override fun name(): String? {
    logFunctionCall("name")
    return nameOverride
  }

  override fun delegate(): CBPeripheralDelegateProtocol? {
    return peripheralDelegateOverride
  }

  override fun RSSI(): NSNumber? {
    logFunctionCall("RSSI")
    return NSNumber(rssiOverride)
  }

  override fun services(): List<*>? {
    logFunctionCall("services")
    return listOf(MockCBService())
  }

  override fun state(): CBPeripheralState {
    return super.state()
  }

  override fun discoverServices(serviceUUIDs: List<*>?) {
    logFunctionCall("discoverServices")
    peripheralDelegateOverride?.peripheral(peripheral = this, didDiscoverServices = null)
  }

  override fun discoverCharacteristics(
    characteristicUUIDs: List<*>?,
    forService: CBService,
  ) {
    logFunctionCall("discoverCharacteristics")
    peripheralDelegateOverride?.peripheral(peripheral = this, didDiscoverCharacteristicsForService = forService, error = null)
  }

  override fun discoverDescriptorsForCharacteristic(characteristic: CBCharacteristic) {
    logFunctionCall("discoverDescriptorsForCharacteristic")
    peripheralDelegateOverride?.peripheral(peripheral = this, didDiscoverDescriptorsForCharacteristic = characteristic, error = null)
  }

  override fun discoverIncludedServices(
    includedServiceUUIDs: List<*>?,
    forService: CBService,
  ) {
    logFunctionCall("discoverIncludedServices")
    peripheralDelegateOverride?.peripheral(peripheral = this, didDiscoverIncludedServicesForService = forService, error = null)
  }

  override fun readRSSI() {
    logFunctionCall("readRSSI")
    peripheralDelegateOverride?.peripheral(peripheral = this, didReadRSSI = NSNumber(rssiOverride), error = null)
  }

  override fun readValueForCharacteristic(characteristic: CBCharacteristic) {
    logFunctionCall("readValueForCharacteristic")
    peripheralDelegateOverride?.peripheral(
      peripheral = this,
      didUpdateValueForCharacteristic = characteristic,
      error = null,
    )
  }

  override fun readValueForDescriptor(descriptor: CBDescriptor) {
    logFunctionCall("readValueForDescriptor")
    peripheralDelegateOverride?.peripheral(
      peripheral = this,
      didUpdateValueForDescriptor = descriptor,
      error = null,
    )
  }

  override fun setDelegate(delegate: CBPeripheralDelegateProtocol?) {
    calledFunctions.clear()
    peripheralDelegateOverride = delegate
    logFunctionCall("setDelegate")
  }

  override fun setNotifyValue(
    enabled: Boolean,
    forCharacteristic: CBCharacteristic,
  ) {
    logFunctionCall("setNotifyValue")
    peripheralDelegateOverride?.peripheral(
      peripheral = this,
      didWriteValueForCharacteristic = forCharacteristic,
      error = null,
    )
  }

  override fun writeValue(
    data: NSData,
    forDescriptor: CBDescriptor,
  ) {
    logFunctionCall("writeValueforDescriptor")
    peripheralDelegateOverride?.peripheral(
      peripheral = this,
      didWriteValueForDescriptor = forDescriptor,
      error = null,
    )
  }

  override fun writeValue(
    data: NSData,
    forCharacteristic: CBCharacteristic,
    type: Long,
  ) {
    logFunctionCall("writeValueforCharacteristic")
    peripheralDelegateOverride?.peripheral(
      peripheral = this,
      didWriteValueForCharacteristic = forCharacteristic,
      error = null,
    )
  }

  override fun identifier(): NSUUID {
    return identifierOverride
  }
}
