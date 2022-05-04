package crosstech.future

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
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
        val navigationBar = binding.navigationView
        // sets the listeners of the bottom nav bar
        Initializations.popNavigationBar(navigationBar)
        // sets the toolbar as app bar
        setSupportActionBar(binding.toolbar)
        setContentView(view)
    }
}