/**
 * Hoffentlich lauffaehige Engine mit Vollbildmodus FSEM
 * 07. September 2012
 */

package gui;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

public class Engine extends JFrame implements Runnable {
    private static int MAX_FRAME_SKIPS = 5; // was 2;
    private static final int NO_DELAYS_PER_YIELD = 16;
    private static final int NUM_BUFFERS = 2;
    private static final long serialVersionUID = 7773333380423469665L;

    public static void main(String args[]) {
        System.out.println("Lalala");
    }

    private Thread animator; // Der Thread der die Animation behandelt
    private BufferStrategy bufferStrategy;
    private boolean finishedOff = false;
    private Font font;
    private volatile boolean gameOver = false;
    private long gameStartTime;
    private GraphicsDevice gd;
    private Graphics gScr;
    private volatile boolean isPaused = false;
    private long period; // period between drawing in _nanosecs_
    private int pWidth, pHeight; // Groesse des Panels einstellen
    private volatile boolean running = false; // Beende Animation
	private long prevStatsTime;
	private int boxesUsed;
	private int framesSkipped;

    public Engine(long period) {
        super("Insane Engine!");
        this.period = period;
        initFullScreen();
        gameStart();
    }

    /**
     * Bevor das Spiel komplett beendet wird, werden noch diese Aufgaben ausgefuehrt
     */
    private void finishOff() {
        if (!finishedOff) {
            finishedOff = true;
            restoreScreen();
            System.exit(0);
        }
    }

    private void gameRender(Graphics gScr) {
        // clear the background
        gScr.setColor(Color.white);
        gScr.fillRect(0, 0, pWidth, pHeight);
        gScr.setColor(Color.blue);
        gScr.setFont(font);
        gScr.setColor(Color.black);
        if (gameOver) {
            System.out.println("Spiel zu Ende");
        }
    }

    private void gameStart() {
        if (animator == null || !running) {
            animator = new Thread(this);
            animator.start();
        }
    } 

    private void gameUpdate() {
        if (!isPaused && !gameOver) {
            /** Hier Bewegungskram reinpacken*/
        }
    } 

    /**
     * Aktiviere Fullscreen
     */
    private void initFullScreen() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        gd = ge.getDefaultScreenDevice();
        setUndecorated(true);
        setIgnoreRepaint(true);
        setResizable(false);
        if (!gd.isFullScreenSupported()) {
            System.out.println("Vollbildmodus konnte nicht aktiviert werden!");
            System.exit(0);
        }
        gd.setFullScreenWindow(this);
        showCurrentMode();
        pWidth = getBounds().width;
        pHeight = getBounds().height;
        setBufferStrategy();
    }

    /**
     * Vollbildmodus verlassen und ggf. alte Aufloesung wiederherstellen
     */
    private void restoreScreen() {
        Window w = gd.getFullScreenWindow();
        if (w != null) {
            w.dispose();
        }
        gd.setFullScreenWindow(null);
    }

    public void run() {
        long beforeTime, afterTime, timeDiff, sleepTime;
        long overSleepTime = 0L;
        int noDelays = 0;
        long excess = 0L;
        gameStartTime = System.nanoTime();
        prevStatsTime = gameStartTime;
        beforeTime = gameStartTime;
        running = true;
        while (running) {
            gameUpdate();
            screenUpdate();
            afterTime = System.nanoTime();
            timeDiff = afterTime - beforeTime;
            sleepTime = period - timeDiff - overSleepTime;
            if (sleepTime > 0) { // some time left in this cycle
                try {
                    Thread.sleep(sleepTime / 1000000L); // nano -> ms
                } catch (InterruptedException ex) {
                }
                overSleepTime = System.nanoTime() - afterTime - sleepTime;
            } else { 
                excess -= sleepTime; 
                overSleepTime = 0L;
                if (++noDelays >= NO_DELAYS_PER_YIELD) {
                    Thread.yield(); 
                    noDelays = 0;
                }
            }
            beforeTime = System.nanoTime();
            int skips = 0;
            while (excess > period && skips < MAX_FRAME_SKIPS) {
                excess -= period;
                gameUpdate(); // update state but don't render
                skips++;
            }
            framesSkipped += skips;
        }
        finishOff();
    } 

    private void screenUpdate() {
        try {
            gScr = bufferStrategy.getDrawGraphics();
            gameRender(gScr);
            gScr.dispose();
            if (!bufferStrategy.contentsLost()) {
                bufferStrategy.show();
            } else {
                System.out.println("Contents Lost");
            }
            Toolkit.getDefaultToolkit().sync();
        } catch (Exception e) {
            e.printStackTrace();
            running = false;
        }
    }

    public void setBoxNumber(int no) {
        boxesUsed = no;
    }

    private void setBufferStrategy() {
        try {
            EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    createBufferStrategy(NUM_BUFFERS);
                }
            });
        } catch (Exception e) {
            System.out.println("Buffer-Strategie konnte nicht angewendet werden.");
            System.exit(0);
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
        }
        bufferStrategy = getBufferStrategy();
    }

    private void showCurrentMode() {
        DisplayMode dm = gd.getDisplayMode();
        System.out.println("Display Modus: (" + dm.getWidth() + "," + dm.getHeight() + "," + dm.getBitDepth() + "," + dm.getRefreshRate()
                + ")  ");
    }
}