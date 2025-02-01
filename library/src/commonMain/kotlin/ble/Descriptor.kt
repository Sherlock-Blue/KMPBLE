package com.sherlockblue.kmpble.ble

data class Descriptor(
  val UUID: String,
  val characteristicUUID: String,
  val data: ByteArray,
)
