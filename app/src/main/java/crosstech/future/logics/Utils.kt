package crosstech.future.logics

import android.content.Context
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import java.io.File
import java.security.MessageDigest
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

class Utils
{
    companion object
    {
        val fullFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        val dateOnlyFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val timeOnlyFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        private fun ByteArray.toHex(): String =
            joinToString(separator = "") { eachByte -> "%02x".format(eachByte) }

        fun parseTime(d: EditText, t: EditText, s: Int = 0): LocalDateTime =
            LocalDateTime.parse(d.text.toString() + "T"
                                + t.text.toString() + ":"
                                + String.format("%02d", s))

        fun String.computeSHA1(): String
        {
            val digest = MessageDigest.getInstance("SHA1")
            return digest.digest(this.toByteArray()).toHex()
        }

        fun String.getSaveSize(context: Context): Long
        {
            val file = File(context.filesDir.absolutePath + "/" + this)
            return if (file.exists()) file.length() else 0
        }

        fun Long.toReadableSize(): String
        {
            return when
            {
                this > 1.2 * 1024 * 1024 -> "${String.format("%.2f", this / 1024.0 / 1024.0)} MB"
                this > 1.2 * 1024        -> "${String.format("%.2f", this / 1024.0)} KB"
                else                     -> "$this B"
            }
        }

        fun Long.toLocalDateTime(): LocalDateTime =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.systemDefault())

        fun getTotalHours(L: Long, R: Long): Double = (R - L) / 1000.0 / 60 / 60

    }
}