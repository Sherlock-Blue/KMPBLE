package ble.fixtures

import android.os.ParcelUuid
import io.mockk.every
import io.mockk.mockk
import java.util.UUID

class MockParcelUUID {
  class Builder {
    private var mockUUID: UUID = UUID.fromString("00000000-0000-1000-8000-00805F9B34FB")

    fun setUUID(newUUID: UUID): Builder {
      this.mockUUID = newUUID
      return this
    }

    fun build(): ParcelUuid =
      mockk<ParcelUuid>().apply {
        val mockUUID: UUID = mockUUID
        every { uuid } returns mockUUID
      }
  }
}
