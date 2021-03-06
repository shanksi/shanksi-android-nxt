// DebugConsole.java, Java SE version
// Platform (Java SE, ME) dependent code
// Should be visible in package only. Not included in Javadoc

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

package uk.co.shanksi.nxt.platform;

import android.util.Log;

public class DebugConsole 
{
  public static void show(String msg)
  {
    System.out.println(msg);
	Log.d("NXT", msg);
  }

  public static void showTimed(String msg, int displayTime)
  {
    System.out.println(msg);
  }
}
