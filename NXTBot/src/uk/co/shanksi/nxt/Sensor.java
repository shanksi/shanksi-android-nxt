// Sensor.java

/*
This software is part of the NxtJLib library.
It is Open Source Free Software, so you may
- run the code for any purpose
- study how the code works and adapt it to your needs
- integrate all or parts of the code in your own programs
- redistribute copies of the code
- improve the code and release your improvements to the public
However the use of the code is entirely your responsibility.
*/

package uk.co.shanksi.nxt;
import java.io.*;

// import ch.aplu.nxt.platform.ShowError;

/**
 * Abstract class as ancestor of all sensors.
 */
public abstract class Sensor extends Part
{
  private SensorPort port;
  private int portId;

  protected Sensor(SensorPort port)
  {
    this.port = port;
    portId = port.getId();
  }

  protected SensorPort getPort()
  {
    return port;
  }

  protected int getPortId()
  {
    return portId;
  }

  protected String getPortLabel()
  {
    return port.getLabel();
  }

  protected void setTypeAndMode(int type, int mode)
  {
    brick.setInputMode(portId, type, mode);
  }

  protected boolean readBooleanValue()
  {
    InputValues vals = brick.getInputValues(portId);
    // I thought open sensor would produce 0 value. My UWORD conversion wrong?
    return (vals.rawADValue < 500);
  }

  protected int readRawValue()
  {
    InputValues vals = brick.getInputValues(portId);
    return vals.rawADValue;
  }

  protected int readNormalizedValue()
  {
    InputValues vals = brick.getInputValues(portId);
    return vals.normalizedADValue;
  }

  protected int readScaledValue()
  {
    InputValues vals = brick.getInputValues(portId);
    return vals.scaledValue;
  }

  protected byte[] LSRead(byte portId)
  {
    byte[] request = {
		CommandType.DIRECT_COMMAND_REPLY, 
		NxtCommand.LS_READ, 
		portId
	};
    byte[] reply = brick.requestData(request);

    if (reply == null)  // Error
      return null; 
    
    byte rxLength = reply[3];
    byte[] rxData = new byte[rxLength];
    if (reply[2] == 0)
    {
      System.arraycopy(reply, 4, rxData, 0, rxLength);
      return rxData;
    }
    return null;  // Error
  }

  protected void LSWrite(byte portId, byte[] txData, byte rxDataLength)
  {
    byte[] request = {
		CommandType.DIRECT_COMMAND_NOREPLY, 
		NxtCommand.LS_WRITE, 
		portId, 
		(byte)txData.length, 
		rxDataLength
	};
    request = appendBytes(request, txData);
	try {
        brick.sendMessage(request);
	} catch(IOException ex) {
		// really should consider doing sonething here
	}
	//robot.sendData(request);
  }

  private byte[] appendBytes(byte[] array1, byte[] array2)
  {
    byte[] array = new byte[array1.length + array2.length];
    System.arraycopy(array1, 0, array, 0, array1.length);
    System.arraycopy(array2, 0, array, array1.length, array2.length);
    return array;
  }
}

