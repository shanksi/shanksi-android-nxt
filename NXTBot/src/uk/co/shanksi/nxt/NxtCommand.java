package uk.co.shanksi.nxt;

public interface NxtCommand
{
	byte START_PROGRAM = 0x00;
	byte STOP_PROGRAM = 0x01;
	byte PLAY_SOUND_FILE = 0x02;
	byte PLAY_TONE = 0x03;
	byte SET_OUTPUT_STATE = 0x04;
	byte SET_INPUT_MODE = 0x05;
	byte GET_OUTPUT_STATE = 0x06;
	byte GET_INPUT_VALUES = 0x07;
	byte RESET_SCALED_INPUT_VALUE = 0x08;
	byte MESSAGE_WRITE = 0x09;
	byte RESET_MOTOR_POSITION = 0x0A;
	byte GET_BATTERY_LEVEL = 0x0B;
	byte STOP_SOUND_PLAYBACK = 0x0C;
	byte KEEP_ALIVE = 0x0D;
	byte LS_GET_STATUS = 0x0E;
	byte LS_WRITE = 0x0F;
	byte LS_READ = 0x10;
	byte GET_CURRENT_PROGRAM_NAME = 0x11;
	byte MESSAGE_READ = 0x13;
	
}
