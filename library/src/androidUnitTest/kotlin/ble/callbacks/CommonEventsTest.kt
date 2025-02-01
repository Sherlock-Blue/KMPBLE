package com.sherlockblue.kmpble.ble.callbacks

import android.bluetooth.BluetoothGatt
import com.sherlockblue.kmpble.DEFAULT_MTU_SIZE
import com.sherlockblue.kmpble.ble.BleResponse
import com.sherlockblue.kmpble.ble.fixtures.MockBluetoothGatt
import com.sherlockblue.kmpble.ble.fixtures.TEST_MTU
import com.sherlockblue.kmpble.ble.fixtures.TEST_RSSI
import com.sherlockblue.kmpble.ble.fixtures.TEST_RX
import com.sherlockblue.kmpble.ble.fixtures.TEST_STATUS
import com.sherlockblue.kmpble.ble.fixtures.TEST_TX
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class CommonEventsTest {
  // onConnectionStateChange

  @Test
  fun `onConnectionStateChange callback emits an ConnectionState BleResponse`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleGatt: BluetoothGatt = MockBluetoothGatt.Builder().build()
      val mockStatus = BluetoothGatt.GATT_SUCCESS
      val mockNewState = BluetoothGatt.STATE_CONNECTED

      // Prepare object under test
      val gattCallbackHandler = GattCallbackHandler(this)

      // Act
      gattCallbackHandler.onConnectionStateChange(mockBleGatt, mockStatus, mockNewState)

      // Assert
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleResponse.ConnectionStateChange)
    }

  @Test
  fun `onConnectionStateChange callback emits an ConnectionStateChanged BleResponse with correct status`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.ConnectionStateChange).status == mockStatus,
      )
    }

  @Test
  fun `onConnectionStateChange callback emits an ConnectionStateChanged BleResponse with correct newState`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.ConnectionStateChange).newState == mockNewState,
      )
    }

  // onMtuChanged

  @Test
  fun `onMtuChanged callback emits an MtuChanged BleResponse`() =
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
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleResponse.MtuChanged)
    }

  @Test
  fun `onMtuChanged callback emits an MtuChanged BleResponse with correct mtu`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.MtuChanged).mtu == mockMtu,
      )
    }

  @Test
  fun `onMtuChanged callback emits an MtuChanged BleResponse with correct status`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.MtuChanged).status == mockStatus,
      )
    }

  // onReadRemoteRssi

  @Test
  fun `onReadRemoteRssi callback emits an ReadRemoteRssi BleResponse`() =
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
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleResponse.ReadRemoteRssi)
    }

  @Test
  fun `onReadRemoteRssi callback emits an ReadRemoteRssi BleResponse with correct status`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.ReadRemoteRssi).status == mockStatus,
      )
    }

  @Test
  fun `onReadRemoteRssi callback emits an ReadRemoteRssi BleResponse with correct rssi`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.ReadRemoteRssi).rssi == mockRSSI,
      )
    }

  // onReliableWriteCompleted

  @Test
  fun `onReliableWriteCompleted callback emits an ReliableWriteCompleted BleResponse`() =
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
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleResponse.ReliableWriteCompleted)
    }

  @Test
  fun `onReliableWriteCompleted callback emits an ReliableWriteCompleted BleResponse with correct status`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.ReliableWriteCompleted).status == mockStatus,
      )
    }

  // onServicesDiscovered

  @Test
  fun `onServicesDiscovered callback emits an ServicesDiscovered BleResponse`() =
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
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleResponse.ServicesDiscovered)
    }

  @Test
  fun `onServicesDiscovered callback emits an ServicesDiscovered BleResponse with correct status`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.ServicesDiscovered).status == mockStatus,
      )
    }

  // onPhyRead

  @Test
  fun `onPhyRead callback emits an PhyRead BleResponse`() =
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
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleResponse.PhyRead)
    }

  @Test
  fun `onPhyRead callback emits an PhyRead BleResponse with correct status`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.PhyRead).status == mockStatus,
      )
    }

  @Test
  fun `onPhyRead callback emits an PhyRead BleResponse with correct Tx`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.PhyRead).txPhy == mockTx,
      )
    }

  @Test
  fun `onPhyRead callback emits an PhyRead BleResponse with correct Rx`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.PhyRead).rxPhy == mockRx,
      )
    }

  // onPhyUpdate

  @Test
  fun `onPhyUpdate callback emits an PhyUpdate BleResponse`() =
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
      Assert.assertTrue(gattCallbackHandler.eventBus().first() is BleResponse.PhyUpdate)
    }

  @Test
  fun `onPhyUpdate callback emits an PhyUpdate BleResponse with correct status`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.PhyUpdate).status == mockStatus,
      )
    }

  @Test
  fun `onPhyUpdate callback emits an PhyUpdate BleResponse with correct Tx`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.PhyUpdate).txPhy == mockTx,
      )
    }

  @Test
  fun `onPhyUpdate callback emits an PhyUpdate BleResponse with correct Rx`() =
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
        (gattCallbackHandler.eventBus().first() as BleResponse.PhyUpdate).rxPhy == mockRx,
      )
    }
}
