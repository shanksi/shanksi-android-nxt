package uk.co.shanksi.nxt;

public interface MotorRunState
{
	byte MOTOR_RUN_STATE_IDLE = 0x00;
	byte MOTOR_RUN_STATE_RAMPUP = 0x10;
	byte MOTOR_RUN_STATE_RUNNING = 0x20;
	byte MOTOR_RUN_STATE_RAMPDOWN = 0x40;
}
