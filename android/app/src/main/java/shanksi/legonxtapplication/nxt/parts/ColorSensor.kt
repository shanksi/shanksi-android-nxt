package shanksi.legonxtapplication.nxt.parts

import shanksi.legonxtapplication.nxt.NxtBrick

class ColorSensor(brick: NxtBrick, port: Port) : Sensor(brick, port, SensorType.LIGHT_ACTIVE) {

    private val TAG = "ColorSensor"

    data class ColorPack(val red: Short?, val green: Short?, val blue: Short?, val full: Short?)

    private val COLORFULL: Byte =
        0x0D  // NXT 2.0 color sensor in full color mode (color sensor mode)
    val COLORRED: Byte = 0x0E   // NXT 2.0 color sensor with red light on  (light sensor mode)
    private val COLORGREEN: Byte =
        0x0F // NXT 2.0 color sensor with green light on (light sensor mode)
    private val COLORBLUE: Byte =
        0x10  // NXT 2.0 color sensor in with blue light on (light sensor mode)
    private val COLORNONE: Byte =
        0x11  // NXT 2.0 color sensor in with light off (light sensor mode)
    private val COLOREXIT: Byte =
        0x12  // NXT 2.0 color sensor internal state  (not sure what this is for yet)

    override fun init() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cleanup() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun setColorMode(mode: Byte) {
        brick.setInputMode(port.portId, mode, SensorMode.RAW_MODE)
    }

    fun lightUp() {
        setColorMode(SensorType.LIGHT_ACTIVE) // makes a short flash??
        Thread.sleep(1000)

        setColorMode(COLORRED) // works
        Thread.sleep(200)
        setColorMode(COLORGREEN) // works
        Thread.sleep(200)
        setColorMode(COLORBLUE) // works
        Thread.sleep(200)
        setColorMode(COLORFULL) // just switches light off, although maybe sets up sensor in background, so???
    }

    fun getReflectedLight(color: Byte): Short? {
        setColorMode(color)
        var inputValues = brick.getInputValues(port.portId)
        return inputValues?.scaledValue
    }

    fun getLightReadings(): ColorPack {
        return ColorPack(
            getReflectedLight(COLORRED),
            getReflectedLight(COLORGREEN),
            getReflectedLight(COLORBLUE),
            getReflectedLight(COLORFULL)
        )
    }

}