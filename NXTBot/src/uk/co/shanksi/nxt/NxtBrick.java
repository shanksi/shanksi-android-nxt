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
        byte[] request = {
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
            sendMessage(request);
		} 
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

    /**
     * Reads the data returned as a reply from the brick.
     * @return the brick's reply
     */
    public synchronized byte[] readData()
    {
        byte[] reply = null;
        int length = -1;
        int lenMSB;
        int lenLSB;

        try
        {
            do
                lenLSB = in.read();
            while (lenLSB < 0);

            lenMSB = in.read(); // MSB of reply length
            length = (0xFF & lenLSB) | ((0xFF & lenMSB) << 8);

            reply = new byte[length];
            // Rest of packet
            in.read(reply);
        }
        catch (IOException ex)
        {
//      System.out.println("read error");
        }

      //  if (debugLevel == DEBUG_LEVEL_HIGH)
    //    {
 /*     DebugConsole.show("DEBUG: readData() returned:");
      for (int i = 0; i
        < reply.length; i++)
        DebugConsole.show("  " + reply[i]);
    *///}

        return reply;
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
      inputValues.rawADValue = (reply[8]) | ((reply[9]) << 8);
      inputValues.normalizedADValue = (reply[10]) | ((reply[11]) << 8);
      inputValues.scaledValue = (short)((reply[12]) | (reply[13] << 8));
      inputValues.calibratedValue = (short)((reply[14]) | (reply[15] << 8));
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
