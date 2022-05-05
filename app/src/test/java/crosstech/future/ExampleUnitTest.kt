package crosstech.future

import crosstech.future.logics.enums.Urgency
import crosstech.future.logics.models.Task
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromHexString
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.encodeToHexString
import kotlinx.serialization.protobuf.ProtoBuf
import org.junit.Test

import org.junit.Assert.*
import java.time.LocalDateTime

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest
{
    @Test
    fun tasksListSerialization()
    {
        val tasks: MutableList<Task> = mutableListOf()
        val task = Task(
            "Dummy task",
            "lorem ipsum of the day",
            LocalDateTime.now(),
            Urgency.Normal,
            5
        )
        val task2 = Task(
            "Dummy Scheduled Task",
            "Lorem ipsum of another day",
            LocalDateTime.now(),
            Urgency.Urgent,
            9
        ).schedule(
            LocalDateTime.now().plusMinutes(1),
            LocalDateTime.now().plusDays(1),
        )
        tasks.add(task)
        tasks.add(task2)
        val seri = ProtoBuf.encodeToHexString(tasks)
        val size = seri.length / 2
        println(seri)
        println(size)
        val deseri = ProtoBuf.decodeFromHexString<List<Task>>(seri)
        assert(deseri.count() == tasks.count())
    }

    @Test
    fun taskSerialization()
    {
        val task = Task(
            "Dummy task",
            "lorem ipsum of the day",
            LocalDateTime.now(),
            Urgency.Normal,
            5
        )
        val serialized = ProtoBuf.encodeToHexString(task)
        val size = ProtoBuf.encodeToByteArray(task).size
        println("Serialized to $size Bytes")
        println(serialized)
        val deserialized = ProtoBuf.decodeFromHexString<Task>(serialized)
        assert(task == deserialized)
    }
}