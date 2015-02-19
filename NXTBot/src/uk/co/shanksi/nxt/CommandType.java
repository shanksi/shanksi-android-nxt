package uk.co.shanksi.nxt;

public interface CommandType
{
	byte DIRECT_COMMAND_REPLY = 0x00;
	byte SYSTEM_COMMAND_REPLY = 0x01;
	byte REPLY_COMMAND = 0x02;
	byte DIRECT_COMMAND_NOREPLY = (byte)0x80;
	byte SYSTEM_COMMAND_NOREPLY = (byte)0x81;
}
