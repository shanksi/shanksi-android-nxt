package shanksi.legonxtapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import shanksi.legonxtapplication.nxt.NxtRobot

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "edttext"
// private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LightsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LightsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    // private var param2: String? = null

    private var robot: NxtRobot? = null
    private var isRobotOn: Boolean = false

    private var lightIndicator: TextView? = null
    private var lightIndicatorRed: TextView? = null
    private var lightIndicatorGreen: TextView? = null
    private var lightIndicatorBlue: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            // param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lights, container, false)
    }

    private var monitorThread : Thread? = null;

    private fun setupViews() {
        lightIndicator = view?.findViewById<TextView>(R.id.lightIndicator)
        lightIndicatorRed = view?.findViewById<TextView>(R.id.lightIndicatorRed)
        lightIndicatorGreen = view?.findViewById<TextView>(R.id.lightIndicatorGreen)
        lightIndicatorBlue = view?.findViewById<TextView>(R.id.lightIndicatorBlue)

        val toggle: Switch? = view?.findViewById(R.id.lightswitch)
        toggle?.isEnabled = true
        toggle?.setOnCheckedChangeListener { _, isChecked ->
            val unit = if (isChecked) {
                // The toggle is enabled
                monitorThread = Thread(Runnable {
                    var isRunning = true;
                    while (isRunning) {
                        try {
                            val readings = robot?.getLightReading()
                            lightIndicatorRed?.post {
                                lightIndicatorRed?.text = readings?.red?.toString()
                                lightIndicatorGreen?.text = readings?.green?.toString()
                                lightIndicatorBlue?.text = readings?.blue?.toString()
                                lightIndicator?.text = readings?.full?.toString()
                            }
                            Thread.sleep(5000)
                        } catch (ex: InterruptedException) {
                            isRunning = false
                        }
                    }
                })
                monitorThread?.start()
            } else {
                // The toggle is disabled
                monitorThread?.interrupt()
            }
        }

    }

    fun setRobot(nxtRobot: NxtRobot?, on: Boolean) {
        setupViews()

        robot = nxtRobot
        isRobotOn = on

        // startColorMonitorThread()
    }


    private fun startColorMonitorThread() {
        Thread(Runnable {
            while (true) {
                Thread.sleep(5000)
                val readings = robot?.getLightReading()
                lightIndicatorRed?.post {
                    lightIndicatorRed?.text = readings?.red?.toString()
                    lightIndicatorGreen?.text = readings?.green?.toString()
                    lightIndicatorBlue?.text = readings?.blue?.toString()
                    lightIndicator?.text = readings?.full?.toString()
                }
            }
        }).start()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LightsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LightsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    // putString(ARG_PARAM2, param2)
                }
            }
    }
}
