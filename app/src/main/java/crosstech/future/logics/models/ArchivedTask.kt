package crosstech.future.logics.models

import android.accounts.AuthenticatorDescription
import crosstech.future.logics.models.serializers.LocalDateTimeAsStringSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class ArchivedTask(
    val name: String,
    val description: String?,
    @Serializable(with = LocalDateTimeAsStringSerializer::class)
    val creationTime: LocalDateTime,
    @Serializable(with = LocalDateTimeAsStringSerializer::class)
    val completeTime: LocalDateTime,
    val hashSHA1: String
)
