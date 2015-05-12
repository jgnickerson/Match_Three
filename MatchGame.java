//Java Implementation of a Bejewled-like Match-3 game
//CS201 Data Structures
//Authors: Gordon Nickerson & Elizabeth Knox

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")

public class MatchGame extends Applet implements ActionListener {
	
	//local color constants
	static final Color black = Color.black;
	static final Color white = Color.white;
	static final Color red = Color.red;
	static final Color green = Color.green;
    static final Color blue = Color.blue;
    static final Color yellow = Color.yellow;
    static final Color magenta = Color.magenta;
	static final Color dred = new Color(160, 0, 100);
	static final Color dgreen = new Color(0, 120, 90);
	static final Color dblue = Color.blue.darker();
	static final Color lblue = Color.blue.brighter();
	static final Color dgrey = Color.darkGray;
	static final Color lgrey = Color.lightGray;
	
	//instance variables
	TokenCanvas c;

	Label MovesRemaining; 
	Label Title;
	Label Score;
	
	//called once at loading
	public void init () {
		setLayout(new BorderLayout()); //sets overall layout of Applet
	
		Panel canvas = new Panel(); //create a new panel canvas that will have our
									//canvas and a light gray border around it
		canvas.setLayout(new BorderLayout());
		add("Center", canvas);
		c = new TokenCanvas(this); 
		c.addMouseListener(c); //add a mouseListener so user can click things
		c.initializeBoard(); //initialize our TokenCanvas, to set up the gameboard
		
		canvas.add("Center", c);
		canvas.add("North", makeBorder(lgrey)); //gives the canvas a light grey border
		canvas.add("East", makeBorder(lgrey));
		canvas.add("West", makeBorder(lgrey));
		canvas.add("South", makeBorder(lgrey));

		//adds the remaining panels 
		add("North", makeNorth()); //panel contains Title and TimeRemaining labels
		add("East", makeBorder(dgrey)); //simple border panel
		add("West", makeBorder(dgrey)); //simple border panel 
		add("South", makeSouth()); //contains NewGame button and Score label	
	}
	
    //makes norther most panel, which contains title and time remaining labels
	public Panel makeNorth(){
		Panel p1 = new Panel();
    	p1.setLayout(new BorderLayout()); //makes a Border Layout
    	p1.setBackground(dgrey); //sets the background of the border layout
    	
    	//Font color and styling for MovesRemaining and Title labels
    	MovesRemaining = new Label("Moves Remaining: 30", Label.RIGHT); //setting this label to be centered to the right
    	MovesRemaining.setForeground(white); 
    	MovesRemaining.setFont(new Font("Lucida", Font.PLAIN, 15)); 
    	Title = new Label("  Match Three", Label.LEFT); //setting this label to be centered to the left
    	Title.setForeground(white); 
    	Title.setFont(new Font("Lucida", Font.BOLD, 30));
    	
    	//creates empty labels to serve as the border 
    	Label north = new Label(); 
    	Label south = new Label(); 
    	Label east = new Label(); 
    	Label west = new Label(); 
    	
    	//adding empty Panels to our borderLayout to create the border
    	p1.add("West", west); 
    	p1.add("East", east);
    	p1.add("South", south);
    	p1.add("North", north); 	
    	
    	p1.add("Center", MovesRemaining); //adding moves remaining to the center portion of the border layout
    	p1.add("West", Title); //adding Match Three to the west of the border layout
    
    	return p1;
	}
	
	//makes a border panel with the desired color
	public Panel makeBorder(Color color){
		Panel p1 = new Panel();
		p1.setBackground(color);
		return p1;
	}
	
	//makes souther panel, which contains score and newGame button
	public Panel makeSouth(){
    	Panel p1 = new Panel();
    	p1.setLayout(new BorderLayout());
    	p1.setBackground(dgrey); 
    	
    	//creates empty labels to serve as the border 
    	Label south = new Label(); 
    	Label east = new Label(); 
    	Label west = new Label();
    	p1.add("West", west);
    	p1.add("East", east);
    	p1.add("South", south);
    	
    	//font and styling for Score label
       	Score = new Label("Score: 0", Label.CENTER); //centering the label Score
       	Score.setForeground(white); //setting the Score to be white
       	Score.setFont(new Font("Lucida", Font.PLAIN, 30));
       	p1.add("North", Score); //adding north to the new panel

       	//creates the New Game button
    	p1.add("Center", CButton("New Game", black, dgrey)); 
    	
    	return p1;
	}
	
	//helper function to create a button easily
    protected Button CButton(String s, Color fg, Color bg) {
        Button b = new Button(s); //new Button of string s
        b.setBackground(bg); //background is the BG parameter
        b.setForeground(fg); //foreground is the FG parameter
        b.addActionListener(this); //allows to press a button
        return b; //returns b
    }
    
    //called when New Game is pressed to re-initialize the board
    //and reset the score/movesRemaining labels
    public void NewGame() {
		c.initializeBoard();
		c.repaint();
		//resetting font and styling of Score/MoveRemaining labels
		Score.setText("Score: 0");
		Score.setForeground(white);
		MovesRemaining.setText("Moves Remaining: 30");
		MovesRemaining.setForeground(white); //setting moves to be white
    	MovesRemaining.setFont(new Font("Lucida", Font.PLAIN, 15));
	}
    
    //Sets styling of MovesRemaining/Score labels
    //to inform the player when the game is over.
    //Called within TokenCanvas
    public void endGame() {
    	MovesRemaining.setFont(new Font("Lucida", Font.BOLD, 30));
    	MovesRemaining.setForeground(red);
    	MovesRemaining.setText("Game Over!");
    	Score.setForeground(red);
    	
    }
	
    //Executes when a button is pressed (specifically New Game)
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof Button) {
            String label = ((Button)e.getSource()).getLabel();
            if (label.equals("New Game")){
            	NewGame();
            }
		}
	}
	
}
