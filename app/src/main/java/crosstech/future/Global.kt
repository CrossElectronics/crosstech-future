package crosstech.future

import android.app.Application
import android.content.Context
import crosstech.future.gui.Initializations
import crosstech.future.gui.Initializations.Companion.saveData
import crosstech.future.logics.models.ArchivedTask
import crosstech.future.logics.models.Milestone
import crosstech.future.logics.models.Task
import kotlin.properties.Delegates

class Global : Application()
{
    var tasks: MutableList<Task> = mutableListOf()
    var archive: MutableList<ArchivedTask> = mutableListOf()
    var milestones: MutableList<Milestone> = mutableListOf()
    lateinit var context: Context

    override fun onCreate()
    {
        context = applicationContext
        super.onCreate()
    }

    companion object
    {
        const val TASKS_FILE = "tasks.data"
        const val ARCHIVE_FILE = "archive.data"
        const val MILESTONES_FILE = "milestones.data"
    }
}