package crosstech.future

import android.app.Application
import crosstech.future.gui.Initializations
import crosstech.future.gui.Initializations.Companion.saveData
import crosstech.future.logics.models.ArchivedTask
import crosstech.future.logics.models.Task
import kotlin.properties.Delegates

class Global : Application()
{
    var tasks: MutableList<Task> = mutableListOf()
    var archive: MutableList<ArchivedTask> = mutableListOf()

    companion object
    {
        val TASKS_FILE = "tasks.data"
    }
}