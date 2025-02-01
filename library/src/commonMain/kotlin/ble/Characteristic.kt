package com.sherlockblue.kmpble.ble

data class Characteristic(
  val UUID: String,
  val serviceUUID: String,
  val data: ByteArray,
  val descriptors: List<Descriptor>,
)
