package uk.co.shanksi.nxt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import at.smartlab.lego.NxtBrick;

public class NxtRobot
{
	private NxtBrick brick;
	private TonePlayer tonePlayer;

    public NxtRobot(NxtBrick brick) {
		this.brick = brick;
		this.tonePlayer = new TonePlayer();
		this.tonePlayer.setBrick(this.brick);	
        try {
		    this.tonePlayer.playConnectMelody();
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
}
