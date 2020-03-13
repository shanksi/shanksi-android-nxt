package shanksi.legonxtapplication.nxt.parts

import android.util.Log
import shanksi.legonxtapplication.nxt.NxtBrick

/**
 *
 */
class Motor(brick: NxtBrick, private var port: Port, private var reverse : Boolean = false) : Part(brick) {
    private val TAG = "Motor"

    private val regulationMode = RegulationMode.REGULATION_MODE_MOTOR_SPEED
    private val turnRatio = 0
    private val speed = 60

    internal enum class MotorState {
        FORWARD, BACKWARD, STOPPED, UNDEFINED
    }

    interface RegulationMode {
        companion object {
            const val REGULATION_MODE_IDLE: Byte = 0x00
            const val REGULATION_MODE_MOTOR_SPEED: Byte = 0x01
            const val REGULATION_MODE_MOTOR_SYNC: Byte = 0x02
        }
    }

    interface MotorRunState {
        companion object {
            const val MOTOR_RUN_STATE_IDLE: Byte = 0x00
            const val MOTOR_RUN_STATE_RAMPUP: Byte = 0x10
            const val MOTOR_RUN_STATE_RUNNING: Byte = 0x20
            const val MOTOR_RUN_STATE_RAMPDOWN: Byte = 0x40
        }
    }

    interface MotorMode {
        companion object {
            var MOTOR_ON: Byte = 0x01
            var BRAKE: Byte = 0x02
            var REGULATED: Byte = 0x04
        }
    }

    override fun init() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun cleanup() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun setMotorSpeedTo(speed: Int) {
        val runState = MotorRunState.MOTOR_RUN_STATE_RUNNING
        val mode = MotorMode.BRAKE + MotorMode.REGULATED + MotorMode.MOTOR_ON

        brick.setOutputState(
            port.portId,
            speed.toByte(),
            mode + MotorMode.MOTOR_ON,
            regulationMode,
            turnRatio,
            runState,
            0
        )
    }

    fun go(speed:Int) {
        val convertedSpeed = if (reverse) -speed else speed
        setMotorSpeedTo(convertedSpeed)
    }

    fun forward() {
        val convertedSpeed = if (reverse) -speed else speed
        setMotorSpeedTo(convertedSpeed)
    }

    fun stop() {
        setMotorSpeedTo(0)
    }
}