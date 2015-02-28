package uk.co.shanksi.nxt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



public class NxtRobot
{
	private NxtBrick brick;
	private TonePlayer tonePlayer;

	private Motor left=new Motor(MotorPort.C);
	private Motor right=new Motor(MotorPort.B);

    private UltrasonicSensor ultrasonicSensor = new UltrasonicSensor(SensorPort.S4);
	
    public NxtRobot(NxtBrick brick) {
		this.brick = brick;
		this.tonePlayer = new TonePlayer();
		this.tonePlayer.setBrick(this.brick);	
        try {
		    this.tonePlayer.playConnectMelody();
			
			
			this.left.setBrick(this.brick);
			this.right.setBrick(this.brick);


			
			
			
		} catch(IOException ex) {}
	}	
	
	public void setMotors(int left,int right) {
    	this.left.setSpeed(left);
        this.right.setSpeed(right);
        this.left.forward();
        this.right.forward();
	
	}
	
	public void forward() {
		this.setMotors(70, 70);
	}
	
	
	public void leftspin() {
		this.setMotors(-70, 70);
	}
	
	public void rightspin() {
		this.setMotors(70, -70);
	}
	
	public void stop() {
		this.setMotors(0, 0);
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
