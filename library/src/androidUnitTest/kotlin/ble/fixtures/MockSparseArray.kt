package ble.fixtures

import android.util.SparseArray
import io.mockk.every
import io.mockk.mockk

class MockSparseArray {
  class Builder {
    private var mockData: ByteArray =
      byteArrayOf(
        0x1A.toByte(),
        0x18.toByte(),
        0x10.toByte(),
        0x20.toByte(),
        0x30.toByte(),
        0x40.toByte(),
      )

    fun setData(newData: ByteArray): Builder {
      this.mockData = newData
      return this
    }

    fun build(): SparseArray<ByteArray> =
      mockk<SparseArray<ByteArray>>().apply {
        val mockData: ByteArray = mockData

        every { size() } returns 1
        every { keyAt(any()) } returns 0
        every { valueAt(any()) } returns mockData
      }
  }
}
