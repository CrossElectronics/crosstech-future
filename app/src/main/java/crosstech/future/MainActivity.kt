package crosstech.future

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import crosstech.future.databinding.ActivityMainBinding
import crosstech.future.gui.Initializations
import crosstech.future.gui.tasks.TaskEditFragment

class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding
    private lateinit var global: Global
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        // initialization
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // configures bottom and top bar
        val navigationBar = binding.navigationBar
        setSupportActionBar(binding.toolbar)
        // configures navigation controller
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        navigationBar.setupWithNavController(navController)
        Initializations.setupNavController(navController, navigationBar)
        // loads data
        global = applicationContext as Global
        global.tasks = Initializations.loadData(Global.TASKS_FILE, this) ?: mutableListOf()
        global.archive = Initializations.loadData(Global.ARCHIVE_FILE, this) ?: mutableListOf()
    }

    override fun onBackPressed()
    {
        val frag = supportFragmentManager.findFragmentById(R.id.content)
        if (frag is TaskEditFragment)
        {
            frag.dismiss()
        }
        else
        {
            super.onBackPressed()
        }
    }
}