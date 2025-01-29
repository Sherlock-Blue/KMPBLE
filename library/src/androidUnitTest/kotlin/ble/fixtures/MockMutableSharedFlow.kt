package com.sherlockblue.kmpble.ble.fixtures

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.StateFlow

class MockMutableSharedFlow<T>(private val events: List<T>, override val subscriptionCount: StateFlow<Int>) : MutableSharedFlow<T> {
  override val replayCache: List<T>
    get() = listOf() // relaxed response

  @ExperimentalCoroutinesApi
  override fun resetReplayCache() {
    // noop
  }

  // relaxed response
  override fun tryEmit(value: T): Boolean = true

  override suspend fun emit(value: T) {
    // noop
  }

  override suspend fun collect(collector: FlowCollector<T>): Nothing {
    events.forEach { event ->
      collector.emit(event)
    }
    return MutableSharedFlow<T>().collect(collector = collector)
  }
}
