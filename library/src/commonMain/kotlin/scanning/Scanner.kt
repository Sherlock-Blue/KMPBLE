package com.sherlockblue.kmpble.scanning

import kotlinx.coroutines.flow.Flow

expect class Scanner {
  fun scanResults(): Flow<ScannedResult>

  fun start()

  fun stop()
}
