package com.cds.iot.core.util

import java.security.MessageDigest

object Md5 {
    fun hash(input: String): String {
        val digest = MessageDigest.getInstance("MD5").digest(input.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }

    fun doubleHash(input: String): String = hash(hash(input))
}
