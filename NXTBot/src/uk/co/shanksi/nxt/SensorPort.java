// SensorPort.java

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

/**
 * Useful declarations for sensor port connections.
 */
public class SensorPort extends Port
{
  /**
   * Declaration used by a sensor connected to port S1.
   */
  public final static SensorPort S1 = new SensorPort(0, "S1");

  /**
   * Declaration used by a sensor connected to port S2.
   */
  public final static SensorPort S2 = new SensorPort(1, "S2");
  
  /**
   * Declaration used by a sensor connected to port S3.
   */
  public final static SensorPort S3 = new SensorPort(2, "S3");
  
  /**
   * Declaration used by a sensor connected to port S4.
   */
  public final static SensorPort S4 = new SensorPort(3,"S4");

    private SensorPort(int portId, String label)
    {
        super(portId, label);
    }
}
