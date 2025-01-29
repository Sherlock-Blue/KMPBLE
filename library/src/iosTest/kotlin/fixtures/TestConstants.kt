package com.sherlockblue.kmpble.fixtures

import platform.Foundation.NSNumber

// Base Bluetooth Low Energy UUID as defined by the SIG
const val DEFAULT_UUID = "00000000-0000-1000-8000-00805F9B34FB"
val TEST_RSSI: NSNumber = NSNumber(Int.MIN_VALUE)

// Some race conditions, particularly those caused by multiple CBPeripheral instances,
// occur sufficiently infrequently that it may taken hundreds of iterations to recreate them.
// The DEFAULT_TEST_RUNS is intended to facilitate large scale testing.
const val DEFAULT_TEST_RUNS = 1
