package shanksi.legonxtapplication.nxt.parts

import shanksi.legonxtapplication.nxt.NxtBrick
import shanksi.legonxtapplication.nxt.NxtCommand
import shanksi.legonxtapplication.nxt.NxtCommandType

class BatteryMonitor(brick: NxtBrick) : Part(brick) {

    override fun init() {}

    override fun cleanup() {}

    /**
     * Returns the battery level.
     * @return voltage (in Volt)
     */
    fun getBatteryLevel(): Double {
        var batteryLevel = 0
        val request = byteArrayOf(
            NxtCommandType.DIRECT_COMMAND_REPLY, NxtCommand.GET_BATTERY_LEVEL
        )
        val reply: ByteArray = brick.requestData(request) ?: return 0.0

        batteryLevel = 0xFF and reply[3].toInt() or (0xFF and reply[4].toInt() shl 8)
        return batteryLevel / 1000.0
    }
}