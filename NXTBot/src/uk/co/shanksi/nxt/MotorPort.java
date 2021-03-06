// MotorPort.java

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
 * Useful declarations for motor port connections.
 */
public class MotorPort extends Port
{
  /**
   * Declaration used by a motor connected to port A.
   */
  public final static MotorPort A = new MotorPort(0, "A");

  /**
   * Declaration used by a motor connected to port B.
   */
  public final static MotorPort B = new MotorPort(1,"B");
  
  /**
   * Declaration used by a motor connected to port C.
   */
  public final static MotorPort C = new MotorPort(2,"C");

  private MotorPort(int portId, String label)
  {
    super(portId, label);
  }  
}
