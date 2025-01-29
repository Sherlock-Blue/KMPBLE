package com.sherlockblue.kmpble.scanning

import com.sherlockblue.kmpble.peripheral.Peripheral

data class ScannedResult(val peripheral: Peripheral, val advertisingData: AdvertisementData, val rssi: Int)
