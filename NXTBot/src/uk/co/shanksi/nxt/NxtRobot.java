package uk.co.shanksi.nxt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import at.smartlab.lego.NxtBrick;

public class NxtRobot
{
	private NxtBrick brick;
	private TonePlayer tonePlayer;

	private Motor left=new Motor(MotorPort.C);
	private Motor right=new Motor(MotorPort.B);
	
    public NxtRobot(NxtBrick brick) {
		this.brick = brick;
		this.tonePlayer = new TonePlayer();
		this.tonePlayer.setBrick(this.brick);	
        try {
		    this.tonePlayer.playConnectMelody();
			
			
			this.left.setBrick(this.brick);
			this.right.setBrick(this.brick);
			
			this.right.backward(true);
			this.left.forward(true);

			
			delay(1000);
			
			this.left.stop();
			this.right.stop();
			
		} catch(IOException ex) {}
	}	
	
	public NxtRobot(OutputStream out, 
					InputStream in) {
						
		this.brick = new NxtBrick(out, in);
		this.tonePlayer = new TonePlayer();
		this.tonePlayer.setBrick(this.brick);	
        try {
		    this.tonePlayer.playConnectMelody();
		} catch(IOException ex) {}
	}
	
	

	public void delay(int msec) {
	    try {
		    Thread.sleep(msec);                 //1000 milliseconds is one second.
	    } catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
    	}	
	}
	
	
}
