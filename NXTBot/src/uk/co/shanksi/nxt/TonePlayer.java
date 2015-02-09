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
		msg[1] = NxtCommand.PLAY_TONE;
		msg[2] = (byte)(f);
		msg[3] = (byte)(f >>> 8);
		msg[4] = (byte)(msec);
		msg[5] = (byte)(msec >>> 8);
		//robot.sendData(msg);
		brick.sendMessage(msg);
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
		msg[1] = NxtCommand.PLAY_TONE;
		msg[2] = (byte)(f & 0xff);
		msg[3] = (byte)((f >> 8) & 0xff);
		msg[4] = (byte)(msec & 0xff);
		msg[5] = (byte)((msec >> 8) & 0xff);
		//robot.sendData(msg);
		brick.sendMessage(msg);		
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
    	}	
	}
	
	
	
	
	
	private static String[] notes = { "C3", "C#3", "Db3", "D3", "D#3", "Eb3",
		"E3", "F3", "F#3", "Gb3", "G3", "G#3", "Ab3", "A3", "A#3", "Bb3",
		"B3", "C4", "C#4", "Db4", "D4", "D#4", "Eb4", "E4", "F4", "F#4",
		"Gb4", "G4", "G#4", "Ab4", "A4", "A#4", "Bb4", "B4", "C5", "C#5",
		"Db5", "D5", "D#5", "Eb5", "E5", "F5", "F#5", "Gb5", "G5", "G#5",
		"Ab5", "A5", "A#5", "Bb5", "B5", "C6" };

	private static float[] frequency = { 130.81f, 138.59f, 138.59f, 146.83f,
		155.56f, 155.56f, 164.81f, 174.61f, 185.0f, 185.0f, 196.0f,
		207.65f, 207.65f, 220.0f, 233.08f, 233.08f, 246.94f, 261.63f,
		277.18f, 277.18f, 293.66f, 311.13f, 311.13f, 329.63f, 349.23f,
		369.99f, 369.99f, 392.0f, 415.3f, 415.3f, 440.0f, 466.16f, 466.16f,
		493.88f, 523.25f, 554.37f, 554.37f, 587.33f, 622.25f, 622.25f,
		659.26f, 698.46f, 739.99f, 739.99f, 783.99f, 830.61f, 830.61f,
		880.0f, 932.33f, 932.33f, 987.77f, 1046.5f };

	public int mapNote(String note) {
		for (int i = 0; i < notes.length; i++) {
			if(note.equals(notes[i]))
				return (int)frequency[i];
		}
        return 0;			
	}
	
	private int guidoOctave = 1;
	private int guidoDuration = 1;
	
	
	
		
	/**
	 * method uses playTone method
	 * @param note is a String representation of the musical note in range C3-C6. See notes[] for allowed values
	 * @param duration is note duration in ms
	 */
	public void musicTone(String note, int duration) {		
		try {
			playTone(mapNote(note), duration);
			delay(duration);
		} catch (IOException ex) {}
	}
	
	public void playGuido(String guido) {
		int noteLength = 500;
		String[] guidoNotes = guido.split(" ");
		for(int i=0; i<guidoNotes.length;++i) {
			String[] guidoSplit = guidoNotes[i].split("/");
			if(guidoSplit.length > 1) {
				guidoDuration = Integer.parseInt(guidoSplit[1]);
			}
			musicTone(guidoSplit[0], noteLength / guidoDuration);
		}
	}	
}
