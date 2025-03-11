package peripheral.callbacks

import com.sherlockblue.kmpble.ble.BleResponse
import com.sherlockblue.kmpble.callbacks.CentralManagerCallbacks
import com.sherlockblue.kmpble.callbacks.OnCharacteristicsDiscovered
import com.sherlockblue.kmpble.callbacks.OnDescriptorsDiscovered
import com.sherlockblue.kmpble.callbacks.OnServicesDiscovered
import com.sherlockblue.kmpble.callbacks.PeripheralCallbacks
import com.sherlockblue.kmpble.fixtures.DEFAULT_TEST_RUNS
import com.sherlockblue.kmpble.fixtures.DEFAULT_UUID
import com.sherlockblue.kmpble.fixtures.MockCBCentralManager
import com.sherlockblue.kmpble.fixtures.MockCBCharacteristic
import com.sherlockblue.kmpble.fixtures.MockCBService
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
        testPeripheral.connect {
          // Assert
          assertTrue(mockCBCentralManager.verify(exactly = 1, functionName = "connectPeripheral"))
        }
        testPeripheral.disconnect()
      }
    }

  @Test
  fun `connect function returns PeripheralConnect BleResponse`() =
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
        testPeripheral.connect { bleEvent ->
          // Assert
          assertTrue(bleEvent is BleResponse.ConnectionStateChange)
        }
        testPeripheral.disconnect()
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
        testPeripheral.disconnect { bleEvent ->
          // Assert
          assertTrue(mockCBCentralManager.verify(exactly = 1, functionName = "cancelPeripheralConnection"))
        }
      }
    }

  @Test
  fun `disconnect function returns PeripheralDisconnect BleResponse`() =
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
        testPeripheral.disconnect { bleEvent ->
          // Assert
          assertTrue(bleEvent is BleResponse.ConnectionStateChange)
        }
      }
    }

  @Test
  fun `discoverCBServices function calls discoverServices in the CBPeripheral exactly once`() =
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
        val result = testPeripheral.discoverCBServices()

        // Assert
        assertTrue(mockCBPeripheral.verify(exactly = 1, functionName = "discoverServices"))

        testPeripheral.disconnect()
      }
    }

  @Test
  fun `discoverCBServices function returns ServicesDiscovered BleResponse`() =
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
        val result = testPeripheral.discoverCBServices()

        // Assert
        assertTrue(result is OnServicesDiscovered)

        testPeripheral.disconnect()
      }
    }

  @Test
  fun `discoverCBCharacteristics function calls discoverCharacteristics in the CBPeripheral exactly once`() =
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
        testPeripheral.discoverCBCharacteristics(MockCBService())

        // Assert
        assertTrue(mockCBPeripheral.verify(exactly = 1, functionName = "discoverCharacteristics"))
        testPeripheral.disconnect()
      }
    }

  @Test
  fun `discoverCBCharacteristics function returns BleResponse`() =
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
        val bleEvent = testPeripheral.discoverCBCharacteristics(MockCBService())

        // Assert
        assertTrue(bleEvent is OnCharacteristicsDiscovered) // TODO
        testPeripheral.disconnect()
      }
    }

  @Test
  fun `discoverCBDescriptors function calls discoverDescriptors in the CBPeripheral exactly once`() =
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
        val bleEvent = testPeripheral.discoverCBDescriptors(MockCBCharacteristic())

        // Assert
        assertTrue(mockCBPeripheral.verify(exactly = 1, functionName = "discoverDescriptorsForCharacteristic"))
        testPeripheral.disconnect()
      }
    }

  @Test
  fun `discoverCBDescriptors function returns BleResponse`() =
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
        val bleEvent = testPeripheral.discoverCBDescriptors(MockCBCharacteristic())

        // Assert
        assertTrue(bleEvent is OnDescriptorsDiscovered) // TODO
        testPeripheral.disconnect()
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
        testPeripheral.readCharacteristic(DEFAULT_UUID) { bleEvent ->
          // Assert
          assertTrue(mockCBPeripheral.verify(exactly = 1, functionName = "readValueForCharacteristic"))
        }
        testPeripheral.disconnect()
      }
    }

  @Test
  fun `readCharacteristic function returns CharacteristicUpdated BleResponse`() =
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
        testPeripheral.readCharacteristic(DEFAULT_UUID) { bleEvent ->
          // Assert
          assertTrue(bleEvent is BleResponse.CharacteristicRead)
        }
        testPeripheral.disconnect()
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
        testPeripheral.readDescriptor(DEFAULT_UUID, DEFAULT_UUID) { bleEvent ->
          // Assert
          assertTrue(mockCBPeripheral.verify(exactly = 1, functionName = "readValueForDescriptor"))
        }
        testPeripheral.disconnect()
      }
    }

  @Test
  fun `readDescriptor function returns DescriptorUpdated BleResponse`() =
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
        testPeripheral.readDescriptor(DEFAULT_UUID, DEFAULT_UUID) { bleEvent ->
          // Assert
          assertTrue(bleEvent is BleResponse.DescriptorRead)
        }
        testPeripheral.disconnect()
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
        testPeripheral.writeCharacteristic(uuid = DEFAULT_UUID, data = mockData) { bleEvent ->
          // Assert
          assertTrue(mockCBPeripheral.verify(exactly = 1, functionName = "writeValueForCharacteristic"))
        }
        testPeripheral.disconnect()
      }
    }

  @Test
  fun `writeCharacteristic function returns CharacteristicWrite BleResponse`() =
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
        testPeripheral.writeCharacteristic(uuid = DEFAULT_UUID, data = mockData) { bleEvent ->
          // Assert
          assertTrue(bleEvent is BleResponse.CharacteristicWrite)
        }
        testPeripheral.disconnect()
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
          ) { bleEvent ->
            // Assert
            assertTrue(mockCBPeripheral.verify(exactly = 1, functionName = "writeValueForDescriptor"))
          }
          testPeripheral.disconnect()
        }.join()
      }
    }

  @Test
  fun `writeDescriptor function returns DescriptorWrite BleResponse`() =
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
        testPeripheral.writeDescriptor(
          characteristicUUID = DEFAULT_UUID,
          descriptorUUID = DEFAULT_UUID,
          data = mockData,
        ) { bleEvent ->
          // Assert
          assertTrue(bleEvent is BleResponse.DescriptorWrite)
        }
        testPeripheral.disconnect()
      }
    }
}
