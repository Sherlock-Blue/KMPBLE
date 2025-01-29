package com.sherlockblue.kmpble.ble.fixtures

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class MockSharedFlow<T>(private val events: List<T>) : SharedFlow<T> {
  override val replayCache: List<T>
    get() = TODO("Not yet implemented")

  override suspend fun collect(collector: FlowCollector<T>): Nothing {
    events.forEach { event ->
      collector.emit(event)
    }
    return MutableSharedFlow<T>().collect(collector = collector)
  }
}
