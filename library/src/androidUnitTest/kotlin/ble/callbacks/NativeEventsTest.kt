package com.sherlockblue.kmpble.ble.callbacks

import android.bluetooth.BluetoothGatt
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGatt
import com.sherlockblue.kmpble.ble.fixtures.TEST_MTU
import com.sherlockblue.kmpble.ble.fixtures.TEST_RSSI
import com.sherlockblue.kmpble.ble.fixtures.TEST_RX
import com.sherlockblue.kmpble.ble.fixtures.TEST_STATUS
import com.sherlockblue.kmpble.ble.fixtures.TEST_TX
import com.sherlockblue.kmpble.constants.DEFAULT_MTU_SIZE
import com.sherlockblue.kmpble.constants.getErrorMessage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class NativeEventsTest {
  // onConnectionStateChange

  @Test
  fun `onConnectionStateChange callback emits an OnConnectionState BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockStatus = TEST_STATUS
      val mockNewState = BluetoothGatt.STATE_CONNECTED

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onConnectionStateChange(mockBleGatt, mockStatus, mockNewState)

      // Assert
      Assert.assertTrue(gattCallbackHandler.nativeEventBus().first() is OnConnectionStateChange)
    }

  @Test
  fun `onConnectionStateChange callback emits an OnConnectionStateChanged BleEvent with same Gatt instance`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockStatus = TEST_STATUS
      val mockNewState = BluetoothGatt.STATE_CONNECTED

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onConnectionStateChange(mockBleGatt, mockStatus, mockNewState)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnConnectionStateChange).gatt === mockBleGatt,
      )
    }

  @Test
  fun `onConnectionStateChange callback emits an OnConnectionStateChanged BleEvent with correct status`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockStatus = TEST_STATUS
      val mockNewState = BluetoothGatt.STATE_CONNECTED

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onConnectionStateChange(mockBleGatt, mockStatus, mockNewState)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnConnectionStateChange).status == mockStatus,
      )
    }

  @Test
  fun `onConnectionStateChange callback emits an OnConnectionStateChanged BleEvent with correct newState`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockStatus = TEST_STATUS
      val mockNewState = Int.MAX_VALUE

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onConnectionStateChange(mockBleGatt, mockStatus, mockNewState)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnConnectionStateChange).newState == mockNewState,
      )
    }

  // onServiceChanged

  @Test
  fun `onServiceChanged callback emits an OnServiceChanged BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onServiceChanged(mockBleGatt)

      // Assert
      Assert.assertTrue(gattCallbackHandler.nativeEventBus().first() is OnServiceChanged)
    }

  @Test
  fun `onServiceChanged callback emits an OnServiceChanged BleEvent with same Gatt instance`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onServiceChanged(mockBleGatt)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnServiceChanged).gatt === mockBleGatt,
      )
    }

  // onMtuChanged

  @Test
  fun `onMtuChanged callback emits an OnMtuChanged BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockStatus = TEST_STATUS
      val mockMtu = TEST_MTU

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onMtuChanged(mockBleGatt, mockMtu, mockStatus)

      // Assert
      Assert.assertTrue(gattCallbackHandler.nativeEventBus().first() is OnMtuChanged)
    }

  @Test
  fun `onMtuChanged callback emits an OnMtuChanged BleEvent with same Gatt instance`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockStatus = TEST_STATUS
      val mockMtu = DEFAULT_MTU_SIZE

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onMtuChanged(mockBleGatt, mockMtu, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnMtuChanged).gatt === mockBleGatt,
      )
    }

  @Test
  fun `onMtuChanged callback emits an OnMtuChanged BleEvent with correct mtu`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockStatus = TEST_STATUS
      val mockMtu = TEST_MTU

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onMtuChanged(mockBleGatt, mockMtu, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnMtuChanged).mtu == mockMtu,
      )
    }

  @Test
  fun `onMtuChanged callback emits an OnMtuChanged BleEvent with correct status`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockStatus = TEST_STATUS
      val mockMtu = DEFAULT_MTU_SIZE

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onMtuChanged(mockBleGatt, mockMtu, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnMtuChanged).status == mockStatus,
      )
    }

  // onReadRemoteRssi

  @Test
  fun `onReadRemoteRssi callback emits an OnReadRemoteRssi BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockRSSI = TEST_RSSI
      val mockStatus = TEST_STATUS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onReadRemoteRssi(mockBleGatt, mockRSSI, mockStatus)

      // Assert
      Assert.assertTrue(gattCallbackHandler.nativeEventBus().first() is OnReadRemoteRssi)
    }

  @Test
  fun `onReadRemoteRssi callback emits an OnReadRemoteRssi BleEvent with same Gatt instance`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockRSSI = TEST_RSSI
      val mockStatus = TEST_STATUS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onReadRemoteRssi(mockBleGatt, mockRSSI, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnReadRemoteRssi).gatt === mockBleGatt,
      )
    }

  @Test
  fun `onReadRemoteRssi callback emits an OnReadRemoteRssi BleEvent with correct status`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockRSSI = TEST_RSSI
      val mockStatus = TEST_STATUS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onReadRemoteRssi(mockBleGatt, mockRSSI, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnReadRemoteRssi).status == mockStatus,
      )
    }

  @Test
  fun `onReadRemoteRssi callback emits an OnReadRemoteRssi BleEvent with correct rssi`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockRSSI = TEST_RSSI
      val mockStatus = TEST_STATUS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onReadRemoteRssi(mockBleGatt, mockRSSI, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnReadRemoteRssi).rssi == mockRSSI,
      )
    }

  // onReliableWriteCompleted

  @Test
  fun `onReliableWriteCompleted callback emits an OnReliableWriteCompleted BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockStatus = TEST_STATUS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onReliableWriteCompleted(mockBleGatt, mockStatus)

      // Assert
      Assert.assertTrue(gattCallbackHandler.nativeEventBus().first() is OnReliableWriteCompleted)
    }

  @Test
  fun `onReliableWriteCompleted callback emits an OnReliableWriteCompleted BleEvent with same Gatt instance`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockStatus = TEST_STATUS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onReliableWriteCompleted(mockBleGatt, mockStatus)
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnReliableWriteCompleted).gatt === mockBleGatt,
      )
    }

  @Test
  fun `onReliableWriteCompleted callback emits an OnReliableWriteCompleted BleEvent with correct status`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockStatus = TEST_STATUS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onReliableWriteCompleted(mockBleGatt, mockStatus)
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnReliableWriteCompleted).status == mockStatus,
      )
    }

  // onServicesDiscovered

  @Test
  fun `onServicesDiscovered callback emits an OnServicesDiscovered BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockStatus = TEST_STATUS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onServicesDiscovered(mockBleGatt, mockStatus)

      // Assert
      Assert.assertTrue(gattCallbackHandler.nativeEventBus().first() is OnServicesDiscovered)
    }

  @Test
  fun `onServicesDiscovered callback emits an OnServicesDiscovered BleEvent with same Gatt instance`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockStatus = TEST_STATUS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onServicesDiscovered(mockBleGatt, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnServicesDiscovered).gatt === mockBleGatt,
      )
    }

  @Test
  fun `onServicesDiscovered callback emits an OnServicesDiscovered BleEvent with correct status`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockStatus = TEST_STATUS

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onServicesDiscovered(mockBleGatt, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnServicesDiscovered).status == mockStatus,
      )
    }

  // onPhyRead

  @Test
  fun `onPhyRead callback emits an OnPhyRead BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockStatus = TEST_STATUS
      val mockTx = TEST_TX
      val mockRx = TEST_RX

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onPhyRead(mockBleGatt, mockTx, mockRx, mockStatus)

      // Assert
      Assert.assertTrue(gattCallbackHandler.nativeEventBus().first() is OnPhyRead)
    }

  @Test
  fun `onPhyRead callback emits an OnPhyRead BleEvent with same Gatt instance`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockStatus = TEST_STATUS
      val mockTx = TEST_TX
      val mockRx = TEST_RX

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onPhyRead(mockBleGatt, mockTx, mockRx, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnPhyRead).gatt === mockBleGatt,
      )
    }

  @Test
  fun `onPhyRead callback emits an OnPhyRead BleEvent with correct status`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockStatus = TEST_STATUS
      val mockTx = TEST_TX
      val mockRx = TEST_RX

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onPhyRead(mockBleGatt, mockTx, mockRx, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnPhyRead).status == mockStatus,
      )
    }

  @Test
  fun `onPhyRead callback emits an OnPhyRead BleEvent with correct Tx`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockTx = TEST_TX
      val mockRx = TEST_RX

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onPhyRead(mockBleGatt, mockTx, mockRx, BluetoothGatt.GATT_SUCCESS)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnPhyRead).txPhy == mockTx,
      )
    }

  @Test
  fun `onPhyRead callback emits an OnPhyRead BleEvent with correct Rx`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockTx = TEST_TX
      val mockRx = TEST_RX

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onPhyRead(mockBleGatt, mockTx, mockRx, BluetoothGatt.GATT_SUCCESS)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnPhyRead).rxPhy == mockRx,
      )
    }

  // onPhyUpdate

  @Test
  fun `onPhyUpdate callback emits an OnPhyUpdate BleEvent`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockTx = TEST_TX
      val mockRx = TEST_RX

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onPhyUpdate(mockBleGatt, mockTx, mockRx, BluetoothGatt.GATT_SUCCESS)

      // Assert
      Assert.assertTrue(gattCallbackHandler.nativeEventBus().first() is OnPhyUpdate)
    }

  @Test
  fun `onPhyUpdate callback emits an OnPhyUpdate BleEvent with same Gatt instance`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockTx = TEST_TX
      val mockRx = TEST_RX

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onPhyUpdate(mockBleGatt, mockTx, mockRx, BluetoothGatt.GATT_SUCCESS)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnPhyUpdate).gatt === mockBleGatt,
      )
    }

  @Test
  fun `onPhyUpdate callback emits an OnPhyUpdate BleEvent with correct status`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockStatus = TEST_STATUS
      val mockTx = TEST_TX
      val mockRx = TEST_RX

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onPhyUpdate(mockBleGatt, mockTx, mockRx, mockStatus)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnPhyUpdate).status == mockStatus,
      )
    }

  @Test
  fun `onPhyUpdate callback emits an OnPhyUpdate BleEvent with correct Tx`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockTx = TEST_TX
      val mockRx = TEST_RX

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onPhyUpdate(mockBleGatt, mockTx, mockRx, BluetoothGatt.GATT_SUCCESS)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnPhyUpdate).txPhy == mockTx,
      )
    }

  @Test
  fun `onPhyUpdate callback emits an OnPhyUpdate BleEvent with correct Rx`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockTx = TEST_TX
      val mockRx = TEST_RX

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onPhyUpdate(mockBleGatt, mockTx, mockRx, BluetoothGatt.GATT_SUCCESS)

      // Assert
      Assert.assertTrue(
        (gattCallbackHandler.nativeEventBus().first() as OnPhyUpdate).rxPhy == mockRx,
      )
    }

  @Test
  fun `GATT error code 0 returns the Success message`() {
    // Assert
    Assert.assertTrue(
      getErrorMessage(0) == "Success",
    )
  }

  @Test
  fun `GATT error code 133 returns the Unspecified Error message`() {
    // Assert
    Assert.assertTrue(
      getErrorMessage(133) == "Unspecified Error",
    )
  }

  @Test
  fun `Invalid GATT error code returns the Unknown Error message`() {
    // Assert
    Assert.assertTrue(
      getErrorMessage(Int.MAX_VALUE) == "Unknown Error",
    )
  }
}
