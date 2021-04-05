package com.asyncant.crypto

import com.asyncant.crypto.Sha256.sha256
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.security.MessageDigest
import kotlin.random.Random

@ExperimentalUnsignedTypes
internal class Sha256JvmTest {
  private val javaMessageDigest = MessageDigest.getInstance("SHA-256")

  @Test
  fun emptyString() {
    val emptyStringHash = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"
    assertEquals(emptyStringHash, sha256(byteArrayOf()).toHexString())
  }

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

private fun ByteArray.toHexString() = fold("") { str, it -> str + "%02x".format(it) }
