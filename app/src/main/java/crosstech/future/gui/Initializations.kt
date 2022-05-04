package crosstech.future.gui

import android.util.Log
import androidx.core.view.forEach
import com.google.android.material.bottomnavigation.BottomNavigationView
import crosstech.future.R
import java.lang.Exception
import kotlin.math.log

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
                true
            }

        }

        private fun BottomNavigationView.updateIcon(itemId: Int)
        {
            Log.i("DEBUG", "ITEMID $itemId")
            this.menu.forEach {
                it.setIcon(
                    when (it.itemId)
                    {
                        R.id.tasksFragment     -> if (itemId == R.id.tasksFragment) R.drawable.tasks_filled else R.drawable.tasks
                        R.id.dashboardFragment -> if (itemId == R.id.dashboardFragment) R.drawable.dashboard_filled else R.drawable.dashboard
                        R.id.statsFragment     -> if (itemId == R.id.statsFragment) R.drawable.stats_filled else R.drawable.stats
                        R.id.milestoneFragment -> if (itemId == R.id.milestoneFragment) R.drawable.milestone_filled else R.drawable.milestone
                        else                   -> throw Exception("Illegal operation")
                    }
                )
            }
        }
    }
}