package crosstech.future.gui

import android.util.Log
import android.view.ContextThemeWrapper
import androidx.core.view.forEach
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import crosstech.future.R
import crosstech.future.logics.models.Task
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromHexString
import kotlinx.serialization.protobuf.ProtoBuf
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
                        else                   -> throw Exception("Illegal operation")
                    }
                )
            }
        }

        @OptIn(ExperimentalSerializationApi::class)
        inline fun <reified T> loadData(): T
        {
            // TODO: Actually load from disk
            val dummy =
                "02470a0a44756d6d79207461736b12166c6f72656d20697073756d206f66207468652064617" +
                        "91a1d323032322d30352d30355431343a34333a30392e3237373133373730302001280595010a14" +
                        "44756d6d79205363686564756c6564205461736b121a4c6f72656d20697073756d206f6620616e6" +
                        "f74686572206461791a1d323032322d30352d30355431343a34333a30392e323738313334393030" +
                        "200228093001421d323032322d30352d30355431343a34343a30392e323738313334393030521d3" +
                        "23032322d30352d30365431343a34333a30392e323738313334393030"
            return ProtoBuf.decodeFromHexString<T>(dummy)
        }
    }
}