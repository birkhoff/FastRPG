/**
 * Fullscreen FSEM Engine
 * 07. September 2012
 */
package gui;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
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
import java.util.LinkedList;

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
    private static final long serialVersionUID = 7773333380423469665L;

    private Thread animator; // start thread who handles the animation
    private BufferStrategy bufferStrategy;
    private boolean finishedOff = false;
    private Font font;
    private long gameStartTime;
    private GraphicsDevice gd;
    private int mWidth;			// Aufloesung des Monitors
    private int mHeight;		// Aufloesung des Monitors 
    private Graphics2D gScr;
    private long period; // period between drawing in _nanosecs_
    private int pWidth, pHeight; // size of panel
    private volatile boolean running = false;
	private static int positionInMainMenu = 0;
	private State state = State.INTRO;
	private boolean debugMode = true;
	private boolean turboMode = false;

	// Alles zur Bewegung
	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;
	private boolean slash;
	private boolean initSlash;
	
	// Frames zaehlen
	private long firstFrame = 0;
	private long currentFrame = 0;
	private int frames = 0;
	private int fps = 0;
	
	// Variablen zum Verschieben des Hintergrundes
	private boolean driftUp;
	private boolean driftRight;
	private boolean driftDown;
	private boolean driftLeft;
	private int MapPosX = 0;
	private int MapPosY = 0;
	
	// Objects
	private Hero hero;
	private Map island;
	
	// Action-Flags
	private boolean drawActionButtonFlag = false;
	private String conversationText = "Debug kills kittens";
	private boolean drawConversation = false;
	
	//Rolling!
	private boolean roll = false;
	private float pixelsrolled = 0;
	private float oldstepsize = 3f;
	
	//HUD
	private boolean HUDflag = true;
	int hudx;
	int hudy;
	
	// Dont know
	private float tolerance;	// Tolerance of 1 Pixel for drifting the map
	private int countFramesForAttackingHero = 0;
	
	/**
	 * Kontruktor
	 */
    public GamePanel(long period) {
        super("Insane Engine!");
        addKeyListener(new GameListener());
        this.period = period;
        initFullScreen();
        readyForTermination();
        loadMap("maps/TestMap1.tmx");
        hudx = this.getWidth()/2-250;
        hudy = this.getHeight()-100;
        gameStart();
    }
    
    public GamePanel(long period, String pathToTMX){
    	super("Insane Engine!");
        addKeyListener(new GameListener());
        this.period = period;
        initFullScreen();
        readyForTermination();
        loadMap(pathToTMX);
        hudx = this.getWidth()/2-250;
        hudy = this.getHeight()-100;
        gameStart();
    }

    /**
     * Menuehandling muss hier auch mit rein, da man auf die Graphics2D gScr 
     * zugreifen muss
     */
    private void gameRender(Graphics2D gScr) {
    	countFramesForAttackingHero++;
        // Hintergrund faerben
        gScr.setColor(Color.black);
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
			case LOADLEVEL : 
				gScr.setColor(Color.RED);
				gScr.fillRect(0, 0, pWidth, pHeight);
				BufferedImage foo = null;
				try {
					foo = ImageIO.read(new File("images/angela_merkel.jpg"));
				} catch (IOException e) {
					e.printStackTrace();
				}
				gScr.drawImage(foo, 400, 400, null);
				break;
			case RUN :
				drawBackground(gScr);
				drawMobs(gScr);
				drawNPCs(gScr);
				drawHUD(gScr);
				drawHero(gScr);	// get ya hero on the screen!
				break;
			case PAUSE :
				break;
    	}
    	if (debugMode) {
    		int temp = 1;
    		gScr.setFont(font);
    		gScr.setColor(Color.black);
	    	gScr.drawString("FPS: "+fps, 20, 20);
	    	gScr.drawString("Hero: HP = "+hero.getHp()+", Position: x = "+hero.getPositionX()+", y = "+hero.getPositionY(), 20, 40);
	    	gScr.drawString("Map: x = "+getMapPosX()+", y = "+getMapPosY(), 20, 60);
	    	gScr.drawString("Keyboard: up: "+up+", right: "+right+", down: "+down+", left: "+left, 20, 80);
	    	gScr.drawString("Sword: slash: "+slash+", x: "+hero.getSword().getX()+", y: "+hero.getSword().getY()+", damage: "+hero.getSword().getDamage(), 20, 100);
	    	gScr.drawString("Turbomode: "+turboMode, 20, 120);
	    	/////////////////////////////////////////// Rechte Seite //////////////////////////////////////////////////////////
	    	LinkedList<Mob> Mobs = AssetCreator.getMobs();
	    	if (!Mobs.isEmpty()) {
		    	gScr.drawString("Mobs",mWidth-300,20*temp);
		    	temp++;
	        	for (int i = 0; i < Mobs.size(); i++) {
	    			Mob mob = Mobs.get(i);
	    			gScr.drawString("Mob "+temp+": x = "+mob.getPositionX()+", y = "+mob.getPositionY(),mWidth-300,20*temp);
	    			gScr.drawString("IsHeroInRange: "+mob.isHeroInRange(), mWidth-300, 20*(temp+1));
	    			temp += 2;
	    		}
	    	}
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
        	LinkedList<Mob> Mobs = AssetCreator.getMobs();
        	for (int i = 0; i < Mobs.size(); i++) {
    			Mob mob = Mobs.get(i);
    			moveMob(mob);
    		}
        }
    } 

	public void run() {
        long beforeTime, afterTime, timeDiff, sleepTime;
        long overSleepTime = 0L;
        int noDelays = 0;
        long excess = 0L;
        gameStartTime = System.nanoTime();
        beforeTime = gameStartTime;
        running = true;
        hero = new Hero(mWidth/2, mHeight/2);	// create insane hero!
        while (running) {
            gameUpdate();
            screenUpdate();
            tolerance = hero.getStepsize();
            
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

    
    
    /******************** Draw Methoden *********************/
    private void drawHero(Graphics2D g) {
    	if (state == State.RUN) {
    		g.drawImage(hero.getImage(), (int)hero.getPositionX(), (int)hero.getPositionY(), null);
    		if(slash) {
    			hero.updateStrike(1);
    			g.drawImage(hero.getSword().getImage(), (int)hero.getSword().getX(), (int)hero.getSword().getY(), null);
    			
    		}
    		if (debugMode) {
    			g.setColor(Color.BLACK);
    			g.drawLine((int)(hero.getPositionX()+hero.getWidth()*0.25), (int)(hero.getPositionY()+hero.getHeight()*0.9), (int)(hero.getPositionX()+hero.getWidth()*0.6), (int)(hero.getPositionY()+hero.getHeight()*0.9));
    			g.drawLine((int)(hero.getPositionX()+hero.getWidth()*0.25), (int)(hero.getPositionY()), (int)(hero.getPositionX()+hero.getWidth()*0.6), (int)(hero.getPositionY()));
    			g.drawLine((int)(hero.getPositionX()+hero.getWidth()*0.6), (int)(hero.getPositionY()), (int)(hero.getPositionX()+hero.getWidth()*0.6), (int)(hero.getPositionY()+hero.getHeight()*0.9));
				g.drawLine((int)(hero.getPositionX()+hero.getWidth()*0.25), (int)(hero.getPositionY()), (int)(hero.getPositionX()+hero.getWidth()*0.25), (int)(hero.getPositionY()+hero.getHeight()*0.9));
			}
    	}
    }
    private void drawBackground(Graphics2D g) {
    	if (state == State.RUN) {
    		g.drawImage(island.getDrawnMap(), getMapPosX(), getMapPosY(), null);
    		driftUp = false;
    		driftRight = false;
    		driftDown = false;
    		driftLeft = false;
    	}
    }
    private void drawMobs(Graphics g) {
    	if (state == State.RUN) {
    		LinkedList<Mob> Mobs = AssetCreator.getMobs();
    		for (int i = 0; i < Mobs.size(); i++) {
    			Mob mob = Mobs.get(i);
    			g.drawImage(mob.getImage(), (int)mob.getPositionX()+MapPosX, (int)mob.getPositionY()+MapPosY, null);
    			if (debugMode) {
    				g.setColor(Color.red);
    				g.drawString(""+mob.getHp(), (int)mob.getPositionX()+MapPosX, (int)mob.getPositionY()+MapPosY);
    				g.setColor(Color.orange);
    				g.fillRect((int)mob.getPositionX()+mob.getWidth()/2+MapPosX-5, (int)mob.getPositionY()+mob.getHeight()/2+MapPosY-5, 10, 10);
    			}
    		}
    	}
    }
    
    private void drawNPCs(Graphics g){
    	if (state == State.RUN) {
    		LinkedList<NPC> npcs = AssetCreator.getNPCs();
    		for(int i=0; i< npcs.size(); i++){
    			NPC npc = npcs.get(i);
    			g.drawImage(npc.getImage(), (int)npc.getPositionX()+MapPosX, (int)npc.getPositionY()+MapPosY, null);
    		}
    	}
    }
    
    private void drawHUD(Graphics g){
    	if(HUDflag){
    		// Position of HUDBox
    		g.setColor(Color.BLACK);
    		//draw the box
    		g.fillRect(hudx, hudy, 500, 100);
    		//draw the stamina-bar
    		g.setColor(Color.GRAY);
    		g.fillRect(hudx+20, hudy+10, 50, 10);
    		g.setColor(Color.GREEN);
    		g.fillRect(hudx+20, hudy+10, (int)(50*hero.getStamina()), 10);
    		g.setColor(Color.BLACK);
    		g.drawLine(hudx+45, hudy+10, hudx+45, hudy+20);
        	if(drawActionButtonFlag){
        		g.setColor(Color.WHITE);
        		g.fillRect((int)hero.getPositionX()-80, (int)hero.getPositionY()+65, 100, 25);
        		g.setColor(Color.BLACK);
        		g.drawString("Action   [F]", (int)hero.getPositionX()-70, (int)hero.getPositionY()+85);
        	}
        	if(drawConversation){
        		g.setColor(Color.DARK_GRAY);
        		g.fillRect(0, mHeight-100, mWidth, 100);
        		g.setColor(Color.ORANGE);
        		g.drawString(conversationText, 20, mHeight-80);
        	}
    	}
    }
    /******************** /Draw Methoden/ *********************/
    /**
     * Soll den Untergrund verschieben, wenn der Held an die Kante
     * des Bildschirms kommt.
     */
    private void calcDrift() {
    	if (state == State.RUN) {
    		float drift = 0.25f;		// Setze 20% Kante zum scrollen
    		float[] center = Lib.getCenter(hero);
    		// Oberer und unterer Rand
    		if (hero.getPositionY() < mHeight*drift - center[1] && up &&
    							!island.isSolid((int)(hero.getPositionX()-MapPosX+hero.getWidth()*0.25),
    											(int)(hero.getPositionY()+hero.getHeight()*0.9-hero.getStepsize()-MapPosY)) &&
    							!island.isSolid((int)(hero.getPositionX()-MapPosX+hero.getWidth()*0.6),
    											(int)(hero.getPositionY()+hero.getHeight()*0.9-hero.getStepsize()-MapPosY))) {
    			if (getMapPosY()+tolerance < 0) {
    				driftUp = true;
    				setMapPosY(getMapPosY() + (int) hero.getStepsize());	
    			}
    		} else if (hero.getPositionY() >= mHeight - (mWidth*drift) - center[1] && down &&
    							!island.isSolid((int)(hero.getPositionX()-MapPosX+hero.getWidth()*0.25),
    											(int)(hero.getPositionY()+hero.getHeight()*0.9+hero.getStepsize()-MapPosY)) &&
								!island.isSolid((int)(hero.getPositionX()-MapPosX+hero.getWidth()*0.6),
												(int)(hero.getPositionY()+hero.getHeight()*0.9+hero.getStepsize()-MapPosY))) {
    			if (getMapPosY()-tolerance >= (-1)*(island.getHeight()-mHeight)) {
    				driftDown = true;
    				setMapPosY(getMapPosY() + (int) ((-1)*hero.getStepsize()));
    			}
    		} 
    		if (hero.getPositionX() < (mWidth*drift) - center[0] && left &&
    							!island.isSolid((int)(hero.getPositionX()-hero.getStepsize()-MapPosX+hero.getWidth()*0.25),
    											(int)(hero.getPositionY()+hero.getHeight()*0.9-MapPosY))) {
    			if (getMapPosX()+tolerance < 0) {
    				driftLeft = true;
    				setMapPosX(getMapPosX() + (int) hero.getStepsize());
    			}
    		} else if (hero.getPositionX() >= mWidth - (mWidth*drift) - center[0] && right &&
    							!island.isSolid((int)(hero.getPositionX()+hero.getStepsize()+hero.getWidth()*0.6-MapPosX),
    											(int)(hero.getPositionY()+hero.getHeight()*0.9-MapPosY))) {
    			if (getMapPosX()-tolerance >= (-1)*(island.getWidth()-mWidth)) {
    				driftRight = true;
    				setMapPosX(getMapPosX() + (int) ((-1)*hero.getStepsize()));
    			}
    		}
    	}
    }
    
    /**
     * Bewege den Helden!
     */
    private void moveHero() {
		if (state == State.RUN) {
			float step = hero.getStepsize();
			hero.regenerateStamina();
			boolean gone = false;		
			drawActionButtonFlag = false;
			
			if(up || down || right || left || roll){
				hero.setStanding(false);
			} else {
				hero.setStanding(true);
			}
			
			//Slashing
			if(initSlash == true){
				slash = true;
				hero.slash();
				initSlash = false;
			}
			if(slash == true){
				slash = hero.isSlash();

			}
			//Save the original stepsize
			if ( (up || down || right || left) && roll && pixelsrolled == 0){
				oldstepsize = hero.getStepsize();
				hero.setStepsize(hero.getStepsize()*3);
			}
			
			if ( (up || down || right || left) && roll && pixelsrolled < 100){
				pixelsrolled += hero.getStepsize();
			} else if (roll && pixelsrolled >= 100){
				System.out.println("Schluss mit rollen");
				roll = false;
				hero.setStepsize(oldstepsize);
				pixelsrolled = 0;
			}
			
			if (up && right) {
    			if (driftUp && driftRight) {}
    			else if (driftUp && hero.getPositionX() < mWidth-hero.getWidth() &&
    					!island.isSolid((int)(hero.getPositionX()+step+hero.getWidth()*0.6-MapPosX),
    									(int)(hero.getPositionY()+hero.getHeight()*0.9-MapPosY))) {
    				hero.setPositionX(hero.getPositionX()+step);
    			} else if (driftRight && hero.getPositionY() > 0 &&
    					!island.isSolid((int)(hero.getPositionX()+hero.getWidth()*0.6-MapPosX),
										(int)(hero.getPositionY()-step+hero.getHeight()*0.9-MapPosY)) &&
						!island.isSolid((int)(hero.getPositionX()-MapPosX+hero.getWidth()*0.25),
										(int)(hero.getPositionY()-step+hero.getHeight()*0.9-MapPosY))){
    				hero.setPositionY(hero.getPositionY()-step);
    			}  else if (!driftUp && !driftRight) {
    				float dia = (float) (Math.sqrt(2*(step*step))/2);
    				if (hero.getPositionX() <= mWidth-hero.getWidth() && 
    					!island.isSolid((int)(hero.getPositionX()+dia+hero.getWidth()*0.6-MapPosX),
    									(int)(hero.getPositionY()+hero.getHeight()*0.9-MapPosY))) 
    					hero.setPositionX(hero.getPositionX()+dia);
    				if (hero.getPositionY() > 0 && 
    					!island.isSolid((int)(hero.getPositionX()+hero.getWidth()*0.6-MapPosX),
    									(int)(hero.getPositionY()-dia+hero.getHeight()*0.9-MapPosY)) &&
    					!island.isSolid((int)(hero.getPositionX()-MapPosX+hero.getWidth()*0.25),
    									(int)(hero.getPositionY()-dia+hero.getHeight()*0.9-MapPosY)))
    					hero.setPositionY(hero.getPositionY()-dia);    				    				
    				gone = true;
    			}
    		} else if (right && down) {
    			if (driftRight && driftDown) {}
    			else if (driftRight && hero.getPositionY() < mHeight-hero.getHeight() &&
    					!island.isSolid((int)(hero.getPositionX()+hero.getWidth()*0.6-MapPosX),
										(int)(hero.getPositionY()+step+hero.getHeight()*0.9-MapPosY)) &&
						!island.isSolid((int)(hero.getPositionX()-MapPosX+hero.getWidth()*0.25),
										(int)(hero.getPositionY()+step+hero.getHeight()*0.9-MapPosY))) {
    					hero.setPositionY(hero.getPositionY()+step);
    			} else if (driftDown && hero.getPositionX() < mWidth-hero.getWidth() &&
    					!island.isSolid((int)(hero.getPositionX()+hero.getWidth()*0.6+step-MapPosX),
										(int)(hero.getPositionY()+hero.getHeight()*0.9-MapPosY))) {
    				hero.setPositionX(hero.getPositionX()+step);
    			} else {
	    			float dia = (float) (Math.sqrt(2*(step*step))/2);
	    			if (hero.getPositionX() < mWidth-hero.getWidth() &&
	    					!island.isSolid((int)(hero.getPositionX()+dia+hero.getWidth()*0.6-MapPosX),
											(int)(hero.getPositionY()+hero.getHeight()*0.9-MapPosY))) 
	    				hero.setPositionX(hero.getPositionX()+dia);
	    			if (hero.getPositionY() < mHeight-hero.getHeight() &&
	    					!island.isSolid((int)(hero.getPositionX()+hero.getWidth()*0.6-MapPosX),
											(int)(hero.getPositionY()+dia+hero.getHeight()*0.9-MapPosY)) &&
							!island.isSolid((int)(hero.getPositionX()-MapPosX+hero.getWidth()*0.25),
											(int)(hero.getPositionY()+dia+hero.getHeight()*0.9-MapPosY)))
	    				hero.setPositionY(hero.getPositionY()+dia);
	    			gone = true;
	    		}
    		} else if (down && left) {
    			if (driftDown && driftLeft) {}
    			else if (driftDown && hero.getPositionX() > 0 &&
    					!island.isSolid((int)(hero.getPositionX()-step-MapPosX+hero.getWidth()*0.25),
										(int)(hero.getPositionY()+hero.getHeight()*0.9-MapPosY))) {
    				hero.setPositionX(hero.getPositionX()-step);
    				gone = true;
    			} else if (driftLeft && hero.getPositionY() < mHeight-hero.getHeight() &&
    					!island.isSolid((int)(hero.getPositionX()-MapPosX+hero.getWidth()*0.25),
										(int)(hero.getPositionY()+step+hero.getHeight()*0.9-MapPosY)) &&
						!island.isSolid((int)(hero.getPositionX()+hero.getWidth()*0.6-MapPosX),
										(int)(hero.getPositionY()+step+hero.getHeight()*0.9-MapPosY))) {
    				hero.setPositionY(hero.getPositionY()+step);
    				gone = true;
    			} else {
	    			float dia = (float) (Math.sqrt(2*(step*step))/2);
	    			if (hero.getPositionX() > 0 &&
	    					!island.isSolid((int)(hero.getPositionX()-dia-MapPosX+hero.getWidth()*0.25),
											(int)(hero.getPositionY()+hero.getHeight()*0.9-MapPosY)))
	    				hero.setPositionX(hero.getPositionX()-dia);
	    			if (hero.getPositionY() < mHeight-hero.getHeight() &&
	    					!island.isSolid((int)(hero.getPositionX()-MapPosX+hero.getWidth()*0.25),
											(int)(hero.getPositionY()+dia+hero.getHeight()*0.9-MapPosY)) &&
							!island.isSolid((int)(hero.getPositionX()+hero.getWidth()*0.6-MapPosX),
											(int)(hero.getPositionY()+dia+hero.getHeight()*0.9-MapPosY)))
	    				hero.setPositionY(hero.getPositionY()+dia);
	    			gone = true;
    			}
    		} else if (left && up) {
    			if (driftLeft && driftUp) {}
    			else if (driftLeft && hero.getPositionY() > 0 &&
    						!island.isSolid((int)(hero.getPositionX()-MapPosX+hero.getWidth()*0.25),
    										(int)(hero.getPositionY()-step+hero.getHeight()*0.9-MapPosY)) &&
    						!island.isSolid((int)(hero.getPositionX()+hero.getWidth()*0.6-MapPosX),
    										(int)(hero.getPositionY()-step+hero.getHeight()*0.9-MapPosY))) {
    				hero.setPositionY(hero.getPositionY()-step);
    			} else if (driftUp && hero.getPositionX() > 0 &&
    						!island.isSolid((int)(hero.getPositionX()-step-MapPosX+hero.getWidth()*0.25),
											(int)(hero.getPositionY()+hero.getHeight()*0.9-MapPosY))) {
    				hero.setPositionX(hero.getPositionX()-step);
    			} else {
    				float dia = (float) (Math.sqrt(2*(step*step))/2);
    				if (hero.getPositionX() > 0 &&
    						!island.isSolid((int)(hero.getPositionX()-dia-MapPosX+hero.getWidth()*0.25),
											(int)(hero.getPositionY()+hero.getHeight()*0.9-MapPosY)))
    					hero.setPositionX(hero.getPositionX()-dia);
    				if (hero.getPositionY() > 0 &&
    						!island.isSolid((int)(hero.getPositionX()-MapPosX+hero.getWidth()*0.25),
											(int)(hero.getPositionY()-dia+hero.getHeight()*0.9-MapPosY)) &&
							!island.isSolid((int)(hero.getPositionX()+hero.getWidth()*0.6-MapPosX),
											(int)(hero.getPositionY()-dia+hero.getHeight()*0.9-MapPosY)))
    					hero.setPositionY(hero.getPositionY()-dia);
    				gone = true;
    			}
    		} 
    		else if (up && !gone && !driftUp && hero.getPositionY() > 0 && 
    								!island.isSolid((int)(hero.getPositionX()-MapPosX+hero.getWidth()*0.25), (int)(hero.getPositionY()-hero.getStepsize()-MapPosY+hero.getHeight()*0.9)) &&
    								!island.isSolid((int)(hero.getPositionX()-MapPosX+hero.getWidth()*0.6),(int)(hero.getPositionY()-hero.getStepsize()-MapPosY+hero.getHeight()*0.9)))
    		{
    			hero.setPositionY(hero.getPositionY()-hero.getStepsize());
    			hero.setLook(0);
		}
    		else if (right && !gone && !driftRight && hero.getPositionX() < mWidth-hero.getWidth() &&
    								!island.isSolid((int)(hero.getPositionX()+hero.getStepsize()-MapPosX+hero.getWidth()*0.6), (int)(hero.getPositionY()-MapPosY+hero.getHeight()*0.9)))
		{
    			hero.setPositionX(hero.getPositionX()+hero.getStepsize());
    			hero.setLook(2);
		}
    		else if (down && !gone && !driftDown && hero.getPositionY() < mHeight-hero.getHeight() &&
    								!island.isSolid((int)(hero.getPositionX()-MapPosX+hero.getWidth()*0.25), (int)(hero.getPositionY()+hero.getStepsize()-MapPosY+hero.getHeight()*0.9)) &&
    								!island.isSolid((int)(hero.getPositionX()-MapPosX+hero.getWidth()*0.6), (int)(hero.getPositionY()+hero.getStepsize()-MapPosY+hero.getHeight()*0.9)))
    			{
    			hero.setPositionY(hero.getPositionY()+hero.getStepsize());
    			hero.setLook(4);
    			}
    		else if (left && !gone && !driftLeft && hero.getPositionX() > 0 &&
    								!island.isSolid((int)(hero.getPositionX()-hero.getStepsize()-MapPosX+hero.getWidth()*0.25), (int)(hero.getPositionY()-MapPosY+hero.getHeight()*0.9)))
    			{
    			hero.setPositionX(hero.getPositionX()-hero.getStepsize());
    			hero.setLook(6);
    			}
    	}
		
		LinkedList<NPC> npcs = AssetCreator.getNPCs();
		for(int i=0; i<npcs.size(); i++){
			NPC npc = npcs.get(i);
			if((npc.getPositionX()+200 > hero.getPositionX()-MapPosX && npc.getPositionX()-150 < hero.getPositionX()-MapPosX) &&
					(npc.getPositionY()+200 >hero.getPositionY()-MapPosY && npc.getPositionY()-150 < hero.getPositionY()-MapPosY)){
				drawActionButtonFlag = true;
			} 
		}
    }
    
    /**
     * Move the Gumba
     */
	private void moveMob(Mob mob) {
		mob.checkRange(hero,MapPosX,MapPosY);
		if (mob.isHeroInRange()) {
			attackHero(mob);
		} else {
			if (mob.getWalkingCounter() < 32) {
				if (!island.isSolid((int)(mob.getPositionX()+mob.getStepsize()*mob.getDirX()), (int)(mob.getPositionY()+mob.getStepsize()*mob.getDirY()))) {
					mob.setPositionX(mob.getPositionX()+mob.getStepsize()*mob.getDirX());
					mob.setPositionY(mob.getPositionY()+mob.getStepsize()*mob.getDirY());
				}
				mob.setWalkingCounter(mob.getWalkingCounter()+1);
			} else {
				mob.setLook();
				mob.setWalkingCounter(0);
			}
		}
	}
	/**
	 * If the shiny hero is in range, ATTACK HIM!
	 */
    private void attackHero(Mob mob) {
    	boolean check = mob.isDirectionForAttack();
    	int diffX = (int) (mob.getPositionX()+MapPosX-hero.getPositionX());
    	int diffY = (int) (mob.getPositionY()+MapPosY-hero.getPositionY());
    	if (check) {
			if (diffX < 0)
    			mob.setPositionX(mob.getPositionX()+mob.getStepsize());
    		else if (diffX > 0)
    			mob.setPositionX(mob.getPositionX()-mob.getStepsize());
    		mob.setDirectionForAttack(false);
    	} else {
			if (diffY < 0)
    			mob.setPositionY(mob.getPositionY()+mob.getStepsize());
    		else if (diffY > 0)
    			mob.setPositionY(mob.getPositionY()-mob.getStepsize());
    		mob.setDirectionForAttack(true);
    	}
    	if (diffX < 0) diffX = -diffX;
    	if (diffY < 0) diffY = -diffY;
    	if (countFramesForAttackingHero > 100 && (diffX < mob.getHitRange() || diffY < mob.getHitRange())) {
    		hero.setHp(hero.getHp()-mob.getDamage());
    		countFramesForAttackingHero = 0;
    	}
	}
	/********************************* Bisschen aufraeumen *********************************/
	
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
     * Gamethread starten
     */
    private void gameStart() {
        if (animator == null || !running) {
            animator = new Thread(this);
            animator.start();
        }
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
        } catch (InterruptedException ex) {}
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
	
	//do this if Action Button is pushed
	private void pressAction(){
		LinkedList<NPC> npcs = AssetCreator.getNPCs();
		double minDistance = Integer.MAX_VALUE;
		NPC actionNPC = null;
		//Calculate nearest NPC for Action-Button use
		for(int i = 0; i<npcs.size(); i++){
			NPC npc = npcs.get(i);
			if(minDistance > Math.sqrt(Math.pow(npc.getPositionX()-(hero.getPositionX()-MapPosX),2) + Math.pow(npc.getPositionY()-(hero.getPositionY()-MapPosY),2))){
				minDistance = Math.sqrt(Math.pow(npc.getPositionX()-(hero.getPositionX()-MapPosX),2) + Math.pow(npc.getPositionY()-(hero.getPositionY()-MapPosY),2));
				actionNPC = npc;
			}
		}
		conversationText = actionNPC.getText(0);
		drawConversation = true;
	}
	
	private void loadMap(String path /* to .TMX mapfile */){
		state = State.LOADLEVEL;
		AssetCreator.removeAll();
		island = null;
		island = new Map(path);
		AssetCreator.createAssets(island);
		LinkedList<NPC> npcs = AssetCreator.getNPCs();
		for(int i=0; i<npcs.size(); i++){
			NPC npc = npcs.get(i);
			island.declareSolid((int)npc.getPositionX(), (int)npc.getPositionY());
			island.declareSolid((int)npc.getPositionX(), (int)(npc.getPositionY() + npc.getImage().getHeight()));
			island.declareSolid((int)(npc.getPositionX() + npc.getImage().getWidth()), (int)npc.getPositionY());
			island.declareSolid((int)(npc.getPositionX() + npc.getImage().getWidth()), (int)(npc.getPositionY() + npc.getImage().getHeight()));
		}
		state = State.RUN;
	}
	/**
     * Alles moegliche, um Tastatureingaben zu lesen. 
     */
    class GameListener implements KeyListener {
    	public void keyPressed(KeyEvent e) {
    		if (debugMode) {
    			if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
    				debugMode = false;
   			} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
   				debugMode = true;
    		if (turboMode) {
    			if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
    				turboMode = false;
    				hero.setStepsize(hero.getStepsize()/10);
    			}
    		} else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
    			turboMode = true;
    			hero.setStepsize(hero.getStepsize()*10);
    		}
    		
    		if (state == State.RUN) {
    			if(e.getKeyCode() == KeyEvent.VK_R && e.isControlDown()){
    				loadMap("maps/TestMap1.tmx");
    			}
    			if(drawConversation && e.getKeyCode() == KeyEvent.VK_F) {drawConversation = false; } 
    			else if(drawActionButtonFlag && e.getKeyCode() == KeyEvent.VK_F){
    				pressAction();
    			}
	    		switch (e.getKeyCode()) {
	    			case 37: left = true; break;
	    			case 38: up = true; break;
	    			case 39: right = true; break;
	    			case 40: down = true; break;
	    			case KeyEvent.VK_SPACE : 
	    				if(slash){
	    					hero.addStrike();
	    				} else {
	    					initSlash = true; 
	    					hero.sword.setAlpha(40); //resets the angle of the sword
	    				}
	    				break;
	    			case KeyEvent.VK_P: loadMap("maps/map1.tmx"); break;
	    			case KeyEvent.VK_V: 
	    				if(hero.getStamina() >=0.5f && !roll){
	    					roll = true; 
	    					hero.loseStamina(0.5f);
	    					System.out.println(hero.getStamina());
	    				}
	    				break;
	    			case KeyEvent.VK_L: try {
							AudioPlayerInstance bgsound = new AudioPlayerInstance();
							bgsound.playSound("./sounds/NoMercy.wav");
						} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						}
	    				break;
	    				
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
	    			case KeyEvent.VK_SPACE : initSlash = false; break;
	    			//case KeyEvent.VK_SPACE: slash = false; break;
	    			default: break;
	    		}
        	}
		}
		@Override
		public void keyTyped(KeyEvent e) {}
    }
    
    
    public int getMapPosX() {
		return MapPosX;
	}
	public void setMapPosX(int bgPosX) {
		this.MapPosX = bgPosX;
	}
	public int getMapPosY() {
		return MapPosY;
	}
	public void setMapPosY(int mapPosY) {
		MapPosY = mapPosY;
	}
}