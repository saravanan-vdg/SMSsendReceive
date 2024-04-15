package com.sai.mysms.utils

import android.util.Base64
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


class EncryptionUtils {

    val password = "sciflarete"
    val salt = "salt1234".toByteArray()
    private val AES_ALGORITHM = "AES"
    private lateinit var ENCRYPTION_KEY:SecretKeySpec;


    init {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), salt, 10000, 128) // 128-bit key
        val tmp = factory.generateSecret(spec)
        ENCRYPTION_KEY = SecretKeySpec(tmp.encoded, AES_ALGORITHM)
    }


    @Throws(Exception::class)
    fun encrypt(message: String): String {
        val cipher = Cipher.getInstance(AES_ALGORITHM)
        cipher.init(Cipher.ENCRYPT_MODE, ENCRYPTION_KEY)
        val encryptedBytes = cipher.doFinal(message.toByteArray())
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    @Throws(Exception::class)
    fun decrypt(encryptedMessage: String?): String {
        val cipher = Cipher.getInstance(AES_ALGORITHM)
        cipher.init(Cipher.DECRYPT_MODE, ENCRYPTION_KEY)
        val encryptedBytes: ByteArray = Base64.decode(encryptedMessage, Base64.DEFAULT)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes)
    }
}