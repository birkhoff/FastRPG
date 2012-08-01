
// TimerRes.java
// Andrew Davison, April 2005, ad@fivedots.psu.ac.th

/* Measure the resolution of System.currentTimeMillis(),
   the Java 3D timer, the sun.misc.Perf counter, and the J2SE 5.0
   nano timer.

   Results
   -------
   Win 98
      Java 3D Timer Resolution: 838 nsecs
      System Time resolution: 55000 microsecs
      Perf resolution: 5866 nsecs;  Perf Time: 20114 nsecs
      Nano Time resolution: 5657 nsecs

   Win 2000
      Java 3D Timer Resolution: 838 nsecs
      System Time resolution: 10000 microsecs
      Perf resolution: 6076 nsecs;  Perf Time: 38552 nsecs

   Win XP
      Java 3D Timer Resolution: 279 nsecs
      System Time resolution: 15500 microsecs
      Perf resolution: 1257 nsecs;  Perf Time: 10476 nsecs

*/

import com.sun.j3d.utils.timer.J3DTimer;  // requires Java 3D



public class TimerRes
{
  public static void main(String args[])
  { j3dTimeResolution();
    sysTimeResolution();
    perfTimeResolution();
    nanoTimeResolution();
  }
 
  private static void j3dTimeResolution()
  { System.out.println("Java 3D Timer Resolution: " + 
                    J3DTimer.getResolution() + " nsecs");
  }

  private static void sysTimeResolution() 
  {
    long total, count1, count2;

    count1 = System.currentTimeMillis();
    count2 = System.currentTimeMillis();
    while(count1 == count2)
      count2 = System.currentTimeMillis();
    total = 1000L * (count2 - count1);

    count1 = System.currentTimeMillis();
    count2 = System.currentTimeMillis();
    while(count1 == count2)
      count2 = System.currentTimeMillis();
    total += 1000L * (count2 - count1);

    count1 = System.currentTimeMillis();
    count2 = System.currentTimeMillis();
    while(count1 == count2)
      count2 = System.currentTimeMillis();
    total += 1000L * (count2 - count1);

    count1 = System.currentTimeMillis();
    count2 = System.currentTimeMillis();
    while(count1 == count2)
      count2 = System.currentTimeMillis();
    total += 1000L * (count2 - count1);

    System.out.println("System Time resolution: " + total/4 + " microsecs");
  } // end of sysTimeResolution()


  private static void perfTimeResolution()
  {
    StopWatch sw = new StopWatch();
    System.out.println("Perf Resolution: " + 
                    sw.getResolution() + " nsecs");

    sw.start();
    long time = sw.stop();
    System.out.println("Perf Time " + time  + " nsecs");
  } // end of perfTimeResolution()


  private static void nanoTimeResolution() 
  // Note: System.nanoTime() is only available in J2SE 5.0 or later
  {
    long total, count1, count2;

    count1 = System.nanoTime();
    count2 = System.nanoTime();
    while(count1 == count2)
      count2 = System.nanoTime();
    total = (count2 - count1);

    count1 = System.nanoTime();
    count2 = System.nanoTime();
    while(count1 == count2)
      count2 = System.nanoTime();
    total += (count2 - count1);

    count1 = System.nanoTime();
    count2 = System.nanoTime();
    while(count1 == count2)
      count2 = System.nanoTime();
    total += (count2 - count1);

    count1 = System.nanoTime();
    count2 = System.nanoTime();
    while(count1 == count2)
      count2 = System.nanoTime();
    total += (count2 - count1);

    System.out.println("Nano Time resolution: " + total/4 + " ns");
  } // end of nanoTimeResolution()

} // end of TimerRes class
