package com.sherlockblue.kmpble.ble

sealed class BleResponse {
  class MtuChanged(val mtu: Int, val status: Int) : BleResponse()

  class ReadRemoteRssi(val rssi: Int, val status: Int) : BleResponse()

  class ReliableWriteCompleted(val status: Int) : BleResponse()

  class ServicesDiscovered(val status: Int) : BleResponse()

  class ConnectionStateChange(val status: Int, val newState: Int) : BleResponse()

  class PhyRead(val txPhy: Int, val rxPhy: Int, val status: Int) : BleResponse()

  class PhyUpdate(val txPhy: Int, val rxPhy: Int, val status: Int) : BleResponse()

  class Error(val message: String, val status: Int) : BleResponse()

  class DescriptorWrite(
    val descriptorUUID: String,
    val status: Int,
  ) : BleResponse()

  class DescriptorRead(
    val descriptorUUID: String,
    val status: Int,
    val data: ByteArray,
  ) : BleResponse()

  class CharacteristicChanged(
    val characteristicUUID: String,
    val data: ByteArray,
  ) : BleResponse()

  class CharacteristicWrite(
    val characteristicUUID: String,
    val status: Int,
  ) : BleResponse()

  class CharacteristicRead(
    val characteristicUUID: String,
    val data: ByteArray,
    val status: Int,
  ) : BleResponse()
}
