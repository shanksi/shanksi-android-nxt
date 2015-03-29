package uk.co.shanksi.nxt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



public class NxtRobot
{
	private NxtBrick brick;
	private TonePlayer tonePlayer;

	private Motor left;
	private Motor right;

    private UltrasonicSensor ultrasonicSensor = new UltrasonicSensor(SensorPort.S4);
	
    public NxtRobot(NxtBrick brick) {
		this.brick = brick;
		this.tonePlayer = (TonePlayer)addPart(new TonePlayer());
		//this.tonePlayer.setBrick(this.brick);	
        try {
		    this.tonePlayer.playConnectMelody();
			
			

        	this.left=(Motor)addPart(new Motor(MotorPort.C));
	        this.right=(Motor)addPart(new Motor(MotorPort.B));		

			this.ultrasonicSensor.setBrick(this.brick);
			this.ultrasonicSensor.init();
			
			
		} catch(IOException ex) {}
	}	
	
	public Part addPart(Part part) {
		part.setBrick(this.brick);
		return part;
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
	
	public int distance() {
		
		return this.ultrasonicSensor.getDistance();
		
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
