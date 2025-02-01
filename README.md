![KMP BLE](./kmpblebanner.jpg)

## About
KMP BLE is Bluetooth Low Energy framework exposing both a high level API for multiplatform application developers and lower level native API's for IoT developers.<br>

## Features: 
#### &nbsp;&nbsp;Supports iOS and Android
#### &nbsp;&nbsp;Exposes both native and platform-agnostic API's
#### &nbsp;&nbsp;Implements BLE with both callback and coroutine API's.
#### &nbsp;&nbsp;SIG standards and BLE utilities
#### &nbsp;&nbsp;100% code coverage
#### &nbsp;&nbsp;Implements an innovative mocking solution for Core Bluetooth<br>

> Not a typo! This library implements unit testing mocks for CBCentralManager and CBPeripheral, once thought to be effectively impossible without requiring changes to the production code.
<br>

## Experimental Features: 
#### - &nbsp;&nbsp;Implementation as a React/Native package
#### - &nbsp;&nbsp;Implementation as a Flutter/Dart package<br><br>
## Architecture

### Android BLE Command Architecture

Bluetooth Low Energy commands are executed sequentially. So each command needs to wait for the prior command to complete
before executing. There are a number of ways to implement this behavior involving various combinations of
mutex's and flags. For the purposes of this library, a thread-safe queue will be used. The queue will contain individual
BLE commands implementing the Command Pattern. Each BleCommand object will add itself to the
Command Queue. When it reaches the top of the queue, the Command Queue will call an execute function which will perform
the intended BLE command. Once the command has completed, the BleCommand object will dequeue itself from the Command
Queue.

The architecture of the BLE command processing queue is inspired by the simplified Active Object example from page 155 of Bob Martin's book, "Agile Software Development: Principles, Patterns, and Practices".

#### UML

```java
+-----------------------------+
|    BleCommand (Sealed)      |
|  +-----------------------+  |-------+
|  |+ enqueue(): void      |  |       |
|  |*+ execute(): void*    |  |       |
|  |+ cleanup(): void      |  |       |
+-----------------------------+       |
^                                     |
|                                     |
+---------------------------+       +------------------------+
|   ConcreteCommandA        |       |   ConcreteCommandB      |
| +-----------------------+ |       | +--------------------+  |
| |+ execute(): void      | |       | |+ execute(): void   |  |
+---------------------------+       +------------------------+
^
|
+------------------------------+
|          Queue               |
| +---------------------------+|
| | bleCommandQueue:           |
| |   ConcurrentLinkedQueue    |
| | +enqueue(bleOperation:     |
| |   BleCommand)              |
| | +completeBleOperation()    |
| | +peek(): BleCommand        |
| | +doNext()                  |
+------------------------------+
  ```

#### GATT Callbacks
On Android, the sytem assumes exactly one BluetoothGattCallback object to receive all callback events coming from a peripheral. So every peripheral (or, to be specific, every peripheral *connection*) has one and only one BluetoothGattCallback object. So it ends up being responsible for handling many different types of callbacks, many of which have little to do with one another. Connection status changes, for example, go to the same place as characteristic notifications and mtu length changes. As a result, it ends up being something of a "God Class" which violates the Single Responsibility Principle at almost every level. And since it serves so many different purposes, the callbacks should ideally be made available to multiple uses including individual command callbacks, queue management, connection status monitoring, logging. So the challenge becomes exposing the appropriate callbacks to the relevant consumers.

The BluetoothGattCallback class also changes on a semi-regular basis, most recently with Tiramisu. So it is important to insulate the code against future changes.

I've tried a variety of solutions and have yet to find the "One Simple Trick". Each approach has advantages and disadvantages.

1) Expose each callback with an "old school" callback interface that forwards the parameters of that GattCallback.

- Pros: Easy to understand and implement.
- Cons: Many nearly identical callback interfaces tightly coupled to the BluetoothGattCallback class. Also need to keep track of observers.

2) Expose all callbacks with a single callback interface that passes a Result class.

- Pros: Decoupled from the BluetoothGattCallback.
- Cons: Confusing Result parameters. Also need to keep track of observers.

3) Expose all callbacks using a single observer list that implement the BluetoothGattCallback interface, itself.

- Pros: BluetoothGattCallback class is an abstract class with default implementations for every callback. So each observer only needs to override the callback it wants to observe. 
Very insulated against changes to BluetoothGattCallback class.
- Cons: Forwarding an interface to observers implementing the same interface is confusing and unintuitive. Too clever for its own good.

4) Expose events using an event bus.

- Pros: Highly scalable. Very simple implementation. Observers merely subscribe to events.
- Cons: The required event classes are very tightly coupled to the BluetoothGattCallback class.
SharedFlow requires coroutines which would not otherwise be required for BLE callbacks.<br>

Inspiration for the event bus came from these articles on Medium:
- [Mohit Rajput](https://medium.com/@mohitrajput987?source=post_page---byline--1fe0c6ca08c8--------------------------------) [*Event Bus Pattern in Android Using Kotlin Flows*](https://medium.com/%40mohitrajput987/event-bus-pattern-in-android-using-kotlin-flows-1fe0c6ca08c8)<br>
- [Ashish Suthar](https://asissuthar.medium.com/?source=post_page---byline--4b6fa8cb1a37--------------------------------): [*Simple Global Event Bus Using Kotlin SharedFlow*](https://asissuthar.medium.com/simple-global-event-bus-using-kotlin-sharedflow-and-koin-4b6fa8cb1a37)<br>

## Usage: Build, Test, Publish
### Build
#### Lint

Lint configuration is defined in .editorconfig file in the project root.

```java
./gradlew ktlintFormat
```

### Test
#### Testing Philosophy
The testing philosophy for this project can be stated as follows: Untested code doesn't work.
So this library will always have 100% unit test code coverage. That said, there currently is no way to obtain code coverage for iOS tests. 

This project implements tests at the interface level and at the coding level (i.e. implementation testing). Implementation tests are unpopular, but the tests need to prove that the code not only works, but works as designed. Or, in other words, in addition to proving that the code produces the correct result, the tests need to demonstrate that the code isn't producing the correct result by accident.<br>


#### Testing With Code Coverage

To run unit tests on the Android side, use this task:

```java
./gradlew ktlintCheck koverHTMLReport
```

Tests on the iOS side need to be run from the IDE.

### Publish

```java
./gradlew publishToMavenLocal
```

## Friends Of The Show

#### BLE on Github
[**KABLE**](https://github.com/JuulLabs/kable)<br>
[**Punch Through**](https://github.com/punchthrough)<br>
[**Nordic**](https://github.com/nordicsemiconductor)<br>

#### KMP on Github
[**Philipp Lackner**](https://github.com/philipplackner)<br>
[**Touchlab**](https://github.com/touchlab)<br>

#### Bluetooth Resources
[**SIG Official Website**](https://www.bluetooth.com/)<br>
<br>
<br>
