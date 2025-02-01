package com.sherlockblue.kmpble.scanning

import android.bluetooth.le.ScanResult
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.util.forEach

@RequiresApi(Build.VERSION_CODES.O)
fun advertisementDataFromScanResult(scanResult: ScanResult): AdvertisementData =
  AdvertisementData(
    payload = scanResult.scanRecord!!.bytes,
    flags = listOf(scanResult.scanRecord!!.advertiseFlags),
    name = scanResult.scanRecord!!.deviceName,
    deviceAddress = scanResult.device.address,
    txPowerLevel = scanResult.scanRecord!!.txPowerLevel,
    advertisingInterval = scanResult.periodicAdvertisingInterval,
    manufacturerData =
      mutableListOf<ManufacturerData>().apply {
        scanResult.scanRecord!!.manufacturerSpecificData.forEach { _, value ->
          add(ManufacturerData(value))
        }
      },
    serviceData =
      scanResult.scanRecord!!.serviceData.keys.fold(mutableListOf<ServiceData>(), { mutableList, serviceUuid ->
        mutableList.plus(
          ServiceData(
            serviceUUID = serviceUuid.uuid.toString(),
            serviceData = scanResult.scanRecord!!.serviceData[serviceUuid]!!,
          ),
        ) as MutableList
      }).toList(),
  )
