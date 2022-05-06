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
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest
{
    @Test
    fun taskListCreation1()
    {
        val tasks = mutableListOf<Task>()
        val task_PN = Task(
            "Planned normal",
            "Normal",
            LocalDateTime.parse("2022-05-06T09:59"),
            Urgency.Normal,
            false,
            5
        )
        val task_PU = Task(
            "Planned urgent",
            "Normal urgent",
            LocalDateTime.parse("2022-05-06T10:01"),
            Urgency.Urgent,
            isImportant = false,
            5
        )
        val task_PI = Task(
            "Planned important",
            "Normal important",
            LocalDateTime.parse("2022-05-06T10:02"),
            Urgency.Normal,
            isImportant = true,
            5
        )
        val task_PUI = Task(
            "Planned urgent important",
            "Normal urgent important",
            LocalDateTime.parse("2022-05-06T10:04"),
            Urgency.Urgent,
            isImportant = true,
            5
        )
        val task_SN = task_PN.copy().schedule(
            LocalDateTime.parse("2022-05-06T10:08")
        )
        task_SN.name = "Scheduled normal"
        val task_SU = task_PU.copy().schedule(
            LocalDateTime.parse("2022-05-06T10:09")
        )
        task_SU.name = "Scheduled urgent"
        val task_SI = task_PI.copy().schedule(
            LocalDateTime.parse("2022-05-06T10:09")
        )
        task_SI.name = "Scheduled important"
        val task_SUI = task_PUI.copy().schedule(
            LocalDateTime.parse("2022-05-06T10:10")
        )
        task_SUI.name = "Scheduled urgent important"
        val task_SND = task_PN.copy().schedule(
            LocalDateTime.parse("2022-05-06T10:08"),
            LocalDateTime.parse("2022-05-07T10:00")
        )
        task_SND.name = "Scheduled normal Appr. Ddl."
        val task_SUD = task_PU.copy().schedule(
            LocalDateTime.parse("2022-05-06T10:09"),
            LocalDateTime.parse("2022-05-07T10:00")
        )
        task_SUD.name = "Scheduled urgent Appr. Ddl."
        val task_SID = task_PI.copy().schedule(
            LocalDateTime.parse("2022-05-06T10:09"),
            LocalDateTime.parse("2022-05-07T10:00")
        )
        task_SID.name = "Scheduled important Appr. Ddl."
        val task_SUID = task_PUI.copy().schedule(
            LocalDateTime.parse("2022-05-06T10:10"),
            LocalDateTime.parse("2022-05-07T10:00")
        )
        task_SUID.name = "Scheduled U/I Appr. Ddl."
        val task_SNd = task_PN.copy().schedule(
            LocalDateTime.parse("2022-05-06T10:08"),
            LocalDateTime.parse("2022-05-07T10:00")
        )
        task_SNd.name = "Scheduled normal Ddl."
        val task_SUd = task_PU.copy().schedule(
            LocalDateTime.parse("2022-05-06T10:09"),
            LocalDateTime.parse("2022-05-09T10:00")
        )
        task_SUd.name = "Scheduled urgent Ddl."
        val task_SId = task_PI.copy().schedule(
            LocalDateTime.parse("2022-05-06T10:09"),
            LocalDateTime.parse("2022-05-09T10:00")
        )
        task_SId.name = "Scheduled important Ddl."
        val task_SUId = task_PUI.copy().schedule(
            LocalDateTime.parse("2022-05-06T10:10"),
            LocalDateTime.parse("2022-05-09T10:00")
        )
        task_SUId.name = "Scheduled U/I Ddl."
        tasks.addAll(
            listOf(
                task_PN, task_PI, task_PU, task_PUI, task_SN, task_SND, task_SNd,
                task_SI, task_SId, task_SID, task_SUI, task_SUID, task_SUId, task_SU,
                task_SUD, task_SUd
            )
        )
        val serialized = ProtoBuf.encodeToByteArray(tasks)
        val string = Base64.getEncoder().encodeToString(serialized)
        println(string)
    }

    @Test
    fun tasksListSerialization()
    {
        val tasks: MutableList<Task> = mutableListOf()
        val task = Task(
            "Dummy task",
            "lorem ipsum of the day",
            LocalDateTime.now(),
            Urgency.Normal,
            false,
            5
        )
        val task2 = Task(
            "Dummy Scheduled Task",
            "Lorem ipsum of another day",
            LocalDateTime.now(),
            Urgency.Urgent,
            false,
            9
        ).schedule(
            LocalDateTime.now().plusMinutes(1),
            LocalDateTime.now().plusDays(1)
        )
        val task3 = Task(
            "An Important Task",
            "Needs to address soon",
            LocalDateTime.now().plusMinutes(1),
            Urgency.Urgent,
            true,
            9
        ).schedule(
            LocalDateTime.now().plusMinutes(1),
            LocalDateTime.now().plusDays(1)
        )
        tasks.add(task)
        tasks.add(task2)
        tasks.add(task3)
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
            false,
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