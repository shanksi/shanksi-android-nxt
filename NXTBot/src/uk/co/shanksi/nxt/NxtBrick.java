package uk.co.shanksi.nxt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import uk.co.shanksi.nxt.NxtCommand;
import uk.co.shanksi.nxt.SharedConstants;
import uk.co.shanksi.nxt.CommandType;

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
//try{
      //  playTone('d', 'z');
  //  setOutputState((byte)0x02, (byte)0x55, true, false, (byte)0x20);
	
	//	} catch (IOException ex) {}
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
				0x07, 0x00, 0x00, runState, 0x00, 
				0x00, 0x00, 0x00 };
 
		sendMessage(msg);
	}


    /**
     * Sets the output state of a specific sensor port.
     */
    public synchronized void setOutputState(int portId, byte power, int mode,
                                               int regulationMode, int turnRatio,
                                               int runState, long tachoLimit)
    {
/*    if (debugLevel >= DEBUG_LEVEL_MEDIUM)
      DebugConsole.show("DEBUG: setOutputState("
        + portId + ", "
        + power + ", "
        + mode + ", "
        + regulationMode + ", "
        + turnRatio + ", "
        + runState + ", "
        + tachoLimit + ")");*/
        byte[] request =
                {
			            CommandType.DIRECT_COMMAND_NOREPLY, 
						NxtCommand.SET_OUTPUT_STATE, 
						(byte)portId,
                        power, 
						(byte)mode, 
						(byte)regulationMode,
                        (byte)turnRatio, 
						(byte)runState, 
						(byte)tachoLimit,
                        (byte)(tachoLimit >>> 8), 
						(byte)(tachoLimit >>> 16),
                        (byte)(tachoLimit >>> 24)
                };
				try {
        sendMessage(request);} 
		catch(IOException ex) {}
    }



    public void setInputMode(int portId, int sensorType, int sensorMode)
    {
/*    if (debugLevel >= DEBUG_LEVEL_MEDIUM)
      DebugConsole.show("DEBUG: setInputMode("
        + portId + ", "
        + sensorType + ", "
        + sensorMode + ")");*/
        byte[] request =
                {
                        CommandType.DIRECT_COMMAND_NOREPLY, NxtCommand.SET_INPUT_MODE, (byte)portId,
                        (byte)sensorType, (byte)sensorMode
                };
				try{
        sendMessage(request);} 
	catch(IOException ex) {}
    }

public synchronized byte[] requestData(byte[] request)
{
try{	
	sendMessage(request);} 
catch(IOException ex) {}
	return readData();
}
/**
 * Reads the values from given a sensor port.
 * @param portId the Id of the sensor port (0,..3)
 */
  public synchronized InputValues getInputValues(int portId)
  {
    byte[] request =
    {
      CommandType.DIRECT_COMMAND_REPLY, NxtCommand.GET_INPUT_VALUES, (byte)portId
    };
    InputValues inputValues = new InputValues();
    byte[] reply = requestData(request);
    if (reply != null)
    {
      inputValues.inputPort = reply[3];
      // 0 is false, 1 is true.
      inputValues.valid = (reply[4] != 0);
      // 0 is false, 1 is true.
      inputValues.isCalibrated = (reply[5] == 0);
      inputValues.sensorType = reply[6];
      inputValues.sensorMode = reply[7];
      inputValues.rawADValue = (0xFF & reply[8]) | ((0xFF & reply[9]) << 8);
      inputValues.normalizedADValue = (0xFF & reply[10]) | ((0xFF & reply[11]) << 8);
      inputValues.scaledValue = (short)((0xFF & reply[12]) | (reply[13] << 8));
      inputValues.calibratedValue = (short)((0xFF & reply[14]) | (reply[15] << 8));
      //if (debugLevel >= DEBUG_LEVEL_MEDIUM)
      //{
      //  DebugConsole.show("DEBUG: getInputValues() returned:");
      //  inputValues.printValues();
      //}
    }

    return inputValues;
  }



    public void sendMessage(byte [] msg) throws IOException {
		if(out !=null) {
			out.write(msg.length & 0xff);
			out.write((msg.length >> 8) & 0xff);
			out.write(msg);
			out.flush();
		}
	}
	
	
	
	
}
