package com.asyncant.crypto

import kotlin.test.Test
import kotlin.test.assertEquals

class Sha256Test {
  /** Origin: https://www.di-mgt.com.au/sha_testvectors.html */
  @Test
  fun testVectors() {
    val emptyStringHash = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"
    assertEquals(emptyStringHash, sha256(byteArrayOf()).toHexString())

    val abcHash = "ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad"
    assertEquals(abcHash, sha256("abc".encodeToByteArray()).toHexString())

    val abc448BitsHash = "248d6a61d20638b8e5c026930c3e6039a33ce45964ff2167f6ecedd419db06c1"
    assertEquals(
      abc448BitsHash,
      sha256("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq".encodeToByteArray()).toHexString()
    )

    val abc896BitsHash = "cf5b16a778af8380036ce59e7b0492370b249b11e8f07a51afac45037afee9d1"
    val abc896Bits = "abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmnhijklmnoijklmnopjklmnopqklmnopqrlmnopqr" +
      "smnopqrstnopqrstu"
    assertEquals(
      abc896BitsHash,
      sha256(abc896Bits.encodeToByteArray()).toHexString()
    )

    val aOneMillionHash = "cdc76e5c9914fb9281a1c7e284d73e67f1809a48a497200e046d39ccc7112cd0"
    assertEquals(
      aOneMillionHash,
      sha256("a".repeat(1000000).encodeToByteArray()).toHexString()
    )
  }
}

internal fun ByteArray.toHexString() = fold("") { str, it -> str + (0xFF and it.toInt()).toString(16).padStart(2, '0') }

