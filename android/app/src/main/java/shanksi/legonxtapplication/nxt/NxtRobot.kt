package shanksi.legonxtapplication.nxt

import android.bluetooth.BluetoothSocket
import android.util.Log
import shanksi.legonxtapplication.nxt.parts.*


class NxtRobot(socket: BluetoothSocket) {
    private val brick = NxtBrick(socket.outputStream, socket.inputStream)

    private val tonePlayer = TonePlayer(brick)
    private val batteryMonitor = BatteryMonitor(brick)

    private val colorSensor = ColorSensor(brick, Port.sensor1)
    private val ultrasonicSensor = UltrasonicSensor(brick, Port.sensor4)

    private val leftMotor = Motor(brick, Port.motorC, true)
    private val rightMotor = Motor(brick, Port.motorB, true)

    init {
        ultrasonicSensor.init()
    }

    fun playConnect() {
        Thread.sleep(1000)
        tonePlayer.playConnectMelody()
    }

    fun getBatteryValue(): Double {
        return batteryMonitor.getBatteryLevel()
    }

    fun makeGo() {
        leftMotor.forward()
        rightMotor.forward()
        Thread.sleep(2000)
        leftMotor.stop()
        rightMotor.stop()
    }

    fun getLightReading(): ColorSensor.ColorPack? {
        return colorSensor.getLightReadings()
    }

    fun getDistance(): Short? {
        return ultrasonicSensor.getDistance(true).toShort()
    }

    fun moveOnJoystick(angle: Int, strength: Int) {
        // The following calculation borrows from
        // https://github.com/jfedor2/nxt-remote-control/blob/master/src/org/jfedor/nxtremotecontrol/NXTRemoteControl.java
        // but no need to use radians so it should simplify it a bit

        var speedLeftRight = listOf(0, 0)
        // work out which quarter of the circle we're in - starting from due right and going anti-clockwise
        if (angle != 0) {
            val quarter = (angle / 90) + 1

            // TODO: Think about this - something's not quite right in the way it works
            val lr = when (quarter) {
                1 -> listOf(1.0f, angle / 90f)
                2 -> listOf((180f - angle) / 90f, 1.0f)
                3 -> listOf(-1.0f, -(180 + angle) / 90f)
                4 -> listOf(-(360f - angle) / 90f, -1.0f)
                else -> listOf(0f, 0f)
            }

            speedLeftRight = lr.map { u -> (strength * u).toInt() }
            Log.v("NxtRobot", "Joystick $angle $strength $quarter $lr $speedLeftRight")
        }
        leftMotor.go(speedLeftRight[0])
        rightMotor.go(speedLeftRight[1])
    }
}