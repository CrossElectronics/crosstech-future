package crosstech.future

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.findNavController
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
        val navigationBar = binding.navigationBar
        // sets the listeners of the bottom nav bar
        Initializations.popNavigationBar(navigationBar)
        // sets the toolbar as app bar
        setSupportActionBar(binding.toolbar)
        // configures navigation controller
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        navigationBar.setupWithNavController(navController)
        setContentView(view)
    }
}