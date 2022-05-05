package crosstech.future.logics

import java.security.MessageDigest

class Utils
{
    companion object
    {
        private fun ByteArray.toHex(): String =
            joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }

        fun String.computeSHA1(): String
        {
            val digest = MessageDigest.getInstance("SHA1")
            return digest.digest(this.toByteArray()).toHex()
        }
    }
}