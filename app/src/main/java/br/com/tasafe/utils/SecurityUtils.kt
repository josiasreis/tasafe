package br.com.tasafe.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec


/*Seguranca do app
*
* 1 - Definicao do manifest para aceitar apenas instalacao no dispositivo, bloqueando por exemplo instalacoes no cartao de memoria e expondo os dados.
* 2 - allowBackup=false para nao permitir backup dos dados.
* 3 - Proteção com criptografia AES, criamos uma chave de 256 bits baseado em senha PBKDF2
*   * para criar a chave é utilizado a senha e dados aleatorios varias vezes criando uma senha forte e exclusiva.
* */

class SecurityUtils {

    companion object {

        fun getEncryptedSharedPreferences(context: Context): SharedPreferences? {
            val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
            return EncryptedSharedPreferences.create(
                "br.com.cedro.tasafe.prefs",
                masterKeyAlias,
                context,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }

        fun encrypt2(senha: String, privateKey: String): String {
            val skeySpec = SecretKeySpec(privateKey.toByteArray(), "AES")
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec)
            val encrypted = cipher.doFinal(senha.toByteArray())
            return Base64.encodeToString(encrypted, Base64.NO_WRAP) //retorna String
        }

        fun decrypt2(senha: String, privateKey: String): String {
            val skeySpec = SecretKeySpec(privateKey.toByteArray(), "AES")
            val cipher = Cipher.getInstance("AES")
            cipher.init(Cipher.DECRYPT_MODE, skeySpec)
            val encrypted = cipher.doFinal(senha.toByteArray())
            return Base64.encodeToString(encrypted, Base64.NO_WRAP) //retorna String
        }


        fun encrypt(dataToEncrypt: ByteArray,
                    password: CharArray): java.util.HashMap<String, ByteArray> {
            val map = java.util.HashMap<String, ByteArray>()

            try {
                // 1
                //Random salt for next step
                val random = SecureRandom()
                val salt = ByteArray(256)
                random.nextBytes(salt)

                // 2
                //PBKDF2 - derive the key from the password, don't use passwords directly
                val pbKeySpec = PBEKeySpec(password, salt, 1324, 256)
                val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
                val keyBytes = secretKeyFactory.generateSecret(pbKeySpec).encoded
                val keySpec = SecretKeySpec(keyBytes, "AES")

                // 3
                //Create initialization vector for AES
                val ivRandom = SecureRandom() //not caching previous seeded instance of SecureRandom
                val iv = ByteArray(16)
                ivRandom.nextBytes(iv)
                val ivSpec = IvParameterSpec(iv)

                // 4
                //Encrypt
                val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
                cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
                val encrypted = cipher.doFinal(dataToEncrypt)

                // 5
                map["salt"] = salt
                map["iv"] = iv
                map["encrypted"] = encrypted
            } catch (e: Exception) {
                Log.e("MYAPP", "encryption exception", e)
            }

            return map

        }

        fun decrypt(map: java.util.HashMap<String, ByteArray>, password: CharArray): ByteArray? {
            var decrypted: ByteArray? = null
            try {
                // 1
                val salt = map["salt"]
                val iv = map["iv"]
                val encrypted = map["encrypted"]

                // 2
                //regenerate key from password
                val pbKeySpec = PBEKeySpec(password, salt, 1324, 256)
                val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
                val keyBytes = secretKeyFactory.generateSecret(pbKeySpec).encoded
                val keySpec = SecretKeySpec(keyBytes, "AES")

                // 3
                //Decrypt
                val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
                val ivSpec = IvParameterSpec(iv)
                cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
                decrypted = cipher.doFinal(encrypted)
            } catch (e: Exception) {
                Log.e("MYAPP", "decryption exception", e)
            }

            return decrypted
        }

    }



}