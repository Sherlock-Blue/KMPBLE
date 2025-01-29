package com.sherlockblue.kmpble.scanning

data class AdvertisementData(
  // Shared Fields
  val name: String?,
  val deviceAddress: String?,
  val txPowerLevel: Int?,
  val manufacturerData: MutableList<ManufacturerData>?,
  val serviceData: List<ServiceData>?,
  // Android Specific Fields
  val payload: ByteArray? = null,
  val flags: List<Int>? = null,
  val advertisingInterval: Int? = null,
  // iOS Specific Fields
  val isConnectable: Boolean? = null,
  val serviceUuids: List<String>? = null,
  // SIG Fields
  val incomplete16BitUUIDs: List<String>? = null,
  val complete16BitUUIDs: List<String>? = null,
  val incomplete32BitUUIDs: List<String>? = null,
  val complete32BitUUIDs: List<String>? = null,
  val incomplete128BitUUIDs: List<String>? = null,
  val complete128BitUUIDs: List<String>? = null,
  val appearance: Int? = null,
  val shortenedName: String? = null,
  val publicTargetAddress: String? = null,
  val randomTargetAddress: String? = null,
  val leRole: Int? = null,
  val leSecureConnectionsOob: ByteArray? = null,
  val leSupportedFeatures: ByteArray? = null,
  val uri: String? = null,
  val channelMapUpdateIndication: ByteArray? = null,
)
