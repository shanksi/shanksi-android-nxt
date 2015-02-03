package at.smartlab.lego;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class NxtBrick {
	
	private OutputStream out;
	private InputStream in;
	
	
	/**
	 * The constructor needs both io streams 
	 * to communicate with a NXT brick.
	 *
	 * @param out the stream to send commands to
	 * @param in the stream to read responses from
	 */
	public NxtBrick(OutputStream out, 
			InputStream in) {
		this.out = out;
		this.in = in;
try{
        playTone('d', 'z'); sayHello();
	
		} catch (IOException ex) {}}
	
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
		sendMessage(msg);
	}

    public void sayHello()
            throws IOException {
        byte [] msg = new byte [6];
        msg[0] = (byte)0x80;
        msg[1] = (byte)0x09;
        msg[2] = (byte)0x00; // Inbox number
        msg[3] = (byte)0x05; // 5 chars
        msg[4] = (byte)0x48; // H
        msg[5] = (byte)0x65; // e
        msg[6] = (byte)0x6c; // l
        msg[7] = (byte)0x6c; // l
        msg[8] = (byte)0x6f; // o
        sendMessage(msg);
    }







	public void setOutputState(byte motor, 
			byte power, 
			boolean speedReg, 
			boolean motorSync, 
			byte runState) throws IOException {
		byte[] msg = { (byte)0x80, 0x04, motor, power, 
				0x01, 0x01, 0x33, runState, 0x00, 
				0x00, 0x00, 0x00 };
 
		sendMessage(msg);
	}
	
	private void sendMessage(byte [] msg) throws IOException {
		if(out !=null) {
			out.write(msg.length & 0xff);
			out.write((msg.length >> 8) & 0xff);
			out.write(msg);
			out.flush();
		}
	}
}
