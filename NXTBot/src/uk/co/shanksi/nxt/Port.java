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
public class Port
{
  private int portId;
  private String label;

  protected Port(int portId, String label)
  {
    this.portId = portId;
    this.label = label;
  }  
  
  protected int getId()
  {
    return portId;
  }  
  
  protected String getLabel()
  {
    return label;
  }  
}
