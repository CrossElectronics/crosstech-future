package crosstech.future.gui

import androidx.core.view.forEach
import com.google.android.material.bottomnavigation.BottomNavigationView
import crosstech.future.MainActivity
import crosstech.future.R
import java.lang.Exception

class Initializations
{
    companion object
    {
        fun popNavigationBar(view: BottomNavigationView)
        {
            view.updateIcon(view.selectedItemId)
            view.setOnItemSelectedListener {
                view.updateIcon(it.itemId)
                when (it.itemId)
                {
                    // TODO: Add activity switching to the placeholder 
                    R.id.dashboardTab ->
                    {

                    }
                    R.id.taskTab      ->
                    {

                    }
                    R.id.milestoneTab ->
                    {

                    }
                    R.id.statsTab     ->
                    {

                    }
                }
                true
            }

        }

        private fun BottomNavigationView.updateIcon(itemId: Int)
        {
            this.menu.forEach {
                it.setIcon(
                    when (it.itemId)
                    {
                        R.id.taskTab      -> if (itemId == R.id.taskTab) R.drawable.tasks_filled else R.drawable.tasks
                        R.id.dashboardTab -> if (itemId == R.id.dashboardTab) R.drawable.dashboard_filled else R.drawable.dashboard
                        R.id.statsTab     -> if (itemId == R.id.statsTab) R.drawable.stats_filled else R.drawable.stats
                        R.id.milestoneTab -> if (itemId == R.id.milestoneTab) R.drawable.milestone_filled else R.drawable.milestone
                        else              -> throw Exception("Illegal operation")
                    }
                )
            }
        }
    }
}