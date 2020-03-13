package shanksi.legonxtapplication.nxt.parts

import shanksi.legonxtapplication.nxt.NxtBrick

abstract class I2CSensor(brick: NxtBrick, port: Port, sensorType: Byte) : Sensor(brick, port, sensorType) {
    private val DEFAULT_ADDRESS: Byte = 0x02

    override fun init() {
        setTypeAndMode(SensorMode.RAW_MODE)
    }

    override fun cleanup() {
    }

    /**
     * Retrieves data from the sensor.
     * Data is read from registers in the sensor, usually starting at 0x00
     * and ending around 0x49.
     * Just supply the register to start reading at, and the length of
     * bytes to read (16 maximum).
     * NOTE: The NXT supplies an unsigned byte but Java converts them into signed bytes.
     * @param register the register used, e.g. FACTORY_SCALE_DIVISOR, BYTE0, etc....
     * @param length the length of data to read (minimum 1, maximum 16)
     * @return the data from the sensor
     */
    open fun getData(register: Byte, length: Int): ByteArray? {
        // checkConnect()
        val txData = byteArrayOf(
            DEFAULT_ADDRESS, register
        )
        LSWrite(txData, length.toByte())
        delay(500)
        //byte[] result = {0x00, 0x12, 0x14};
        return LSRead()
    }

    /**
     * Sets a single byte in the I2C sensor.
     * (Fails to work with some I2C sensors.)
     * @param register the data register in the I2C sensor
     * @param value the data sent to the sensor
     */
    open fun sendData(register: Byte, value: Byte) {
        // checkConnect()
        val txData = byteArrayOf(
            DEFAULT_ADDRESS, register, value
        )
        LSWrite(txData, 0.toByte())
    }

    /**
     * Sets two consecutive bytes in the I2C sensor.
     * (Fails to work with some I2C sensors.)
     * @param register the first data register in the I2C sensor
     * @param value1 the first data value sent to the sensor
     * @param value2 the second data value sent to the sensor
     */
    open fun sendData(
        register: Byte,
        value1: Byte,
        value2: Byte
    ) {
        // checkConnect()
        //   System.out.println("reg: " + register);
        val txData = byteArrayOf(
            DEFAULT_ADDRESS, register, value1, value2
        )
        LSWrite(txData, 0.toByte())
    }

}