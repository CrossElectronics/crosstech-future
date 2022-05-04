package crosstech.future

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import crosstech.future.databinding.ActivityMainBinding
import crosstech.future.databinding.DashboardFragmentBinding

class DashboardFragment: Fragment(R.layout.dashboard_fragment)
{
    private lateinit var binding: DashboardFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        // initialization
        binding = DashboardFragmentBinding.inflate(inflater)
        binding.textView3.typeface = resources.getFont(R.font.nova_mono)
        binding.textView3.text = "--:--:--"
        return binding.root
    }
}