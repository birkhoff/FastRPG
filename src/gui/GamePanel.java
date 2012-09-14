/**
 * Fullscreen FSEM Engine
 * 07. September 2012
 */
package gui;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import chars.*;
import engine.*;

enum State {
	INTRO, MENU, LOADLEVEL, RUN, PAUSE
}
enum Direction {
	N, NE, E, SE, S, SW, W, NW, NONE
}

public class GamePanel extends JFrame implements Runnable {
    private static int MAX_FRAME_SKIPS = 5; // was 2;
    private static final int NO_DELAYS_PER_YIELD = 16;
    private static final int NUM_BUFFERS = 2;
    private static int DEFAULT_FPS = 100;
    private static final long serialVersionUID = 7773333380423469665L;

    public static void main(String args[]) {
    	int fps = DEFAULT_FPS;
    	long period = (long) 1000.0 / fps;
        new GamePanel(period * 1000000L);
    }

    private Thread animator; // start thread who handles the animation
    private BufferStrategy bufferStrategy;
    private boolean finishedOff = false;
    private Font font;
    private long gameStartTime;
    private GraphicsDevice gd;
    private int mWidth;			// Aufloesung des Monitors
    private int mHeight;		// Aufloesung des Monitors 
    private Graphics2D gScr;
    private volatile boolean isPaused = false;
    private long period; // period between drawing in _nanosecs_
    private int pWidth, pHeight; // size of panel
    private volatile boolean running = false;
	private static int positionInMainMenu = 0;
	private State state = State.INTRO;

	// Alles zur Bewegung
	private Direction gotoDir = Direction.NONE;
	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;
	
	// Frames zaehlen
	private long firstFrame = 0;
	private long currentFrame = 0;
	private int frames = 0;
	private int fps = 0;
	
	// Variablen zum Verschieben des Hintergrundes
	private boolean drifting;
	private boolean driftUp;
	private boolean driftRight;
	private boolean driftDown;
	private boolean driftLeft;
	private int bgPosX = 0;
	private int bgPosY = 0;
	
	// Objects
	private Hero hero;
	private Map island;
	
	/**
	 * Kontruktor
	 */
    public GamePanel(long period) {
        super("Insane Engine!");
        addKeyListener(new GameListener());
        this.period = period;
        initFullScreen();
        readyForTermination();
        island = new Map("maps/TestMap1.tmx");
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

    /**
     * Menuehandling muss hier auch mit rein, da man auf die Graphics2D gScr 
     * zugreifen muss
     */
    private void gameRender(Graphics2D gScr) {
        // Hintergrund faerben
        gScr.setColor(Color.white);
        gScr.fillRect(0, 0, pWidth, pHeight);       
        gScr.setFont(font);
        gScr.setColor(Color.black);
    	switch(state) {
			case INTRO :	
				state = state.MENU;
				break;
			case MENU : 	
				new menu.MainMenu(gScr, positionInMainMenu, mWidth, mHeight);
				state = state.RUN;
				break;
			case LOADLEVEL: 
				break;
			case RUN :
				gScr.drawImage(island.getDrawnMap(),0,0,null);
				drawBackground(gScr);
				drawHero(gScr);	// get ya hero on the screen!
				break;
			case PAUSE :
				break;
    	}
    	gScr.drawString("FPS: "+fps, 20, 20);
    	gScr.drawString("Hero Position: x = "+hero.getPositionX()+", y = "+hero.getPositionY()+", Schrittgroesse: "+hero.getStepsize(), 20, 40);
    	gScr.drawString("Up: "+up, 20, 60);
    	gScr.drawString("Right: "+right, 20, 80);
    	gScr.drawString("Down: "+down, 20, 100);
    	gScr.drawString("Left: "+left, 20, 120);
    }
    
    /**
     * Gamethread starten
     */
    private void gameStart() {
        if (animator == null || !running) {
            animator = new Thread(this);
            animator.start();
        }
    } 

    /**
     * Hauptmethode zur Aktualisierung des Spiels
     */
    private void gameUpdate() {
        if (state == State.RUN) {
        	countFPS();
        	calcDrift();
        	moveHero();
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
        beforeTime = gameStartTime;
        running = true;
        hero = new Hero();	// create insane hero!
        
        while (running) {
            gameUpdate();
            screenUpdate();
            afterTime = System.nanoTime();
            timeDiff = afterTime - beforeTime;
            sleepTime = period - timeDiff - overSleepTime;
            if (sleepTime > 0) { // some time left in this cycle
                try {
                    Thread.sleep(sleepTime / 1000000L); // nano -> ms
                } catch (InterruptedException ex) {}
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
        }
        finishOff();
    } 

    private void readyForTermination() {
        addKeyListener(new KeyAdapter() {
            // listen for esc, q, end, ctrl-c on the canvas to
            // allow a convenient exit from the full screen configuration
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_Q || keyCode == KeyEvent.VK_END || keyCode == KeyEvent.VK_C
                        && e.isControlDown()) {
                    running = false;
                }
            }
        });
        // for shutdown tasks
        // a shutdown may not only come from the program
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                running = false;
                finishOff();
            }
        });
    }
    
    private void screenUpdate() {
        try {
            gScr = (Graphics2D) bufferStrategy.getDrawGraphics();
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
        mWidth = dm.getWidth();
        mHeight = dm.getHeight();
        System.out.println("Display Modus: (" + mWidth + "," + mHeight + "," + dm.getBitDepth() + "," + dm.getRefreshRate()+")  ");
    }
    
    public static void setPositionInMainMenu(int pos) {
    	positionInMainMenu = pos;
    }
    
    private void countFPS() {
    	frames++;
    	currentFrame = System.currentTimeMillis();
    	if(currentFrame > firstFrame + 1000){
    		firstFrame = currentFrame;
    		fps = frames;
    		frames = 0;
    	}
    }
    
    /******************** Draw Methoden *********************/
    private void drawHero(Graphics2D g) {
    	if (state == State.RUN) {
    		g.drawImage(hero.getImage(), (int)hero.getPositionX(), (int)hero.getPositionY(), null);
    	}
    }
    private void drawBackground(Graphics2D g) {
    	if (state == State.RUN) {    		
//    		try {
////    			BufferedImage konzeptBG;
////    			konzeptBG = ImageIO.read(new File("images/konzeptBG.png"));
////    			g.drawImage(konzeptBG, bgPosX, bgPosY, null);
//    		} catch (IOException e) {
//    			e.printStackTrace();
//    		}
    		driftUp = false;
    		driftRight = false;
    		driftDown = false;
    		driftLeft = false;
    	}
    }
    /**
     * Soll den Untergrund verschieben, wenn der Held an die Kante
     * des Bildschirms kommt.
     */
    private void calcDrift() {
    	if (state == State.RUN) {
	    	float drift = 0.2f;		// Setze 20% Kante zum scrollen
	    	// Oberer und unterer Rand
	    	if (hero.getPositionY() < mHeight*drift && up) {
	    		drifting = true;
	    		driftUp = true;
	    		bgPosY += (int) hero.getStepsize();
	    	} else if (hero.getPositionY() >= mHeight - (mHeight*drift) && down) {
	    		drifting = true;
	    		driftDown = true;
	    		bgPosY += (int) ((-1)*hero.getStepsize());
	    	} 
	    	if (hero.getPositionX() < (mWidth*drift) && left) {
	    		drifting = true;
	    		driftLeft = true;
	    		bgPosX += (int) hero.getStepsize();
	    	} else if (hero.getPositionX() >= mWidth - (mWidth*drift) && right) {
	    		drifting = true;
	    		driftRight = true;
	    		bgPosX += (int) ((-1)*hero.getStepsize());
	    	}
    	}
    }
    /******************** /Draw Methoden/ *********************/
    
    /**
     * Bewege den Helden!
     */
    private void moveHero() {
		if (state == State.RUN) {
			float step = hero.getStepsize();
			boolean gone = false;		
			
    		if (up && right) {
				gone = true;
				float dia = (float) (Math.sqrt(2*(step*step))/2);
				hero.setPositionX(hero.getPositionX()+dia);
				hero.setPositionY(hero.getPositionY()-dia);    				
    		} else if (right && down) {
    			float dia = (float) (Math.sqrt(2*(step*step))/2);
    			hero.setPositionX(hero.getPositionX()+dia);
    			hero.setPositionY(hero.getPositionY()+dia);
    			gone = true;
    		} else if (down && left) {
    			float dia = (float) (Math.sqrt(2*(step*step))/2);
    			hero.setPositionX(hero.getPositionX()-dia);
    			hero.setPositionY(hero.getPositionY()+dia);
    			gone = true;
    		} else if (left && up) {
    			float dia = (float) (Math.sqrt(2*(step*step))/2);
    			hero.setPositionX(hero.getPositionX()-dia);
    			hero.setPositionY(hero.getPositionY()-dia);
    			gone = true;
    		} 
    		else if (up && !gone && !driftUp)
    			hero.setPositionY(hero.getPositionY()-hero.getStepsize());
    		else if (right && !gone && !driftRight)
    			hero.setPositionX(hero.getPositionX()+hero.getStepsize());
    		else if (down && !gone && !driftDown)
    			hero.setPositionY(hero.getPositionY()+hero.getStepsize());
    		else if (left && !gone && !driftLeft)
    			hero.setPositionX(hero.getPositionX()-hero.getStepsize());
    	}
    }
    /**
     * Alles moegliche, um Tastatureingaben zu lesen. 
     */
    class GameListener implements KeyListener {
    	public void keyPressed(KeyEvent e) {
    		if (state == State.RUN) {
	    		switch (e.getKeyCode()) {
	    			case 37: left = true; break;
	    			case 38: up = true; break;
	    			case 39: right = true; break;
	    			case 40: down = true; break;
	    			default: break;
	    		}
        	}
    	}
		public void keyReleased(KeyEvent e) {
			if (state == State.RUN) {
	    		switch (e.getKeyCode()) {
	    			case 37: left = false; break;
	    			case 38: up = false; break;
	    			case 39: right = false; break;
	    			case 40: down = false; break;
	    			default: break;
	    		}
        	}
		}
		@Override
		public void keyTyped(KeyEvent e) {}
    }
}