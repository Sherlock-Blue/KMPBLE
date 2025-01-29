package peripheral.coroutines

import com.sherlockblue.kmpble.callbacks.BleEvent
import com.sherlockblue.kmpble.callbacks.CentralManagerCallbacks
import com.sherlockblue.kmpble.callbacks.PeripheralCallbacks
import com.sherlockblue.kmpble.fixtures.DEFAULT_TEST_RUNS
import com.sherlockblue.kmpble.fixtures.DEFAULT_UUID
import com.sherlockblue.kmpble.fixtures.MockCBCentralManager
import com.sherlockblue.kmpble.fixtures.mockCBPeripheral
import com.sherlockblue.kmpble.peripheral.Peripheral
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertTrue

class PeripheralTest {
  @Test
  fun `connect function calls connectPeripheral in the CBCentralManager exactly once`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val mockCBCentralManager = MockCBCentralManager(centralManagerDelegateOverride = CentralManagerCallbacks(this))
        val mockPeripheralCallbacks = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(mockPeripheralCallbacks)

        // Prepare object under test
        val testPeripheral =
          Peripheral(
            coroutineScope = this,
            centralManager = mockCBCentralManager,
            peripheral = mockCBPeripheral,
          )

        // Act
        testPeripheral.connect()

        // Assert
        assertTrue(mockCBCentralManager.verify(exactly = 1, functionName = "connectPeripheral"))
      }
    }

  @Test
  fun `connect function returns OnPeripheralConnect BleEvent`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val mockCBCentralManager = MockCBCentralManager(centralManagerDelegateOverride = CentralManagerCallbacks(this))
        val mockPeripheralCallbacks = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(mockPeripheralCallbacks)

        // Prepare object under test
        val testPeripheral =
          Peripheral(
            coroutineScope = this,
            centralManager = mockCBCentralManager,
            peripheral = mockCBPeripheral,
          )

        // Act
        val connectionResult = testPeripheral.connect()

        // Assert
        assertTrue(connectionResult is BleEvent.OnPeripheralConnect)
      }
    }

  @Test
  fun `disconnect function calls cancelPeripheralConnection in the CBCentralManager exactly once`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val mockCBCentralManager = MockCBCentralManager(centralManagerDelegateOverride = CentralManagerCallbacks(this))
        val mockPeripheralCallbacks = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(mockPeripheralCallbacks)

        // Prepare object under test
        val testPeripheral =
          Peripheral(
            coroutineScope = this,
            centralManager = mockCBCentralManager,
            peripheral = mockCBPeripheral,
          )

        // Act
        testPeripheral.disconnect()

        // Assert
        assertTrue(mockCBCentralManager.verify(exactly = 1, functionName = "cancelPeripheralConnection"))
      }
    }

  @Test
  fun `disconnect function returns OnPeripheralDisconnect BleEvent`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val mockCBCentralManager = MockCBCentralManager(centralManagerDelegateOverride = CentralManagerCallbacks(this))
        val mockPeripheralCallbacks = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(mockPeripheralCallbacks)

        // Prepare object under test
        val testPeripheral =
          Peripheral(
            coroutineScope = this,
            centralManager = mockCBCentralManager,
            peripheral = mockCBPeripheral,
          )

        // Act
        val disconnectionEvent = testPeripheral.disconnect()

        // Assert
        assertTrue(disconnectionEvent is BleEvent.OnPeripheralDisconnect)
      }
    }

  @Test
  fun `discoverServices function calls discoverServices in the CBPeripheral exactly once`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val mockCBCentralManager = MockCBCentralManager(centralManagerDelegateOverride = CentralManagerCallbacks(this))
        val peripheralDelegate = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(peripheralDelegate)

        // Prepare object under test
        val testPeripheral =
          Peripheral(
            coroutineScope = this,
            centralManager = mockCBCentralManager,
            peripheral = mockCBPeripheral,
          )

        // Act
        testPeripheral.discoverServices()

        // Assert
        assertTrue(mockCBPeripheral.verify(exactly = 1, functionName = "discoverServices"))
      }
    }

  @Test
  fun `discoverServices function returns OnServicesDiscovered BleEvent`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val mockCBCentralManager = MockCBCentralManager(centralManagerDelegateOverride = CentralManagerCallbacks(this))
        val peripheralDelegate = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(peripheralDelegate)

        // Prepare object under test
        val testPeripheral =
          Peripheral(
            coroutineScope = this,
            centralManager = MockCBCentralManager(centralManagerDelegateOverride = CentralManagerCallbacks(this)),
            peripheral = mockCBPeripheral,
          )

        // Act
        val discoverServicesEvent = testPeripheral.discoverServices()

        // Assert
        assertTrue(discoverServicesEvent is BleEvent.OnServicesDiscovered)
      }
    }

  @Test
  fun `discoverCharacteristics function calls discoverCharacteristics in the CBPeripheral exactly once`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val mockCBCentralManager = MockCBCentralManager(centralManagerDelegateOverride = CentralManagerCallbacks(this))
        val peripheralDelegate = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(peripheralDelegate)

        // Prepare object under test
        val testPeripheral =
          Peripheral(
            coroutineScope = this,
            centralManager = mockCBCentralManager,
            peripheral = mockCBPeripheral,
          )

        // Act
        val discoverCharacteristicsEvent = testPeripheral.discoverCharacteristicsForService(DEFAULT_UUID)

        // Assert
        assertTrue(mockCBPeripheral.verify(exactly = 1, functionName = "discoverCharacteristics"))
      }
    }

  @Test
  fun `discoverCharacteristics function returns OnCharacteristicsDiscovered BleEvent`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val mockCBCentralManager = MockCBCentralManager(centralManagerDelegateOverride = CentralManagerCallbacks(this))
        val peripheralDelegate = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(peripheralDelegate)

        // Prepare object under test
        val testPeripheral =
          Peripheral(
            coroutineScope = this,
            centralManager = MockCBCentralManager(centralManagerDelegateOverride = CentralManagerCallbacks(this)),
            peripheral = mockCBPeripheral,
          )

        // Act
        val discoverCharacteristicsEvent = testPeripheral.discoverCharacteristicsForService(DEFAULT_UUID)

        // Assert
        assertTrue(discoverCharacteristicsEvent is BleEvent.OnCharacteristicsDiscovered)
      }
    }

  @Test
  fun `discoverDescriptors function calls discoverDescriptors in the CBPeripheral exactly once`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val mockCBCentralManager = MockCBCentralManager(centralManagerDelegateOverride = CentralManagerCallbacks(this))
        val peripheralDelegate = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(peripheralDelegate)

        // Prepare object under test
        val testPeripheral =
          Peripheral(
            coroutineScope = this,
            centralManager = mockCBCentralManager,
            peripheral = mockCBPeripheral,
          )

        // Act
        val discoverDescriptorsEvent = testPeripheral.discoverDescriptors(DEFAULT_UUID)

        // Assert
        assertTrue(mockCBPeripheral.verify(exactly = 1, functionName = "discoverDescriptors"))
      }
    }

  @Test
  fun `discoverDescriptors function returns OnDescriptorsDiscovered BleEvent`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val mockCBCentralManager = MockCBCentralManager(centralManagerDelegateOverride = CentralManagerCallbacks(this))
        val peripheralDelegate = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(peripheralDelegate)

        // Prepare object under test
        val testPeripheral =
          Peripheral(
            coroutineScope = this,
            centralManager = MockCBCentralManager(centralManagerDelegateOverride = CentralManagerCallbacks(this)),
            peripheral = mockCBPeripheral,
          )

        // Act
        val discoverDescriptorsEvent = testPeripheral.discoverDescriptors(DEFAULT_UUID)

        // Assert
        assertTrue(discoverDescriptorsEvent is BleEvent.OnDescriptorsDiscovered)
      }
    }

  @Test
  fun `readCharacteristic function calls readValueForCharacteristic in the CBPeripheral exactly once`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val mockCBCentralManager = MockCBCentralManager(centralManagerDelegateOverride = CentralManagerCallbacks(this))
        val peripheralDelegate = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(peripheralDelegate)

        // Prepare object under test
        val testPeripheral =
          Peripheral(
            coroutineScope = this,
            centralManager = mockCBCentralManager,
            peripheral = mockCBPeripheral,
          )

        // Act
        testPeripheral.readCharacteristic(DEFAULT_UUID)

        // Assert
        assertTrue(mockCBPeripheral.verify(exactly = 1, functionName = "readValueForCharacteristic"))
      }
    }

  @Test
  fun `readCharacteristic function returns OnCharacteristicUpdated BleEvent`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val mockCBCentralManager = MockCBCentralManager(centralManagerDelegateOverride = CentralManagerCallbacks(this))
        val peripheralDelegate = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(peripheralDelegate)

        // Prepare object under test
        val testPeripheral =
          Peripheral(
            coroutineScope = this,
            centralManager = mockCBCentralManager,
            peripheral = mockCBPeripheral,
          )

        // Act
        val discoverServicesEvent = testPeripheral.readCharacteristic(DEFAULT_UUID)

        // Assert
        assertTrue(discoverServicesEvent is BleEvent.OnCharacteristicUpdated)
      }
    }

  @Test
  fun `readDescriptor function calls readValueForDescriptor in the CBPeripheral exactly once`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val mockCBCentralManager = MockCBCentralManager(centralManagerDelegateOverride = CentralManagerCallbacks(this))
        val peripheralDelegate = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(peripheralDelegate)

        // Prepare object under test
        val testPeripheral =
          Peripheral(
            coroutineScope = this,
            centralManager = mockCBCentralManager,
            peripheral = mockCBPeripheral,
          )

        // Act
        testPeripheral.readDescriptor(DEFAULT_UUID, DEFAULT_UUID)

        // Assert
        assertTrue(mockCBPeripheral.verify(exactly = 1, functionName = "readValueForDescriptor"))
      }
    }

  @Test
  fun `readDescriptor function returns OnDescriptorUpdated BleEvent`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val mockCBCentralManager = MockCBCentralManager(centralManagerDelegateOverride = CentralManagerCallbacks(this))
        val peripheralDelegate = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(peripheralDelegate)

        // Prepare object under test
        val testPeripheral =
          Peripheral(
            coroutineScope = this,
            centralManager = mockCBCentralManager,
            peripheral = mockCBPeripheral,
          )

        // Act
        val discoverServicesEvent = testPeripheral.readDescriptor(DEFAULT_UUID, DEFAULT_UUID)

        // Assert
        assertTrue(discoverServicesEvent is BleEvent.OnDescriptorUpdated)
      }
    }

  @Test
  fun `writeCharacteristic function calls writeValueForCharacteristic in the CBPeripheral exactly once`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val mockCBCentralManager = MockCBCentralManager(centralManagerDelegateOverride = CentralManagerCallbacks(this))
        val peripheralDelegate = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(peripheralDelegate)
        val mockData = byteArrayOf()

        // Prepare object under test
        val testPeripheral =
          Peripheral(
            coroutineScope = this,
            centralManager = mockCBCentralManager,
            peripheral = mockCBPeripheral,
          )

        // Act
        testPeripheral.writeCharacteristic(
          uuid = DEFAULT_UUID,
          data = mockData,
        )

        // Assert
        assertTrue(mockCBPeripheral.verify(exactly = 1, functionName = "writeValueForCharacteristic"))
      }
    }

  @Test
  fun `writeCharacteristic function returns OnCharacteristicWrite BleEvent`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val mockCBCentralManager = MockCBCentralManager(centralManagerDelegateOverride = CentralManagerCallbacks(this))
        val peripheralDelegate = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(peripheralDelegate)
        val mockData = byteArrayOf()

        // Prepare object under test
        val testPeripheral =
          Peripheral(
            coroutineScope = this,
            centralManager = mockCBCentralManager,
            peripheral = mockCBPeripheral,
          )

        // Act
        val discoverServicesEvent =
          testPeripheral.writeCharacteristic(
            uuid = DEFAULT_UUID,
            data = mockData,
          )

        // Assert
        assertTrue(discoverServicesEvent is BleEvent.OnCharacteristicWrite)
      }
    }

  @Test
  fun `writeDescriptor function calls writeValueForDescriptor in the CBPeripheral exactly once`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val mockCBCentralManager = MockCBCentralManager(centralManagerDelegateOverride = CentralManagerCallbacks(this))
        val peripheralDelegate = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(peripheralDelegate)
        val mockData = byteArrayOf()

        launch {
          // Prepare object under test
          val testPeripheral =
            Peripheral(
              coroutineScope = this,
              centralManager = mockCBCentralManager,
              peripheral = mockCBPeripheral,
            )

          // Act
          testPeripheral.writeDescriptor(
            characteristicUUID = DEFAULT_UUID,
            descriptorUUID = DEFAULT_UUID,
            data = mockData,
          )
        }.join()

        // Assert
        assertTrue(mockCBPeripheral.verify(exactly = 1, functionName = "writeValueForDescriptor"))
      }
    }

  @Test
  fun `writeDescriptor function returns OnDescriptorWrite BleEvent`() =
    runTest {
      repeat(DEFAULT_TEST_RUNS) {
        // Arrange
        // Mocked Fixtures
        val mockCBCentralManager = MockCBCentralManager(centralManagerDelegateOverride = CentralManagerCallbacks(this))
        val peripheralDelegate = PeripheralCallbacks(this)
        mockCBPeripheral.setDelegate(peripheralDelegate)
        val mockData = byteArrayOf()

        // Prepare object under test
        val testPeripheral =
          Peripheral(
            coroutineScope = this,
            centralManager = mockCBCentralManager,
            peripheral = mockCBPeripheral,
          )

        // Act
        val writeDescriptorEvent =
          testPeripheral.writeDescriptor(
            characteristicUUID = DEFAULT_UUID,
            descriptorUUID = DEFAULT_UUID,
            data = mockData,
          )

        // Assert
        assertTrue(writeDescriptorEvent is BleEvent.OnDescriptorWrite)
      }
    }
}
