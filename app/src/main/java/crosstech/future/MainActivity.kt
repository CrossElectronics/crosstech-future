package crosstech.future

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        Initializations.popNavigationBar(navigationBar)
        setContentView(view)
    }
}