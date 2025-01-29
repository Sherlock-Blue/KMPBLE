package com.sherlockblue.kmpble.scanning

data class ManufacturerData(private val manufacturerDataPayload: ByteArray) {
  val manufacturerDataArray = manufacturerDataPayload
  val manufacturerId = manufacturerDataPayload.take(2).toByteArray()
  val manufacturerData = manufacturerDataPayload.copyOfRange(2, manufacturerDataPayload.size)
}
