
// SleepAcc.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Measure the accuracy of sleep() using the Java 3D timer.
*/


import java.text.DecimalFormat;
import com.sun.j3d.utils.timer.J3DTimer;


/*  Example output:

Win 98
Slept: 1000 ms  J3D: 1002.89 ms  err: -0.29 %
Slept: 500 ms  J3D: 496.62 ms  err: 0.68 %
Slept: 200 ms  J3D: 199.73 ms  err: 0.14 %
Slept: 100 ms  J3D: 98.86 ms  err: 1.15 %
Slept: 50 ms  J3D: 50.6 ms  err: -1.19 %
Slept: 20 ms  J3D: 24.74 ms  err: -19.16 %
Slept: 10 ms  J3D: 14.75 ms  err: -32.19 %
Slept: 5 ms  J3D: 5.79 ms  err: -13.59 %
Slept: 1 ms  J3D: 0.31 ms  err: 219.92 %

Win 2000
Slept: 1000 ms  J3D: 995.95 ms  err: 0.41 %
Slept: 500 ms  J3D: 497.59 ms  err: 0.48 %
Slept: 200 ms  J3D: 199.21 ms  err: 0.4 %
Slept: 100 ms  J3D: 99.11 ms  err: 0.9 %
Slept: 50 ms  J3D: 49.11 ms  err: 1.82 %
Slept: 20 ms  J3D: 19.95 ms  err: 0.25 %
Slept: 10 ms  J3D: 8.97 ms  err: 11.45 %
Slept: 5 ms  J3D: 18.24 ms  err: -72.59 %
Slept: 1 ms  J3D: 18.86 ms  err: -94.7 %

  But there are large variations from run to run, from
  OS to OS, and machine to machine.
*/


public class SleepAcc
{
  private static DecimalFormat df;

  public static void main(String args[])
  {
    df = new DecimalFormat("0.##");  // 2 dp

    // test various sleep values
    sleepTest(1000);
    sleepTest(500);
    sleepTest(200);
    sleepTest(100);
    sleepTest(50);
    sleepTest(20);
    sleepTest(10);
    sleepTest(5);
    sleepTest(1);
  } // end of main()


  private static void sleepTest(int delay)
  {
    long timeStart = J3DTimer.getValue();

    try {
      Thread.sleep(delay);
    }
    catch(InterruptedException e) {}

    double timeDiff = ((double)(J3DTimer.getValue() - timeStart))/(1000000L);
    double err = ((delay - timeDiff)/timeDiff) * 100;

    System.out.println("Slept: " + delay + " ms  J3D: " + 
                          df.format(timeDiff) + " ms  err: " +
                          df.format(err) + " %" );
  }  // end of sleepTest()

} // end of SleepAcc class
