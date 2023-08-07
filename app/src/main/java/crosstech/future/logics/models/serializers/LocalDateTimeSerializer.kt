package crosstech.future.logics.models.serializers

import crosstech.future.logics.Utils.Companion.toLocalDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.DateTimeException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

object LocalDateTimeSerializer : KSerializer<LocalDateTime>
{
    override fun deserialize(decoder: Decoder): LocalDateTime
    {
        val timestamp = decoder.decodeLong()
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneOffset.UTC)
            .toLocalDateTime()
    }

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalDateTime)
    {
        encoder.encodeLong(value.toInstant(ZoneOffset.UTC).toEpochMilli())
    }
}