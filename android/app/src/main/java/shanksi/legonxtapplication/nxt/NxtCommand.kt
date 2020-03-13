package shanksi.legonxtapplication.nxt

object NxtCommand {
    const val START_PROGRAM: Byte = 0x00
    const val STOP_PROGRAM: Byte = 0x01
    const val PLAY_SOUND_FILE: Byte = 0x02
    const val PLAY_TONE: Byte = 0x03
    const val SET_OUTPUT_STATE: Byte = 0x04
    const val SET_INPUT_MODE: Byte = 0x05
    const val GET_OUTPUT_STATE: Byte = 0x06
    const val GET_INPUT_VALUES: Byte = 0x07
    const val RESET_SCALED_INPUT_VALUE: Byte = 0x08
    const val MESSAGE_WRITE: Byte = 0x09
    const val RESET_MOTOR_POSITION: Byte = 0x0A
    const val GET_BATTERY_LEVEL: Byte = 0x0B
    const val STOP_SOUND_PLAYBACK: Byte = 0x0C
    const val KEEP_ALIVE: Byte = 0x0D
    const val LS_GET_STATUS: Byte = 0x0E
    const val LS_WRITE: Byte = 0x0F
    const val LS_READ: Byte = 0x10
    const val GET_CURRENT_PROGRAM_NAME: Byte = 0x11
    const val MESSAGE_READ: Byte = 0x13
}