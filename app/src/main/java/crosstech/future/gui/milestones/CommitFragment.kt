package crosstech.future.gui.milestones

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import crosstech.future.R
import crosstech.future.logics.models.Milestone

/**
 * A simple [Fragment] subclass.
 * Use the [CommitFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommitFragment : Fragment()
{
    private lateinit var milestone: Milestone

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        arguments?.let {
            milestone = it.getParcelable(ARG_COMMIT)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_commit, container, false)
    }

    companion object
    {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param milestone parameter to pass
         * @return A new instance of fragment CommitFragment.
         */
        @JvmStatic
        fun newInstance(milestone: Milestone) =
            CommitFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_COMMIT, milestone)
                }
            }

        private const val ARG_COMMIT = "milestone"
    }
}