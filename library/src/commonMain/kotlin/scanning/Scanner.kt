package com.sherlockblue.kmpble.scanning

import kotlinx.coroutines.flow.Flow

expect class Scanner {
  fun scanResults(): Flow<ScannedResult>

  suspend fun start()

  suspend fun stop()
}
