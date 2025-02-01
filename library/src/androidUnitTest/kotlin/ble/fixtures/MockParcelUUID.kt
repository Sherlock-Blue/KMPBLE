package ble.fixtures

import android.os.ParcelUuid
import com.sherlockblue.kmpble.ble.fixtures.TEST_UUID
import io.mockk.every
import io.mockk.mockk
import java.util.UUID

class MockParcelUUID {
  class Builder {
    private var mockUUID: UUID = UUID.fromString(TEST_UUID)

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
