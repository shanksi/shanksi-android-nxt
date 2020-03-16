package shanksi.legonxtapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlinx.android.synthetic.main.activity_main.*
import shanksi.legonxtapplication.bluetooth.BluetoothConnector
import shanksi.legonxtapplication.nxt.NxtRobot

class MainActivity : AppCompatActivity() {
    private val bluetoothConnector: BluetoothConnector = BluetoothConnector(this)
    private var robot: NxtRobot? = null

    // private var batteryIndicator: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        configureButtons()
    }

    private fun configureButtons() {
        openconnection_button.setOnClickListener(clickListener)

        // batteryIndicator = findViewById<TextView>(R.id.batteryIndicator)
    }

    private fun setupJoystick () {
        val joystick: JoystickView = findViewById<JoystickView>(R.id.joystickView)
        joystick.setOnMoveListener(object : JoystickView.OnMoveListener {
            override fun onMove(angle: Int, strength: Int) {
                // do whatever you want
                robot?.moveOnJoystick(angle, strength)
            }
        })
    }

    private val clickListener = View.OnClickListener { view ->
        (view as Button).text = "clicked"
        connectBluetooth()
    }

    private fun connectBluetooth() {
        val socket = bluetoothConnector.connectToNXT()
        if (socket != null) {
            robot = NxtRobot(socket)
            robot?.playConnect()
            startBatteryMonitorThread()
            startDistanceMonitorThread()
            // robot?.makeGo()
            // robot?.lightUpSensor()

            setupJoystick()

            val fragment = supportFragmentManager.findFragmentById(R.id.lightsfragment) as LightsFragment
            fragment.setRobot(robot, true)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Check which request we're responding to
        if (requestCode == BluetoothConnector.REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                connectBluetooth()
            }
        }
    }

    private fun startBatteryMonitorThread() {
        Thread(Runnable {
            while(true) {
                Thread.sleep(5000)
                val batteryValue = robot?.getBatteryValue()
                batteryIndicator?.post {
                    batteryIndicator?.text = batteryValue.toString()
                }
            }
        }).start()
    }

    private fun startDistanceMonitorThread() {
        Thread(Runnable {
            while(true) {
                Thread.sleep(200)
                val distanceValue = robot?.getDistance()
                distanceIndicator?.post {
                    distanceIndicator?.text = distanceValue.toString()
                }
            }
        }).start()
    }
}
