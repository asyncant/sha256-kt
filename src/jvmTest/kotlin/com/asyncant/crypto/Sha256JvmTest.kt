package com.asyncant.crypto

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import java.security.MessageDigest
import kotlin.random.Random

internal class Sha256JvmTest {
  private val javaMessageDigest = MessageDigest.getInstance("SHA-256")

  @Test
  fun shortTexts() {
    for (i in 0 until 100) {
      val input = Random.Default.nextBytes(7)
      assertArrayEquals(javaHash(input), sha256(input))
    }
  }

  @Test
  fun mediumTexts() {
    for (i in 0 until 100) {
      val input = Random.Default.nextBytes(57)
      assertArrayEquals(javaHash(input), sha256(input))
    }
  }

  @Test
  fun longTexts() {
    for (i in 0 until 100) {
      val input = Random.Default.nextBytes(256 * 1024)
      assertArrayEquals(javaHash(input), sha256(input))
    }
  }

  private fun javaHash(value: ByteArray): ByteArray {
    return javaMessageDigest.digest(value)
  }
}
