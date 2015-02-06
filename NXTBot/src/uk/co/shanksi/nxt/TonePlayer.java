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
	public void playTone(int f, int msec) 
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
	
	/**
	 * Plays a standard connect melody.
	 */
	public synchronized void playConnectMelody()
	throws IOException {
		delay(1000);
		playTone(600, 100);
		delay(100);
		playTone(900, 100);
		delay(100);
		playTone(750, 200);
		delay(200);
	}

	/**
	 * Plays a standard disconnect melody.
	 */
	public synchronized void playDisconnectMelody()
	throws IOException {
		delay(1000);
		playTone(900, 100);
		delay(100);
		playTone(750, 100);
		delay(100);
		playTone(600, 200);
		delay(200);
	}
	
	
	// Called when connected
	protected void init() {}

	// Called to cleanup
	protected void cleanup() {}
	
	protected void delay(int msec) {
	try {
		Thread.sleep(msec);                 //1000 milliseconds is one second.
	} catch(InterruptedException ex) {
		Thread.currentThread().interrupt();
	}	}
}
