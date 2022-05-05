package crosstech.future

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import crosstech.future.databinding.ActivityMainBinding
import crosstech.future.gui.Initializations

class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding
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
    }
}