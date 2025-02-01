package utils

import com.sherlockblue.kmpble.ble.Characteristic
import com.sherlockblue.kmpble.ble.Descriptor
import com.sherlockblue.kmpble.ble.Service
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBService
import platform.Foundation.NSData

fun CBService.toService(): Service = Service(UUID = UUID.toString(), characteristics = characteristics?.getCharacteristics() ?: listOf())

fun CBCharacteristic.toCharacteristic(): Characteristic =
  Characteristic(
    UUID = UUID.toString(),
    serviceUUID = service?.UUID.toString(),
    data = value?.toByteArray() ?: byteArrayOf(),
    descriptors = descriptors?.getDescriptors() ?: listOf(),
  )

fun CBDescriptor.toDescriptor(): Descriptor =
  Descriptor(
    UUID = UUID.toString(),
    characteristicUUID = characteristic?.UUID.toString(),
    data = (value as? NSData)?.toByteArray() ?: byteArrayOf(),
  )

fun List<*>.getServices(): List<Service> = map { service -> (service as CBService).toService() }

fun List<*>.getCharacteristics() = map { characteristic -> (characteristic as CBCharacteristic).toCharacteristic() }

fun List<*>.getDescriptors() = map { descriptor -> (descriptor as CBDescriptor).toDescriptor() }
