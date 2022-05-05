package crosstech.future

import android.app.Application
import crosstech.future.gui.Initializations
import crosstech.future.logics.models.Task

class Global : Application()
{
    var tasks: MutableList<Task> = mutableListOf()
}