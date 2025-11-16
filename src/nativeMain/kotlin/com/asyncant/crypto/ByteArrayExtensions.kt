@file:OptIn(ExperimentalNativeApi::class)

package com.asyncant.crypto

import kotlin.experimental.ExperimentalNativeApi

actual inline fun ByteArray.copyToIntArray(sourceOffset: Int, count: Int, target: IntArray) {
  for (i in 0 until count) {
    val value = this.getIntAt(sourceOffset + i * 4)
    target[i] = (value and 0x000000FF shl 24) or
      (value and 0x0000ff00 shl 8) or
      (value and 0x00ff0000 ushr 8) or
      (value and 0xff000000.toInt() ushr 24)
  }
}
