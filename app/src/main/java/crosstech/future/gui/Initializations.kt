package crosstech.future.gui

import android.content.Context
import android.util.Base64
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.view.forEach
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import crosstech.future.R
import crosstech.future.logics.models.Task
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.decodeFromByteArray
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.protobuf.ProtoBuf
import java.io.File
import java.io.FileInputStream
import java.lang.Exception
import kotlin.math.log

class Initializations
{
    companion object
    {
        fun setupNavController(controller: NavController, navbar: BottomNavigationView)
        {
            navbar.updateIcon(navbar.selectedItemId)
            controller.addOnDestinationChangedListener { _, dest, _ ->
                navbar.updateIcon(dest.id)
                when (dest.id)
                {
                    // TODO: Add fifth fragment (Commits)
                    // Check if having five tabs in the bottom nav bar complies with material design code
                    // refactor if necessary
                    R.id.dashboardFragment ->
                    {
                    }
                    R.id.tasksFragment     ->
                    {
                    }
                    R.id.milestoneFragment ->
                    {
                    }
                    R.id.statsFragment     ->
                    {
                    }
                }
            }
        }

        private fun BottomNavigationView.updateIcon(itemId: Int)
        {
            this.menu.forEach {
                it.setIcon(
                    when (it.itemId)
                    {
                        R.id.tasksFragment     -> if (itemId == R.id.tasksFragment) R.drawable.tasks_filled else R.drawable.tasks
                        R.id.dashboardFragment -> if (itemId == R.id.dashboardFragment) R.drawable.dashboard_filled else R.drawable.dashboard
                        R.id.statsFragment     -> if (itemId == R.id.statsFragment) R.drawable.stats_filled else R.drawable.stats
                        R.id.milestoneFragment -> if (itemId == R.id.milestoneFragment) R.drawable.milestone_filled else R.drawable.milestone
                        // TODO: Add two types of commit icon
                        else                   -> throw Exception("Illegal operation")
                    }
                )
            }
        }

        @OptIn(ExperimentalSerializationApi::class)
        inline fun <reified T> loadData(path: String, context: Context): T?
        {
            return try
            {
                val fin = context.openFileInput(path)
                val bytes = fin.readBytes()
                ProtoBuf.decodeFromByteArray(bytes)
            } catch (e: Exception)
            {
                null
            }
        }

        @OptIn(ExperimentalSerializationApi::class)
        inline fun <reified T> T.saveData(path: String, context: Context)
        {
            try
            {
                val fout = context.openFileOutput(path, 0)
                val bytes = ProtoBuf.encodeToByteArray(this)
                fout.write(bytes)
            } catch (e: Exception)
            {
                println(e.message)
                println(e.stackTrace)
            }
        }
    }
}