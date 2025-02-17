package com.sherlockblue.kmpble.scanning

import platform.CoreBluetooth.CBAdvertisementDataIsConnectable
import platform.CoreBluetooth.CBAdvertisementDataLocalNameKey
import platform.CoreBluetooth.CBAdvertisementDataManufacturerDataKey
import platform.CoreBluetooth.CBAdvertisementDataServiceDataKey
import platform.CoreBluetooth.CBAdvertisementDataServiceUUIDsKey
import platform.CoreBluetooth.CBAdvertisementDataTxPowerLevelKey
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBUUID
import platform.Foundation.NSData
import utils.toByteArray

fun CBPeripheral.parseAdvertisementData(advertisingDataMap: Map<Any?, *>): AdvertisementData {
  val name: String? = advertisingDataMap[CBAdvertisementDataLocalNameKey] as? String
  val deviceAddress: String? = identifier()?.UUIDString()
  val isConnectable: Boolean? = advertisingDataMap[CBAdvertisementDataIsConnectable] as? Boolean
  val txPowerLevel: Int? = advertisingDataMap[CBAdvertisementDataTxPowerLevelKey] as? Int

  val manufacturerData = advertisingDataMap[CBAdvertisementDataManufacturerDataKey] as? ByteArray

  val serviceUuids: List<String> =
    (advertisingDataMap[CBAdvertisementDataServiceUUIDsKey] as? List<*>)?.map { (it as CBUUID).UUIDString() } ?: emptyList()
  val serviceData: Map<String, ByteArray> =
    (advertisingDataMap[CBAdvertisementDataServiceDataKey] as? Map<*, *>)?.map { it ->
      (it.key as CBUUID).UUIDString() to (it.value as NSData).toByteArray()
    }?.toMap() ?: emptyMap()

  return AdvertisementData(
    name = name,
    deviceAddress = deviceAddress,
    txPowerLevel = txPowerLevel,
    isConnectable = isConnectable,
    manufacturerData = manufacturerData?.let { mutableListOf(ManufacturerData(manufacturerData)) } ?: mutableListOf(),
    serviceUuids = serviceUuids,
    serviceData =
      serviceData.keys.fold(mutableListOf<ServiceData>(), { mutableList, serviceUuid ->
        mutableList.plus(
          ServiceData(
            serviceUUID = serviceUuid,
            serviceData = serviceData[serviceUuid]!!,
          ),
        ) as MutableList
      }).toList(),
  )
}
