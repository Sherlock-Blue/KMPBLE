package com.sherlockblue.kmpble.ble.commandQueue

import com.sherlockblue.kmpble.ble.commandQueue.commands.BleCommand
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.ConcurrentLinkedQueue

class CommandQueueTest {
  // Test queue management

  @Test
  fun `Initialized CommandQueue default queue contains no BleCommands`() {
    // Arrange
    val commandQueue = CommandQueue()

    // Assert
    assert(commandQueue.peek() == null)
  }

  @Test
  fun `Calling enqueue calls the add function with the correct BleCommand instance`() {
    // Arrange
    // Mocked Fixtures
    val bleCommandQueue: ConcurrentLinkedQueue<BleCommand> =
      mockk(relaxed = true) // "relaxed = true" prevents the mock add(), size, and peek() from failing
    val mockBleCommand: BleCommand =
      mockk(relaxed = true) // "relaxed = true" prevents the mock execute() from failing

    // Prepare object under test
    val commandQueue = CommandQueue(bleCommandQueue = bleCommandQueue)

    // Execute the test
    commandQueue.enqueue(mockBleCommand)

    // Assert
    verify(exactly = 1) { bleCommandQueue.add(mockBleCommand) }
  }

  @Test
  fun `Calling enqueue on an empty queue calls the peek function`() {
    // Arrange
    // Mocked Fixtures
    val mockBleCommand: BleCommand =
      mockk(relaxed = true) // "relaxed = true" prevents the mock execute() from failing
    val bleCommandQueue: ConcurrentLinkedQueue<BleCommand> =
      mockk<ConcurrentLinkedQueue<BleCommand>>(relaxed = true).apply {
        every { size } returns 1
        every { peek() } returns mockBleCommand
      } // "relaxed = true" prevents the mock add() from failing

    // Prepare object under test
    val commandQueue = CommandQueue(bleCommandQueue = bleCommandQueue)

    // Execute the test
    commandQueue.enqueue(mockBleCommand)

    // Assert
    verify(exactly = 1) { bleCommandQueue.peek() }
  }

  // enqueue

  @Test
  fun `Calling enqueue adds the correct BleCommand instance to the Command Queue`() {
    // Arrange
    // Mocked Fixtures
    val mockBleCommand: BleCommand =
      mockk(relaxed = true) // "relaxed = true" prevents the mock execute() from failing

    // Prepare object under test
    val commandQueue = CommandQueue()

    // Execute the test
    commandQueue.enqueue(mockBleCommand)

    // Assert
    assert(commandQueue.peek() === mockBleCommand) // same instance
  }

  @Test
  fun `Calling enqueue on empty queue executes queued command`() {
    // Arrange
    // Mocked Fixtures
    val mockBleCommand: BleCommand =
      mockk(relaxed = true) // "relaxed = true" prevents the mock execute() from failing

    // Prepare object under test
    val commandQueue = CommandQueue()

    // Execute the test
    commandQueue.enqueue(mockBleCommand)

    // Assert
    verify(exactly = 1) { mockBleCommand.execute() }
  }

  @Test
  fun `Queueing multiple commands only executes the first command`() {
    // Arrange
    // Mocked Fixtures
    val mockBleCommand1: BleCommand =
      mockk(relaxed = true) // "relaxed = true" prevents the mock execute() from failing
    val mockBleCommand2: BleCommand =
      mockk(relaxed = true) // "relaxed = true" prevents the mock execute() from failing

    // Prepare object under test
    val commandQueue = CommandQueue()

    // Execute the test
    commandQueue.enqueue(mockBleCommand1)
    commandQueue.enqueue(mockBleCommand2)

    verify(exactly = 1) { mockBleCommand1.execute() }
    verify(exactly = 0) { mockBleCommand2.execute() }
  }

  @Test
  fun `multiple queued commands are executed in correct order`() {
    // Arrange
    // Mocked Fixtures
    val mockBleCommand1: BleCommand =
      mockk(relaxed = true) // "relaxed = true" prevents the mock execute() from failing
    val mockBleCommand2: BleCommand =
      mockk(relaxed = true) // "relaxed = true" prevents the mock execute() from failing

    // Prepare object under test
    val commandQueue = CommandQueue()

    // Execute the test
    commandQueue.enqueue(mockBleCommand1)
    commandQueue.enqueue(mockBleCommand2)
    commandQueue.completeBleOperation()

    // Assert
    verifyOrder {
      mockBleCommand1.execute()
      mockBleCommand2.execute()
    }
    verify(exactly = 1) { mockBleCommand1.execute() }
    verify(exactly = 1) { mockBleCommand2.execute() }
  }

  // completeOperation

  @Test
  fun `completeOperation function calls execute() on next queued command`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleCommand1: BleCommand =
        mockk(
          relaxed = true,
        ) // "relaxed = true" prevents the mock execute() and cleanup() from failing
      val mockBleCommand2: BleCommand =
        mockk(
          relaxed = true,
        ) // "relaxed = true" prevents the mock execute() from failing

      // Prepare object under test
      val commandQueue = CommandQueue()
      commandQueue.enqueue(mockBleCommand1)
      commandQueue.enqueue(mockBleCommand2)

      // Execute the test
      commandQueue.completeBleOperation()

      // Assert
      verify(exactly = 1) { mockBleCommand2.execute() }
    }

  @Test
  fun `completeOperation function removes correct BleCommand from the CommandQueue`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleCommand1: BleCommand =
        mockk(
          relaxed = true,
        ) // "relaxed = true" prevents the mock execute() from failing
      val mockBleCommand2: BleCommand =
        mockk(
          relaxed = true,
        ) // "relaxed = true" prevents the mock execute() from failing

      // Prepare object under test
      val commandQueue = CommandQueue()
      commandQueue.enqueue(mockBleCommand1)
      commandQueue.enqueue(mockBleCommand2)

      // Execute the test
      commandQueue.completeBleOperation()

      // Assert
      assert(commandQueue.peek() === mockBleCommand2) // same instance
    }

  @Test
  fun `completeBleOperation function on an empty queue makes no changes to the CommandQueue`() =
    runTest {
      // Arrange
      val commandQueue = CommandQueue()

      // Execute the test
      commandQueue.completeBleOperation()

      // Assert
      assert(commandQueue.peek() == null)
    }

  @Test
  fun `clear function calls cleanup() on queued command`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleCommand1: BleCommand =
        mockk(
          relaxed = true,
        ) // "relaxed = true" prevents the mock cleanup() from failing

      // Prepare object under test
      val commandQueue = CommandQueue()
      commandQueue.enqueue(mockBleCommand1)

      // Execute the test
      commandQueue.clear()

      // Assert
      verify(exactly = 1) { mockBleCommand1.cleanup() }
    }

  @Test
  fun `clear function calls cleanup() only on queued command`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleCommand1: BleCommand =
        mockk(
          relaxed = true,
        ) // "relaxed = true" prevents the mock cleanup() from failing
      val mockBleCommand2: BleCommand =
        mockk(
          relaxed = true,
        ) // "relaxed = true" prevents the mock cleanup() from failing

      // Prepare object under test
      val commandQueue = CommandQueue()
      commandQueue.enqueue(mockBleCommand1)
      commandQueue.enqueue(mockBleCommand2)

      // Execute the test
      commandQueue.clear()

      // Assert
      verify(exactly = 0) { mockBleCommand2.cleanup() }
    }

  @Test
  fun `clear function calls does not call execute() on queued command`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleCommand1: BleCommand =
        mockk(
          relaxed = true,
        ) // "relaxed = true" prevents the mock execute(), cleanup() from failing
      val mockBleCommand2: BleCommand =
        mockk(
          relaxed = true,
        ) // "relaxed = true" prevents the mock execute() from failing

      // Prepare object under test
      val commandQueue = CommandQueue()
      commandQueue.enqueue(mockBleCommand1)
      commandQueue.enqueue(mockBleCommand2)

      // Execute the test
      commandQueue.clear()

      // Assert
      verify(exactly = 0) { mockBleCommand2.execute() }
    }

  @Test
  fun `clear function leaves CommandQueue with no BleCommands`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleCommand: BleCommand =
        mockk(
          relaxed = true,
        ) // "relaxed = true" prevents the mock execute() from failing

      // Prepare object under test
      val commandQueue = CommandQueue()
      commandQueue.enqueue(mockBleCommand)

      // Execute the test
      commandQueue.clear()

      // Assert
      assert(commandQueue.peek() == null)
    }

  @Test
  fun `clear function does not execute cleanUp() with no BleCommands`() =
    runTest {
      // Arrange
      // Mocked Fixtures
      val mockBleCommand: BleCommand =
        mockk(
          relaxed = true,
        ) // "relaxed = true" prevents the mock execute() from failing

      // Prepare object under test
      val commandQueue = CommandQueue()
      commandQueue.enqueue(mockBleCommand)
      commandQueue.completeBleOperation()

      // Execute the test
      commandQueue.clear()

      // Assert
      verify(exactly = 0) { mockBleCommand.cleanup() }
    }

  // peak()

  @Test
  fun `Calling peek() on an empty queue returns null`() {
    // Arrange
    val commandQueue = CommandQueue()

    // Execute the test
    val peekResult = commandQueue.peek()

    // Assert
    Assert.assertEquals(null, peekResult)
  }

  @Test
  fun `Calling peek() delegates to the queue`() {
    // Arrange
    // Mocked Fixtures
    val bleCommandQueue: ConcurrentLinkedQueue<BleCommand> =
      mockk<ConcurrentLinkedQueue<BleCommand>>().apply {
        every { peek() } returns null
      }

    // Prepare object under test
    val commandQueue = CommandQueue(bleCommandQueue = bleCommandQueue)

    // Execute the test
    commandQueue.peek()

    // Assert
    verify(exactly = 1) { bleCommandQueue.peek() }
  }

  @Test
  fun `Calling peek() returns correct BleCommand instance`() {
    // Arrange
    // Mocked Fixtures
    val mockBleCommand: BleCommand =
      mockk(relaxed = true) // "relaxed = true" prevents the mock execute() from failing

    val bleCommandQueue: ConcurrentLinkedQueue<BleCommand> =
      ConcurrentLinkedQueue<BleCommand>().apply {
        add(mockBleCommand)
      }

    // Prepare object under test
    val commandQueue = CommandQueue(bleCommandQueue = bleCommandQueue)

    // Execute the test
    val peekResult = commandQueue.peek()

    // Assert
    Assert.assertTrue(peekResult === mockBleCommand)
  }
}
