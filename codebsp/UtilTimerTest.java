
// UtilTimerTest.java
// Andrew Davison, April 2005, ad@fivedots.coe.psu.ac.th

/* Test the accuracy of the java.util timer used with a TimerTask
   by trying to repaint a JPanel at a given FPS (frames per second).
   Measure the actual FPS achieved.

   A frame in this application is one execution of run() in 
   the MyTimerTask class

   Usage:
     java UtilTimerTest <requested FPS>

   Average actual FPS Results
              20      50     80/83     100	
            ------------------------------
   Win 98):   20      47      81       99 
   Win 2000:  20      50      83       99
   Win XP:    20      50      83       95

   Slow at the start; gradually reaches the required FPS
   over 10-20 seconds.
   A garbage collection reduces the FPS for a few seconds.

  The numbers are the same if reportStats() uses System.currentTimeMillis()
  instead of J3DTimer.getValue() since MAX_STATS_INTERVAL is > than the
  timer's resolution.
*/

import java.awt.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;
import java.text.DecimalFormat;

import com.sun.j3d.utils.timer.J3DTimer;     // used to measure accuracy


public class UtilTimerTest 
{
  private static int DEFAULT_FPS = 80;

  public static void main(String args[]) 
  {
    int fps = DEFAULT_FPS;
    if (args.length != 0)
      fps = Integer.parseInt(args[0]);

    int period = (int) 1000.0/fps;     // in ms
    System.out.println("fps: " + fps + "; period: " + period + " ms");

    PaintPanel pp = new PaintPanel(period);

    // create a JFrame to hold the timer test JPanel
    JFrame app = new JFrame("Utilities Timer Test");
    app.getContentPane().add(pp, BorderLayout.CENTER);
    app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    MyTimerTask task = new MyTimerTask(pp);
    Timer t = new Timer();
    t.scheduleAtFixedRate(task, 0, period);  
           // execute every period ms (at a fixed rate)

     app.pack();
     app.setResizable(false);  
     app.setVisible(true);
  } // end of main()

} // end of UtilTimerTest class


//-------------------------------------------------

class MyTimerTask extends TimerTask 
{
  private PaintPanel pp;


  public MyTimerTask(PaintPanel pp) 
  { this.pp = pp;  } 


  public void run()
  // update, render
  { sillyTask();
    pp.repaint();
  } // end of run()


  private void sillyTask()
  { long tot = 0;
    for (long i=0; i < 200000L; i++)
      tot += i;
    // System.out.println("tot: " + tot);
  } // end of sillyTask()

} // end of myTimerTask class


//------------------------------------------------------

class PaintPanel extends JPanel 
{
  private static final int PWIDTH = 200;
  private static final int PHEIGHT = 75;

  private static long MAX_STATS_INTERVAL = 1000L;
    // record stats every 1 second (roughly)

  private static int NUM_FPS = 10;
     // number of FPS values stored to get an average


  // used for gathering statistics
  private long statsInterval = 0L;    // in ms
  private long prevStatsTime;   
  private long totalElapsedTime = 0L;

  private long frameCount = 0;
  private double fpsStore[];
  private long statsCount = 0;
  private double averageFPS = 0.0;


  private DecimalFormat df = new DecimalFormat("0.##");  // 2 dp
  private DecimalFormat timedf = new DecimalFormat("0.####");  // 4 dp

  private int period;       // period between drawing in _ms_
              // i.e. the time requested for one frame iteration


  public PaintPanel(int p)
  {
    period = p;

    setBackground(Color.white);
    setPreferredSize( new Dimension(PWIDTH, PHEIGHT) );

    fpsStore = new double[NUM_FPS];
    for (int i=0; i < NUM_FPS; i++)
      fpsStore[i] = 0.0;

    prevStatsTime = J3DTimer.getValue();
  } // end of PaintPanel()


   public void paintComponent(Graphics g)
   {
     super.paintComponent(g);

     // clear the background
     g.setColor(Color.white);
     g.fillRect (0, 0, PWIDTH, PHEIGHT);

    // report average FPS
	 g.setColor(Color.black);
	 g.drawString("Average FPS: " + df.format(averageFPS), 10, 25);

    reportStats();
   } // end of paintComponent()


  private void reportStats()
  /* The statistics:
       - the summed periods for all the iterations in this interval 
         (period is the amount of time a single frame iteration should take),
         the actual elapsed time in this interval, 
         the error between these two numbers;

       - the total number of calls to paintComponent();

       - the FPS (frames/sec) for this interval, and the average 
         FPS over the last NUM_FPSs intervals.

     The data is collected every MAX_STATS_INTERVAL  (1 sec).
  */
  { 
    frameCount++;
    statsInterval += period;

    if (statsInterval >= MAX_STATS_INTERVAL) {     // record stats every MAX_STATS_INTERVAL
      long timeNow = J3DTimer.getValue();

      long realElapsedTime = timeNow - prevStatsTime;   // time since last stats collection
      totalElapsedTime += realElapsedTime;

      long sInterval = (long)statsInterval*1000000L;  // ms --> ns
      double timingError = 
          ((double)(realElapsedTime - sInterval)) / sInterval * 100.0;

      double actualFPS = 0;     // calculate the latest FPS
      if (totalElapsedTime > 0)
        actualFPS = (((double)frameCount / totalElapsedTime) * 1000000000L);

      // store the latest FPS
      fpsStore[ (int)statsCount%NUM_FPS ] = actualFPS;
      statsCount = statsCount+1;

      double totalFPS = 0.0;     // total the stored FPSs
      for (int i=0; i < NUM_FPS; i++)
        totalFPS += fpsStore[i];

      if (statsCount < NUM_FPS)  // obtain the average FPS
        averageFPS = totalFPS/statsCount;
      else
        averageFPS = totalFPS/NUM_FPS;

      System.out.println(timedf.format( (double) statsInterval/1000) + " " + 
                        timedf.format((double) realElapsedTime/1000000000L) + "s " + 
						df.format(timingError) + "% " + 
                        frameCount + "c " +
                        df.format(actualFPS) + " " +
                        df.format(averageFPS) + " afps" );

      prevStatsTime = timeNow;
      statsInterval = 0L;   // reset
    }
  }  // end of reportStats()


} // end of PaintPanel class

