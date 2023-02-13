package com.example.googlebook.viewmodel

import android.view.View
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

inline fun <T> conflated(block: Channel<T>.() -> Unit): Flow<T> =
  Channel<T>(Channel.CONFLATED).apply(block).consumeAsFlow()

fun View.clicks(): Flow<Unit> = conflated {
  setOnClickListener { offer(Unit) }
}

operator fun CoroutineContext.invoke(block: suspend CoroutineScope.() -> Unit): Job =
  GlobalScope.launch(this, block = block)

fun <T> Flow<T>.consumeOnEach(
  context: CoroutineContext = Dispatchers.Default, block: (T) -> Unit
): Job = (Dispatchers.IO) { onEach { context { block(it) } }.collect() }

fun <T> Flow<T>.sendTo(context: CoroutineContext, channel: () -> SendChannel<T>?): Job =
  consumeOnEach(context) { channel()?.offer(it) }
