/* *******************************************************************************************************************
 * SHA-256 hash algorithm implementation.
 *
 * Original C implementation by Brad Conte (brad@bradconte.com), https://github.com/B-Con/crypto-algorithms.
 * Ported to Kotlin and modified by asyncant.
 *
 * Like the original C implementation, this code is released into the public domain.
 *
 * ***************************************************************************************************************** */
package com.asyncant.crypto

@ExperimentalUnsignedTypes
object Sha256 {
  fun sha256(text: ByteArray): ByteArray {
    val ctx = Sha256Ctx()
    sha256Update(ctx, text)
    return sha256Final(ctx)
  }

  private val k = uintArrayOf(
    0x428a2f98u, 0x71374491u, 0xb5c0fbcfu, 0xe9b5dba5u, 0x3956c25bu, 0x59f111f1u, 0x923f82a4u, 0xab1c5ed5u,
    0xd807aa98u, 0x12835b01u, 0x243185beu, 0x550c7dc3u, 0x72be5d74u, 0x80deb1feu, 0x9bdc06a7u, 0xc19bf174u,
    0xe49b69c1u, 0xefbe4786u, 0x0fc19dc6u, 0x240ca1ccu, 0x2de92c6fu, 0x4a7484aau, 0x5cb0a9dcu, 0x76f988dau,
    0x983e5152u, 0xa831c66du, 0xb00327c8u, 0xbf597fc7u, 0xc6e00bf3u, 0xd5a79147u, 0x06ca6351u, 0x14292967u,
    0x27b70a85u, 0x2e1b2138u, 0x4d2c6dfcu, 0x53380d13u, 0x650a7354u, 0x766a0abbu, 0x81c2c92eu, 0x92722c85u,
    0xa2bfe8a1u, 0xa81a664bu, 0xc24b8b70u, 0xc76c51a3u, 0xd192e819u, 0xd6990624u, 0xf40e3585u, 0x106aa070u,
    0x19a4c116u, 0x1e376c08u, 0x2748774cu, 0x34b0bcb5u, 0x391c0cb3u, 0x4ed8aa4au, 0x5b9cca4fu, 0x682e6ff3u,
    0x748f82eeu, 0x78a5636fu, 0x84c87814u, 0x8cc70208u, 0x90befffau, 0xa4506cebu, 0xbef9a3f7u, 0xc67178f2u
  )

  private class Sha256Ctx {
    var data: UIntArray = UIntArray(64)
    var state: UIntArray = UIntArray(8)
    var dataLength: Int = 0
    var bitLength: Long = 0

    init {
      state[0] = 0x6a09e667u
      state[1] = 0xbb67ae85u
      state[2] = 0x3c6ef372u
      state[3] = 0xa54ff53au
      state[4] = 0x510e527fu
      state[5] = 0x9b05688cu
      state[6] = 0x1f83d9abu
      state[7] = 0x5be0cd19u
    }
  }

  private fun sha256Transform(ctx: Sha256Ctx, data: UIntArray) {
    var a: UInt
    var b: UInt
    var c: UInt
    var d: UInt
    var e: UInt
    var f: UInt
    var g: UInt
    var h: UInt
    var t1: UInt
    var t2: UInt
    val m = UIntArray(64)

    for (i in 0 until 16) {
      val j = i * 4
      m[i] = (data[j] shl 24) or (data[j + 1] shl 16) or (data[j + 2] shl 8) or (data[j + 3])
    }
    for (i in 16 until 64) {
      m[i] = sig1(m[i - 2]) + m[i - 7] + sig0(m[i - 15]) + m[i - 16]
    }
    a = ctx.state[0]
    b = ctx.state[1]
    c = ctx.state[2]
    d = ctx.state[3]
    e = ctx.state[4]
    f = ctx.state[5]
    g = ctx.state[6]
    h = ctx.state[7]

    for (i in 0 until 64) {
      t1 = h + ep1(e) + ch(e, f, g) + k[i] + m[i]
      t2 = ep0(a) + maj(a, b, c)
      h = g
      g = f
      f = e
      e = d + t1
      d = c
      c = b
      b = a
      a = t1 + t2
    }

    ctx.state[0] += a
    ctx.state[1] += b
    ctx.state[2] += c
    ctx.state[3] += d
    ctx.state[4] += e
    ctx.state[5] += f
    ctx.state[6] += g
    ctx.state[7] += h
  }

  private fun sha256Update(ctx: Sha256Ctx, data: ByteArray) {
    for (byte in data.toUByteArray()) {
      ctx.data[ctx.dataLength] = byte.toUInt()
      ctx.dataLength++
      if (ctx.dataLength == 64) {
        sha256Transform(ctx, ctx.data)
        ctx.bitLength += 512
        ctx.dataLength = 0
      }
    }
  }

  private fun sha256Final(ctx: Sha256Ctx): ByteArray {
    // Pad whatever data is left in the buffer.
    if (ctx.dataLength < 56) {
      ctx.data[ctx.dataLength] = 0x80u
      for (i in ctx.dataLength + 1 until 56) ctx.data[i] = 0x00u
    } else {
      ctx.data[ctx.dataLength] = 0x80u
      for (i in ctx.dataLength + 1 until 64) ctx.data[i] = 0x00u
      sha256Transform(ctx, ctx.data)
      for (i in 0 until 56) ctx.data[i] = 0u
    }

    // Append to the padding the total message's length in bits and transform.
    ctx.bitLength += ctx.dataLength * 8
    ctx.data[63] = ctx.bitLength.toUInt()
    ctx.data[62] = (ctx.bitLength ushr 8).toUInt()
    ctx.data[61] = (ctx.bitLength ushr 16).toUInt()
    ctx.data[60] = (ctx.bitLength ushr 24).toUInt()
    ctx.data[59] = (ctx.bitLength ushr 32).toUInt()
    ctx.data[58] = (ctx.bitLength ushr 40).toUInt()
    ctx.data[57] = (ctx.bitLength ushr 48).toUInt()
    ctx.data[56] = (ctx.bitLength ushr 56).toUInt()
    sha256Transform(ctx, ctx.data)

    // Since this implementation uses little endian byte ordering and SHA uses big endian,
    // reverse all the bytes when copying the final state to the output hash.
    val hash = ByteArray(32)

    for (i in 0 until 4) {
      hash[i] = ((ctx.state[0] shr (24 - i * 8)) and 0x000000ffu).toByte()
      hash[i + 4] = ((ctx.state[1] shr (24 - i * 8)) and 0x000000ffu).toByte()
      hash[i + 8] = ((ctx.state[2] shr (24 - i * 8)) and 0x000000ffu).toByte()
      hash[i + 12] = ((ctx.state[3] shr (24 - i * 8)) and 0x000000ffu).toByte()
      hash[i + 16] = ((ctx.state[4] shr (24 - i * 8)) and 0x000000ffu).toByte()
      hash[i + 20] = ((ctx.state[5] shr (24 - i * 8)) and 0x000000ffu).toByte()
      hash[i + 24] = ((ctx.state[6] shr (24 - i * 8)) and 0x000000ffu).toByte()
      hash[i + 28] = ((ctx.state[7] shr (24 - i * 8)) and 0x000000ffu).toByte()
    }
    return hash
  }

  private fun maj(x: UInt, y: UInt, z: UInt) = (x and y) xor (x and z) xor (y and z)

  private fun ep0(x: UInt) = rotateRight(x, 2) xor rotateRight(x, 13) xor rotateRight(x, 22)

  private fun ch(x: UInt, y: UInt, z: UInt) = (x and y) xor (x.inv() and z)

  private fun ep1(x: UInt) = rotateRight(x, 6) xor rotateRight(x, 11) xor rotateRight(x, 25)

  private fun sig0(x: UInt) = rotateRight(x, 7) xor rotateRight(x, 18) xor (x shr 3)

  private fun sig1(x: UInt) = rotateRight(x, 17) xor rotateRight(x, 19) xor (x shr 10)

  private fun rotateRight(a: UInt, b: Int) = a shr b or (a shl (32 - b))
}
