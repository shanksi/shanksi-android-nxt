package shanksi.legonxtapplication.nxt

object NxtCommandType {
    const val DIRECT_COMMAND_REPLY: Byte = 0x00
    const val SYSTEM_COMMAND_REPLY: Byte = 0x01
    const val REPLY_COMMAND: Byte = 0x02
    const val DIRECT_COMMAND_NOREPLY = 0x80.toByte()
    const val SYSTEM_COMMAND_NOREPLY = 0x81.toByte()
}
