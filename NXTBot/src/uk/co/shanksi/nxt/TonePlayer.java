package uk.co.shanksi.nxt;

import java.io.IOException;
public class TonePlayer extends Part
{
	/**
	 * Play a tone on the Nxt brick.
	 *
	 * @param frequency 
	 * @param msec duration 
	 */
	public void playTone(char f, char msec) 
	throws IOException {
		byte [] msg = new byte [6];
		msg[0] = (byte)0x80;
		msg[1] = (byte)0x03;
		msg[2] = (byte)(f & 0xff);
		msg[3] = (byte)((f >> 8) & 0xff);
		msg[4] = (byte)(msec & 0xff);
		msg[5] = (byte)((msec >> 8) & 0xff);
		robot.sendData(msg);
	}
	// Called when connected
	protected void init() {}

	// Called to cleanup
	protected void cleanup() {}
	
}
