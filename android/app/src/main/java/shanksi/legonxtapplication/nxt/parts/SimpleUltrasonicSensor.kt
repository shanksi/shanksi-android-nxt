package shanksi.legonxtapplication.nxt.parts

import shanksi.legonxtapplication.nxt.InputValues
import shanksi.legonxtapplication.nxt.NxtBrick

class SimpleUltrasonicSensor(brick: NxtBrick, var port: Port) : Part(brick) {
    private val TAG = "ColorSensor"

    override fun init() {
        // TODO("Not yet implemented")
    }

    override fun cleanup() {
        // TODO("Not yet implemented")
    }

    fun setMode(){
        brick.setInputMode(port.portId, SensorType.LOWSPEED_9V, SensorMode.RAW_MODE)
    }

    fun read(): InputValues? {
        return brick.getInputValues(port.portId)
    }

    fun getDistance(): Short? {
        setMode() // TODO: check if this is actually needed here
        var inputValues = read()
        return inputValues?.scaledValue
    }
}