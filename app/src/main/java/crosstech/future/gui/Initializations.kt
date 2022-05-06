package crosstech.future.gui

import android.util.Base64
import android.util.Log
import android.view.ContextThemeWrapper
import androidx.core.view.forEach
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import crosstech.future.R
import crosstech.future.logics.models.Task
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromByteArray
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
        inline fun <reified T> loadData(): T?
        {
            // TODO: Actually load from disk
            try
            {
                val dummy =
                    "EDAKDlBsYW5uZWQgbm9ybWFsEgZOb3JtYWwaEDIwMjItMDUtMDZUMDk6NTkgASgAMAU9ChFQbGFu" +
                            "bmVkIGltcG9ydGFudBIQTm9ybWFsIGltcG9ydGFudBoQMjAyMi0wNS0wNlQxMDowMiAB" +
                            "KAEwBTcKDlBsYW5uZWQgdXJnZW50Eg1Ob3JtYWwgdXJnZW50GhAyMDIyLTA1LTA2VDEw" +
                            "OjAxIAIoADAFSwoYUGxhbm5lZCB1cmdlbnQgaW1wb3J0YW50EhdOb3JtYWwgdXJnZW50" +
                            "IGltcG9ydGFudBoQMjAyMi0wNS0wNlQxMDowNCACKAEwBUgKEFNjaGVkdWxlZCBub3Jt" +
                            "YWwSBk5vcm1hbBoQMjAyMi0wNS0wNlQwOTo1OSABKAAwBTgBQAFKEDIwMjItMDUtMDZU" +
                            "MTA6MDhlChtTY2hlZHVsZWQgbm9ybWFsIEFwcHIuIERkbC4SBk5vcm1hbBoQMjAyMi0w" +
                            "NS0wNlQwOTo1OSABKAAwBTgBQAFKEDIwMjItMDUtMDZUMTA6MDhaEDIwMjItMDUtMDdU" +
                            "MTA6MDBfChVTY2hlZHVsZWQgbm9ybWFsIERkbC4SBk5vcm1hbBoQMjAyMi0wNS0wNlQw" +
                            "OTo1OSABKAAwBTgBQAFKEDIwMjItMDUtMDZUMTA6MDhaEDIwMjItMDUtMDdUMTA6MDBV" +
                            "ChNTY2hlZHVsZWQgaW1wb3J0YW50EhBOb3JtYWwgaW1wb3J0YW50GhAyMDIyLTA1LTA2" +
                            "VDEwOjAyIAEoATAFOAFAAUoQMjAyMi0wNS0wNlQxMDowOWwKGFNjaGVkdWxlZCBpbXBv" +
                            "cnRhbnQgRGRsLhIQTm9ybWFsIGltcG9ydGFudBoQMjAyMi0wNS0wNlQxMDowMiABKAEw" +
                            "BTgBQAFKEDIwMjItMDUtMDZUMTA6MDlaEDIwMjItMDUtMDlUMTA6MDByCh5TY2hlZHVs" +
                            "ZWQgaW1wb3J0YW50IEFwcHIuIERkbC4SEE5vcm1hbCBpbXBvcnRhbnQaEDIwMjItMDUt" +
                            "MDZUMTA6MDIgASgBMAU4AUABShAyMDIyLTA1LTA2VDEwOjA5WhAyMDIyLTA1LTA3VDEw" +
                            "OjAwYwoaU2NoZWR1bGVkIHVyZ2VudCBpbXBvcnRhbnQSF05vcm1hbCB1cmdlbnQgaW1w" +
                            "b3J0YW50GhAyMDIyLTA1LTA2VDEwOjA0IAIoATAFOAFAAUoQMjAyMi0wNS0wNlQxMDox" +
                            "MHMKGFNjaGVkdWxlZCBVL0kgQXBwci4gRGRsLhIXTm9ybWFsIHVyZ2VudCBpbXBvcnRh" +
                            "bnQaEDIwMjItMDUtMDZUMTA6MDQgAigBMAU4AUABShAyMDIyLTA1LTA2VDEwOjEwWhAy" +
                            "MDIyLTA1LTA3VDEwOjAwbQoSU2NoZWR1bGVkIFUvSSBEZGwuEhdOb3JtYWwgdXJnZW50" +
                            "IGltcG9ydGFudBoQMjAyMi0wNS0wNlQxMDowNCACKAEwBTgBQAFKEDIwMjItMDUtMDZU" +
                            "MTA6MTBaEDIwMjItMDUtMDlUMTA6MDBPChBTY2hlZHVsZWQgdXJnZW50Eg1Ob3JtYWwg" +
                            "dXJnZW50GhAyMDIyLTA1LTA2VDEwOjAxIAIoADAFOAFAAUoQMjAyMi0wNS0wNlQxMDow" +
                            "OWwKG1NjaGVkdWxlZCB1cmdlbnQgQXBwci4gRGRsLhINTm9ybWFsIHVyZ2VudBoQMjAy" +
                            "Mi0wNS0wNlQxMDowMSACKAAwBTgBQAFKEDIwMjItMDUtMDZUMTA6MDlaEDIwMjItMDUt" +
                            "MDdUMTA6MDBmChVTY2hlZHVsZWQgdXJnZW50IERkbC4SDU5vcm1hbCB1cmdlbnQaEDIw" +
                            "MjItMDUtMDZUMTA6MDEgAigAMAU4AUABShAyMDIyLTA1LTA2VDEwOjA5WhAyMDIyLTA1" +
                            "LTA5VDEwOjAw"
                val byteArray = java.util.Base64.getDecoder().decode(dummy)
                return ProtoBuf.decodeFromByteArray(byteArray)
                //return ProtoBuf.decodeFromHexString<T>(dummy)
            } catch (e: Exception)
            {
                return null
            }
        }
    }
}