package com.sherlockblue.kmpble.fixtures

import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBCentralManagerStatePoweredOn
import platform.CoreBluetooth.CBPeripheral
import platform.Foundation.NSNumber

class MockCBCentralManager(
  val centralManagerDelegateOverride: CBCentralManagerDelegateProtocol? = null,
  val isScanningOverride: Boolean = false,
  val bluetoothState: Long = CBCentralManagerStatePoweredOn,
) : CBCentralManager(null, null, null) {
  private var mockDiscoveredPeripherals: MutableList<MockDiscoveredPeripheral> = mutableListOf()

  fun setMockDiscoveredPeripherals(mockDiscoveredPeripherals: List<MockDiscoveredPeripheral>) {
    this.mockDiscoveredPeripherals = mockDiscoveredPeripherals.toMutableList()
  }

  override fun delegate(): CBCentralManagerDelegateProtocol? {
    return centralManagerDelegateOverride
  }

  private val calledFunctions: MutableList<String> = mutableListOf()

  fun verify(
    atLeast: Int = 0,
    atMost: Int = Int.MAX_VALUE,
    exactly: Int = -1,
    functionName: String,
  ) = (calledFunctions.count { it.contains(functionName, ignoreCase = true) } in maxOf(atLeast, exactly)..minOf(atMost, exactly))

  fun verify(
    atLeast: Int = 0,
    atMost: Int = Int.MAX_VALUE,
    exactly: Int = -1,
    functionName: () -> String,
  ) = (calledFunctions.count { it.contains(functionName(), ignoreCase = true) } in maxOf(atLeast, exactly)..minOf(atMost, exactly))

  private fun recordFunctionCall(functionName: String) {
    calledFunctions.add(functionName)
  }

  fun wasCalled(functionName: String): Int = calledFunctions.count { it.contains(functionName, ignoreCase = true) }

  override fun state(): Long {
    recordFunctionCall("state")
    return bluetoothState
  }

  override fun isScanning(): Boolean {
    recordFunctionCall("isScanning")
    return isScanningOverride
  }

  override fun scanForPeripheralsWithServices(
    serviceUUIDs: List<*>?,
    options: Map<Any?, *>?,
  ) {
    recordFunctionCall("scanForPeripheralsWithServices")
    mockDiscoveredPeripherals.forEach { scanResult ->
      centralManagerDelegateOverride?.centralManager(
        central = this,
        didDiscoverPeripheral = scanResult.didDiscoverPeripheral,
        advertisementData = scanResult.advertisementData,
        RSSI = scanResult.RSSI,
      )
    }
  }

  override fun connectPeripheral(
    peripheral: CBPeripheral,
    options: Map<Any?, *>?,
  ) {
    recordFunctionCall("connectPeripheral")
    centralManagerDelegateOverride?.centralManager(central = this, didConnectPeripheral = peripheral)
  }

  override fun cancelPeripheralConnection(peripheral: CBPeripheral) {
    recordFunctionCall("cancelPeripheralConnection")
    centralManagerDelegateOverride?.centralManager(
      central = this,
      didDisconnectPeripheral = peripheral,
      error = null,
    )
  }

  override fun stopScan() {
    recordFunctionCall("stopScan")
  }

  class MockDiscoveredPeripheral(
    val didDiscoverPeripheral: CBPeripheral = mockCBPeripheral,
    val advertisementData: Map<Any?, *> = DEFAULT_ADVERTISING_DATA.build(),
    val RSSI: NSNumber = NSNumber(0),
  )
}
