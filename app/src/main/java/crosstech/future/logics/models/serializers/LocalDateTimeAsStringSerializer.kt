package crosstech.future.logics.models.serializers

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.DateTimeException
import java.time.LocalDateTime
import java.util.*

object LocalDateTimeAsStringSerializer : KSerializer<LocalDateTime>
{
    // Preferably long: use Unix Timestamp
    override fun deserialize(decoder: Decoder): LocalDateTime
    {
        val string = decoder.decodeString()
        return LocalDateTime.parse(string)
    }

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime)
    {
        encoder.encodeString(value.toString())
    }
}