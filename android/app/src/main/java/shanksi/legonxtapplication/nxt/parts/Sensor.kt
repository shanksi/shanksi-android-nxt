package shanksi.legonxtapplication.nxt.parts

import shanksi.legonxtapplication.nxt.NxtBrick
import shanksi.legonxtapplication.nxt.NxtCommand
import shanksi.legonxtapplication.nxt.NxtCommandType
import java.io.IOException

abstract class Sensor(brick: NxtBrick, protected var port: Port, private var sensorType : Byte) : Part(brick) {
    protected open fun setTypeAndMode(mode: Int) {
        brick.setInputMode(port.portId, sensorType, mode)
    }

    protected open fun LSRead(): ByteArray? {
        val request = byteArrayOf(
            NxtCommandType.DIRECT_COMMAND_REPLY,
            NxtCommand.LS_READ,
            port.portId.toByte()
        )
        val reply = brick.requestData(request) ?: // Error
        return null
        val rxLength = reply[3]
        val rxData = ByteArray(rxLength.toInt())
        if (reply[2].toInt() == 0) {
            System.arraycopy(reply, 4, rxData, 0, rxLength.toInt())
            return rxData
        }
        return null // Error
    }

    protected open fun LSWrite(
        txData: ByteArray,
        rxDataLength: Byte
    ) {
        var request = byteArrayOf(
            NxtCommandType.DIRECT_COMMAND_NOREPLY,
            NxtCommand.LS_WRITE,
            port.portId.toByte(),
            txData.size.toByte(),
            rxDataLength
        )
        request = appendBytes(request, txData)
        try {
            brick.sendData(request)
        } catch (ex: IOException) {
            // really should consider doing sonething here
        }
        //robot.sendData(request);
    }

    private fun appendBytes(
        array1: ByteArray,
        array2: ByteArray
    ): ByteArray {
        val array = ByteArray(array1.size + array2.size)
        System.arraycopy(array1, 0, array, 0, array1.size)
        System.arraycopy(array2, 0, array, array1.size, array2.size)
        return array
    }
}