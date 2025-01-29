package utils

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.convert
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.create
import platform.posix.memcpy

// Source: https://stackoverflow.com/questions/58521108/how-to-convert-kotlin-bytearray-to-nsdata-and-viceversa

// ByteArray -> NSData
//     memScoped is a Kotlin/Native cinterop function used to manage memory allocation without JVM garbage
//     collection. Any memory allocated inside the memScoped block will automatically be freed when it is exited.
//     NSData.create is an iOS native function which returns an NSData object of the specified length and
//     starting at the bytes pointer.

@OptIn(ExperimentalForeignApi::class, kotlinx.cinterop.BetaInteropApi::class)
fun ByteArray.toNSData(): NSData =
  memScoped {
    NSData.create(
      bytes = allocArrayOf(this@toNSData),
      length = size.convert(),
    )
  }

// NSData -> ByteArray
//     usePinned is a Kotlin/Native function that fixes the location of an object in memory ensuring it won't be
//     relocated or garbage collected. memcpy is a function in the C Standard Library which is wrapped and exposed
//     by Kotlin/Native.

@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray {
  return ByteArray(length.toInt()).apply {
    usePinned {
      memcpy(it.addressOf(0), bytes, length)
    }
  }
}
