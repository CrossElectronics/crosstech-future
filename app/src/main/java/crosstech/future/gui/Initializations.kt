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
                "03490a0a44756d6d79207461736b12166c6f72656d20697073756d206f6620746865206461" +
                        "791a1d323032322d30352d30355432313a35373a32332e39323937373933303020012800300590" +
                        "010a1444756d6d79205363686564756c6564205461736b121a4c6f72656d20697073756d206f66" +
                        "20616e6f74686572206461791a1a323032322d30352d30355432313a35373a32332e3933303737" +
                        "36200228003009380140014a1a323032322d30352d30355432313a35383a32332e393330373736" +
                        "5a1a323032322d30352d30365432313a35373a32332e39333037373688010a11416e20496d706f" +
                        "7274616e74205461736b12154e6565647320746f206164647265737320736f6f6e1a1a32303232" +
                        "2d30352d30355432313a35383a32332e393330373736200228013009380140014a1a323032322d" +
                        "30352d30355432313a35383a32332e3933303737365a1a323032322d30352d30365432313a3537" +
                        "3a32332e393330373736"
            return ProtoBuf.decodeFromHexString<T>(dummy)
        }
    }
}