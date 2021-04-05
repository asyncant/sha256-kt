package com.asyncant.crypto

@Suppress("NOTHING_TO_INLINE")
actual inline fun ByteArray.copyToIntArray(sourceOffset: Int, count: Int, target: IntArray) {
  for (i in 0 until count) {
    val j = sourceOffset + i * 4
    target[i] = (this[j].toInt() and 0xFF shl 24) or
      (this[j + 1].toInt() and 0xFF shl 16) or
      (this[j + 2].toInt() and 0xFF shl 8) or
      (this[j + 3].toInt() and 0xFF)
  }
}
