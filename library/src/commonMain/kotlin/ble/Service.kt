package com.sherlockblue.kmpble.ble

data class Service(
  val UUID: String,
  val characteristics: List<Characteristic>,
)
