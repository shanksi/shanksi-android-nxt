package shanksi.legonxtapplication.nxt.parts

import shanksi.legonxtapplication.nxt.NxtBrick

class UltrasonicSensor(brick: NxtBrick, port:Port) : I2CSensor(brick, port, SensorType.LOWSPEED_9V) {

    private val COMMAND_STATE: Byte = 0x41 // Command or reply length = 1

    private val BYTE0: Byte = 0x42
    private val CONTINUOUS_MEASUREMENT: Byte = 0x02

    private fun setSensorMode(modeEnumeration: Byte) {
        sendData(COMMAND_STATE, modeEnumeration)
    }


    public override fun init() {
        setSensorMode(CONTINUOUS_MEASUREMENT)
    }

    fun getDistance(check: Boolean): Int {
        // if (check) checkConnect()
        val `val` = getData(BYTE0, 1) ?: return 255
        // Illegal mesurement
        return 0xFF and `val`[0].toInt() // Convert signed byte to unsigned (positive only)
    }

}