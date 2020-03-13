package shanksi.legonxtapplication.nxt

import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class NxtBrick(private val outputStream: OutputStream, private val inputStream: InputStream) {
    private val TAG = "NxtBrick"

    /**
     * Sends a direct command to the brick.
     * @param command the data to send
     */
    @Synchronized
    fun sendData(command: ByteArray) {
        Log.v(TAG, "sending data")
/*        if (uk.co.shanksi.nxt.LegoRobot.myClosingMode == uk.co.shanksi.nxt.LegoRobot.ClosingMode.ReleaseOnClose && uk.co.shanksi.nxt.LegoRobot.isReleased) throw RuntimeException(
            "Java frame disposed"
        )*/
        val lenMSB = byteArrayOf((command.size shr 8).toByte())
        val lenLSB = byteArrayOf(command.size.toByte())
        try {
            outputStream.write(lenLSB)
            outputStream.write(lenMSB)
            outputStream.write(command)
        } catch (ex: IOException) {
            Log.e(TAG, ex.message)
        }
    }

    /**
     * Reads the data returned as a reply from the brick.
     * @return the brick's reply
     */
    @Synchronized
    fun readData(): ByteArray? {
        Log.v(TAG, "reading data")
        var reply: ByteArray? = null
        var length = -1
        val lenMSB: Int
        var lenLSB: Int
        try {
            do lenLSB = inputStream.read() while (lenLSB < 0)
            lenMSB = inputStream.read() // MSB of reply length
            length = 0xFF and lenLSB or (0xFF and lenMSB shl 8)
            reply = ByteArray(length)
            // Rest of packet
            inputStream.read(reply)
        } catch (ex: IOException) {
            Log.e(TAG, ex.message)
        }
        return reply
    }

    /**
     * Combines sendData() and readData() in synchronized block.
     * @param request the data to send
     * @return the brick's reply
     */
    @Synchronized
    fun requestData(request: ByteArray?): ByteArray? {
        sendData(request!!)
        return readData()
    }

    /**
     * Sets the output state of a specific output port.
     */
    @Synchronized
    fun setOutputState(
        portId: Int,
        power: Byte,
        mode: Int,
        regulationMode: Byte,
        turnRatio: Int,
        runState: Byte,
        tachoLimit: Long
    ) {

        val request = byteArrayOf(
            NxtCommandType.DIRECT_COMMAND_NOREPLY,
            NxtCommand.SET_OUTPUT_STATE,
            portId.toByte(),
            power,
            mode.toByte(),
            regulationMode,
            turnRatio.toByte(),
            runState,
            tachoLimit.toByte(),
            (tachoLimit ushr 8).toByte(),
            (tachoLimit ushr 16).toByte(),
            (tachoLimit ushr 24).toByte()
        )
        sendData(request)
    }

    @Synchronized
    fun setInputMode(portId: Int, sensorType: Byte, sensorMode: Int) {
        Log.v(TAG, "setInputMode")
        val request = byteArrayOf(
            NxtCommandType.DIRECT_COMMAND_NOREPLY,
            NxtCommand.SET_INPUT_MODE,
            portId.toByte(),
            sensorType,
            sensorMode.toByte()
        )
        try {
            sendData(request)
        } catch (ex: IOException) {
            Log.e(TAG, ex.message)
        }
    }

    /**
     * Reads the values from given a sensor port.
     * @param portId the Id of the sensor port (0,..3)
     */
    @Synchronized
    fun getInputValues(portId: Int): InputValues? {
        val request = byteArrayOf(
            NxtCommandType.DIRECT_COMMAND_REPLY,
            NxtCommand.GET_INPUT_VALUES,
            portId.toByte()
        )

        val inputValues = InputValues()

        val reply = requestData(request)
        if (reply != null) {
            inputValues.inputPort = reply[3].toInt()
            // 0 is false, 1 is true.
            inputValues.valid = reply[4].toInt() != 0
            // 0 is false, 1 is true.
            inputValues.isCalibrated = reply[5].toInt() == 0
            inputValues.sensorType = reply[6].toInt()
            inputValues.sensorMode = reply[7].toInt()
            inputValues.rawADValue = reply[8].toInt() or (reply[9].toInt() shl 8)
            inputValues.normalizedADValue = reply[10].toInt() or (reply[11].toInt() shl 8)
            inputValues.scaledValue = (reply[12].toInt() or (reply[13].toInt() shl 8)).toShort()
            inputValues.calibratedValue = (reply[14].toInt() or (reply[15].toInt() shl 8)).toShort()

            inputValues.printValues(TAG)
        }
        return inputValues
    }
}