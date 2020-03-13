package shanksi.legonxtapplication.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import shanksi.legonxtapplication.nxt.NxtBrick
import shanksi.legonxtapplication.nxt.parts.TonePlayer
import java.io.IOException
import java.util.*

class BluetoothConnector(private val activity: Activity) {
    companion object {
        const val REQUEST_ENABLE_BT: Int = 1
        const val TAG = "MY_APP_DEBUG_TAG"
    }

    private val MY_UUID: UUID = UUID.fromString("DEADBEEF-0000-0000-0000-123000000045") // TODO: Fix this sometime
    private val adapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var device: BluetoothDevice? = null

    fun enable(): Boolean {
        // Device doesn't support Bluetooth
        if (adapter == null) {
            // maybe do nothing - no BT support
            return false
        }

        // See if it's enabled
        if (!adapter?.isEnabled) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(activity, enableBluetoothIntent, REQUEST_ENABLE_BT, null)
            return false
        }

        return true
    }

    private fun findNXTDevice(): BluetoothDevice? {
        val pairedDevices: Set<BluetoothDevice>? = adapter?.bondedDevices
        pairedDevices?.forEach { pairedDevice: BluetoothDevice ->
            if(pairedDevice.name == "NXT") {
                return pairedDevice
            }
        }
        return null
        // device = adapter?.getRemoteDevice("00:16:53:13:0C:7F")
    }

    private fun getSocket(device: BluetoothDevice) : BluetoothSocket {
        // This would be the simple way to do it but doesn't seem to work on many phones
        // return device.createRfcommSocketToServiceRecord(MY_UUID)
        // so...
        val paramTypes = arrayOf<Class<*>>(Integer.TYPE)
        val m = device.javaClass.getMethod("createRfcommSocket", *paramTypes)
        return m.invoke(device, Integer.valueOf(1)) as BluetoothSocket
    }

    fun connectToNXT() : BluetoothSocket? {
        if(enable()) {
            device = findNXTDevice()
            if(device != null) {
                // ConnectThread(device as BluetoothDevice).run()
                val socket = getSocket(device as BluetoothDevice)
                if(!socket.isConnected) {
                    socket.connect()
                    Log.d(TAG, "Connected in main process")

                    return socket
                }
            }
        }
        return null
    }

    private inner class ConnectThread(device: BluetoothDevice) : Thread() {

        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createRfcommSocketToServiceRecord(MY_UUID)
        }

        public override fun run() {
            // Cancel discovery because it otherwise slows down the connection.
            adapter?.cancelDiscovery()

            mmSocket?.use { socket ->
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                socket.connect()
                Log.d(TAG, "Connected")
                // The connection attempt succeeded. Perform work associated with
                // the connection in a separate thread.
                // manageMyConnectedSocket(socket)
            }
        }

        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                Log.e(TAG, "Could not close the client socket", e)
            }
        }
    }
}