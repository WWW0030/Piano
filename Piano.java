//importing methods used in the code
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class Piano extends java.applet.Applet implements Runnable, KeyListener {
	Random r = new Random();
	Thread myThread = null;
	//defining offScreen and background as images: offScreen is the virtual screen, background is the background of the game.
	Image offScreen, background;
	Graphics offG;
	//define the music notes used within the game
	AudioClip Bb, BbD, C, CD, Eb, EbD, F, G;
	//the score tracker
	int score = 0;
	//the time, which is used to calculate the position of the tiles
	int time = 0;
	//number of lives left
	int live = 3;
	//a boolean to see if the game has started
	boolean startG = false;
	//a boolean to see if the tile has been clicked
	boolean clicked = false;
	//a boolean to see if the score has been added
	boolean added = false;
	//a int array which will represent the position of the tiles
	//this array will always have 5 tiles in store, and will constantly change
	//each element will contain a value of either 0, 1, 2, or 3, representing the four buttons
	int[] current = new int[5];
	//an int array which tells the game which note to play, each number represent a different note
	int[] note =		   {8, 8, 8, 1, 3, 5, 5, 3,
							1, 1, 1, 3, 1, 1, 1, 1,
							8, 8, 8, 1, 3, 5, 5, 3,
							1, 1, 1, 3, 1, 1, 1, 1,
							1, 1, 1, 1, 1, 1, 8, 1,
							3, 3, 3, 3, 1, 1, 1, 1,
							8, 8, 7, 8, 1, 1, 8, 7,
							6, 6, 6, 7, 6, 6, 6, 6,
							8, 7, 6, 8, 7, 7, 7, 8,
							1, 1, 3, 5, 1, 1, 1, 8,
							7, 7, 8, 1, 7, 8, 6, 4,
							2, 2, 2, 2, 3, 3, 3, 6,
							7, 7, 7, 8, 6, 7, 6, 3,
							1, 1, 1, 1, 0, 0, 0, 0};

	//initalizing values
    public void init() {
		//initalize offscreen
		offScreen = createImage(1920,960);
		offG = offScreen.getGraphics();
		//initalize the notes
		Bb = getAudioClip(getCodeBase(), "Bb.wav");
		BbD = getAudioClip(getCodeBase(), "BbD.wav");
		C = getAudioClip(getCodeBase(), "C.wav");
		CD = getAudioClip(getCodeBase(), "CD.wav");
		Eb = getAudioClip(getCodeBase(), "Eb.wav");
		EbD = getAudioClip(getCodeBase(), "EbD.wav");
		F = getAudioClip(getCodeBase(), "F.wav");
		G = getAudioClip(getCodeBase(), "G.wav");
		//initalize background
		background = getImage(getDocumentBase(), "Bg.png");
		//define media tracker, which makes sure that the image loads
		MediaTracker tracker = new MediaTracker(this);
		tracker.addImage(background, 0);
		while(tracker.checkAll(true) != true){ }
		if (tracker.isErrorAny()){
			System.out.println("OOPS");
		}
		//draws the background
		offG.drawImage(background, 0, 0, this);
		//draws the board, which is a black square
		offG.fillRect(480, 0, 960, 960);
		addKeyListener(this);

    }
    public void keyPressed(KeyEvent e) {
    	//define an instant variable c to be used as the input
    	char c = e.getKeyChar();
    	//define another instant variable, which keeps track of which note to play
    	int cnote = note[(score+3-live)%112];
    	//if 'g' is pressed, the game will start
    	if (c=='g') {
    		startG = true;
    	}
    	//the following code runs to see if the correct button is clicked.
    	//If so, the score is added and the button is changed to clicked to avoid the user clicking it again
    	else if (c=='d') {
    		if (current[0]==0) {
    			if (!added) {
    				added=true;
    				noteplay(cnote);
    				score++;
    			}
    			clicked = true;
    		}
    	}
    	else if (c=='f') {
    		if (current[0]==1) {
    			if (!added) {
    				added=true;
    				noteplay(cnote);
    				score++;
    			}
    			clicked = true;
    		}
    	}
    	else if (c=='j') {
    		if (current[0]==2) {
    			if (!added) {
    				added=true;
    				noteplay(cnote);
    				score++;
    			}
    			clicked = true;
    		}
    	}
    	else if (c=='k') {
    		if (current[0]==3) {
    			if (!added) {
    				added=true;
    				noteplay(cnote);
    				score++;
    			}
    			clicked = true;
    		}
    	}
    }
    public void keyReleased(KeyEvent e){}
    public void keyTyped(KeyEvent e) {
    }
	//the paint method to display the virtual screen
    public void paint(Graphics g) {
		g.drawImage(offScreen, 0, 0, this);

    }
	//for threading
    public void start() {
    	if (myThread==null) {
    		myThread = new Thread(this);
    		myThread.start();
    	}
    }
    public void stop() {
    	if (myThread != null) {
    		myThread = null;
    	}
    }
    //a method to avoid screen flashing
    public void update(Graphics g) {
    	paint(g);
    }
    //the main code
    public void run() {
		//call the initalQ to create a tile array
    	initalQ(current);
    	//a while loop which runs until g is pressed
    	while(!startG) {
    		//draw the background and so forth
			offG.drawImage(background, 0,0, this);
    		offG.setColor(Color.black);
			offG.fillRect(480, 0, 960, 960);
			//call the prinQ to printout the current array
			printQ(current, time%300);
    	}
    	//a while loop which runs when the game is playing
    	while (live!=0 && startG) {
    		//draws the background and board
    		offG.drawImage(background, 0,0, this);
    		offG.setColor(Color.black);
			offG.fillRect(480, 0, 960, 960);
			offG.setColor(Color.red);
			//display the score
			offG.setFont(new Font("TimesRoman", Font.ITALIC, 32));
			offG.drawString("Current Score: "+score, 10, 50);
			//print out the current array
			printQ(current, time%300);
			//add to the time
			time=time+3;
			//if time goes beyond a certain threshold, it means the tile has reached the bottom
			//the time resets, and the next tile is up
			if (time>=300) {
				//if the tile wasn't clicked, it means it was missed
				if (!clicked) {
					live--;
				}
				//reset clicked and time
				clicked = false;
				time = time-300;
				//call the updateQ method to update the current array
				current = updateQ(current);
			}
			//repaint the screen
			repaint();
    	try {
				Thread.sleep(2);
			}catch(Exception e) {}

    	}
    	//this runs after the game is over
    	//draws the background and display the end screen.
    	offG.drawImage(background, 0,0, this);
    	offG.setFont(new Font("TimesRoman", Font.ITALIC, 32));
		offG.drawString("Nice Try! Your Score: "+score, 10, 50);
		repaint();
    	stop();
    }
    //the methods used to manipulate current, the array which represent the position of the tiles
    //note here, Q stands for queue, the sequence of array which is suppose to be pressed.
    //initalQ creates a completely new array
	public int[] initalQ(int[] x) {
		for (int i=0; i<x.length; i++) {
			x[i] = r.nextInt(4);
		}
		return x;
	}
	//updateQ moves every element forward one spot, and creates a new, random last element
	public int[] updateQ(int[] x) {
		for (int i=0; i<x.length-1; i++) {
			x[i] = x[i+1];
		}
		x[x.length-1] = r.nextInt(4);
		added = false;
		return x;
	}
	//print out the array, with given time
	//time plays an important role, since it changes with the game's time, it will determine how far down the tiles are, or if they reached the bottom
	public void printQ(int[] x, int time) {
		offG.setColor(Color.white);
		//changes the color of the first element based on if it is clicked or not
		if (clicked) {
			offG.setColor(Color.red);
			offG.fillRect(480+x[0]*240, 720+time, 240, 300);
		}
		else {
			offG.setColor(Color.yellow);
			offG.fillRect(480+x[0]*240, 720+time, 240, 300);
		}
		//draws the rest of the elements, and their color corrosponds with the variables score, live, and their spot
		//this make sure that in the player's eyes, the same tile will always have the same color
		for (int i=1; i<x.length; i++) {
				if (clicked) {
				if ((score+live+i)%2==0) {
					offG.setColor(Color.green);
				}
				else {
					offG.setColor(Color.blue);
				}
				}
				else {
				if ((score+live+i+1)%2==0) {
					offG.setColor(Color.green);
				}
				else {
					offG.setColor(Color.blue);
				}
				}
			//draws the tiles based on their assigned value
			if (x[i]==0) {
				offG.fillRect(480, 720-300*i+time, 240, 300);
			}
			if (x[i]==1) {
				offG.fillRect(720, 720-300*i+time, 240, 300);
			}
			if (x[i]==2) {
				offG.fillRect(960, 720-300*i+time, 240, 300);
			}
			if (x[i]==3) {
				offG.fillRect(1200, 720-300*i+time, 240, 300);
			}
		}
	}
	//a method which decides which note to play
	//it takes in an int input, which is already calculated and decide which note to play base off it.
	public void noteplay(int i) {
		if (i==1) {
			Bb.play();
		}
		else if (i==2) {
			BbD.play();
		}
		else if (i==3) {
			C.play();
		}
		else if (i==4) {
			CD.play();
		}
		else if (i==5) {
			Eb.play();
		}
		else if (i==6) {
			EbD.play();
		}
		else if (i==7) {
			F.play();
		}
		else if (i==8) {
			G.play();
		}
	}


}