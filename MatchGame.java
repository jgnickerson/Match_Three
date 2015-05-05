//Java implementation of a match3 game, based on Bejeweled/CandyCrush
//
//CS201
//
//Gordon Nickerson & Elizabeth Knox
//

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

@SuppressWarnings("serial")

public class MatchGame extends Applet implements ActionListener {
	
	TokenCanvas c;

	Label Moves; 
	Label MatchThree;
	Label Score;

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
	
	public void init () {
		//set overall layout, add buttons, etc.
		//in this init function
		
		//adds a TokenCanvas to the center of our applet
		setLayout(new BorderLayout());
		c = new TokenCanvas();
		c.addMouseListener(c);
		c.initializeBoard(); //not actually randomized yet, but just sets token values to test;
		add("Center", c);

		add("North", makeNorth());
		add("East", makeEast());
		add("South", makeSouth());
		add("West", makeWest());		
		
	}

	public Panel makeNorth(){
		Panel p1 = new Panel();
    	p1.setLayout(new BorderLayout()); //makes a Border Layout
    	p1.setBackground(Color.blue); //sets the background of the whole border to be blue
    	
    	Moves = new Label("Moves Made: 0", Label.RIGHT); //setting this label to be centered to the right
    	MatchThree = new Label("Match Three", Label.LEFT); //setting this label to be centered to the left
    	Label north = new Label(); // makes the north empty in this border layout
    	Label south = new Label(); //makes the south empty in this border layout
    	Label east = new Label(); //makes the east empty in this border layout
    	Label west = new Label(); //makes the west empty of this border layout
    	
    	p1.add("West", west); //adding west to the new panel
    	p1.add("East", east); //adding east to the new panel
    	p1.add("South", south); //adding south to the new panel
    	p1.add("North", north); //adding north to the new panel
    	Moves.setForeground(white); //setting moves to be white
    	MatchThree.setForeground(white); //setting match three to be white
    	p1.add("Center", Moves); //adding Moves to the center portion of the border layout
    	p1.add("West", MatchThree); //adding Match Three to the west of the border layout
    
    	return p1;//returning the panel
	}
	
	public Panel makeEast(){
		//making the east blue
		Panel p1 = new Panel();
		p1.setBackground(Color.blue);
		return p1;
	}
	public Panel makeWest(){
		//making the west blue
		Panel p1 = new Panel();
		p1.setBackground(Color.blue);
		return p1;
	}
	public Panel makeSouth(){
    	Panel p1 = new Panel(); //creates a new panel
    	p1.setLayout(new BorderLayout()); //makes a Border Layout
    	p1.setBackground(Color.blue); //sets the background to be blue

    	Label center = new Label(); //makes the center empty in this border layout
    	Label north = new Label(); // makes the north empty in this border layout
    	Label south = new Label(); //makes the south empty in this border layout
    	Label east = new Label(); //makes the east empty in this border layout
    	Label west = new Label(); //makes the west empty of this border layout
    	
    	
       	Score = new Label("Score: 0", Label.CENTER); //centering the label Score
    	p1.add("Center", makeCenter2()); //calling makeCenter2() method for the center
    	p1.add("West", west); //adding west to the new panel
    	p1.add("East", east); //adding east to the new panel
    	p1.add("South", south); //adding south to the new panel
    	p1.add("North", north); //adding north to the new panel
    	Score.setForeground(white); //setting the Score to be white
    	p1.add("North", Score); //adding Score to the north
    
    	return p1;//returning the panel
	}
	public Panel makeCenter2(){
		//new panel with a grid layout with 2 rows and 2 columns
		Panel p1 = new Panel();
		p1.setLayout(new GridLayout(2,2));
    	p1.add(CButton("New Game", white, blue)); //adds the white button New Game with a blue background
    	p1.add(CButton("High Scores", white, blue)); // adds the white button High Scores with a blue background
    	
    	return p1; //returns the red panel
	}
    protected Button CButton(String s, Color fg, Color bg) {
        Button b = new Button(s); //new Button of string s
        b.setBackground(bg); //background is the BG parameter
        b.setForeground(fg); //foreground is the FG parameter
        b.addActionListener(this); //allows to press a button
        return b; //returns b
    }
    
    public void NewGame(){
		//re-initialize the board when New Game is presed
		c.initializeBoard();
		c.findMatches();
		c.highlightMatches();
		c.repaint();
		Score.setText("Score: 0");
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof Button) {
            String label = ((Button)e.getSource()).getLabel();
            if (label.equals("New Game")){
            	NewGame();
            }
		}
	}
	
}


@SuppressWarnings("serial")

class TokenCanvas extends Canvas implements MouseListener {
	
	//instance variables
	boolean BoardCleanedUp; //true if the board has been checked for existing 3-matches, and then cleaned up
	
	//local color constants
	static final Color black = Color.black;
	static final Color white = Color.white;
	static final Color red = Color.red;
	static final Color green = Color.green;
    static final Color blue = Color.blue;
    static final Color yellow = Color.yellow;
	static final Color magenta = Color.magenta;
	static final Color cyan = Color.cyan;
	static final Color dred = new Color(160, 0, 100);
	static final Color dgreen = new Color(0, 120, 90);
	static final Color dblue = Color.blue.darker();
	
	//initializing all the arrays that will hold tokens
	int n = 8;
	Token[] row1 = new Token[n];
	Token[] row2 = new Token[n];
	Token[] row3 = new Token[n];
	Token[] row4 = new Token[n];
	Token[] row5 = new Token[n];
	Token[] row6 = new Token[n];
	Token[] row7 = new Token[n];
	Token[] row8 = new Token[n];
	// Token[y][x]...coordinate system is backwards;
	Token[][] rowArray = {row1, row2, row3, row4, row5, row6, row7, row8};
	
	//instance variables for board/tile size/styling
	int size = 50;
	int border = 20;
	Image square;
	
	//game variables
	int numSelected = 0; //will help us ensure user only selects max two tokens at a time
	Token selectedTile;

	
	public void initializeBoard() {
		//creates a randomized 8x8 board filled with tile
		//representation of Token objects
		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++) {
				//choosing a random integer between 0-5
				//in order to randomize color 
				int randInt = (int)(Math.random() *((5)+1));
				rowArray[i][j] = new Token(randInt,j,i);	
			}
		}
		if (findMatches()) {
			//re-initializes board if there are any matches found on initialization
			System.out.println("Matches found after initializing. Re-Initializing");
			initializeBoard();
		}
	}
	
	
	//returns true if there are any matches on the board
	public boolean findMatches() {
	
		//first check whether there are any 
		//vertical 3 (or more) of a kind
		for (int i = 1; i < 7; i++) {
			for (int j = 0; j < 8; j++) {
				Token t1 = rowArray[i][j];
				Token t2 = rowArray[i-1][j];
				Token t3 = rowArray[i+1][j];
				if (t1.color == t2.color && t1.color == t3.color) {
					return true;
				}
			}
		}
		//second check whether there are any
		//horizontal three (or more) of a kind
		for (int i = 0; i < 8; i++) {
			for (int j = 1; j < 7; j++) {
				Token t1 = rowArray[i][j];
				Token t2 = rowArray[i][j-1];
				Token t3 = rowArray[i][j+1];
				if (t1.color == t2.color && t1.color == t3.color) {
					return true;
				}
			}
		}
		//executes if both nested loops above have run
		//and still have not found any matches
		return false;
	}
	
	
	//changes state of Token.matched
	public void highlightMatches() {
		
		//first check whether there are any 
		//vertical 3 of a kind
		//sets Token.matched to true if matched
		for (int i = 1; i < 7; i++) {
			for (int j = 0; j < 8; j++) {
				Token t1 = rowArray[i][j];
				Token t2 = rowArray[i-1][j];
				Token t3 = rowArray[i+1][j];
				if (t1.color == t2.color && t1.color == t3.color) {
					t1.matched = true;
					t2.matched = true;
					t3.matched = true;
				} 
			}
		}
		//second check whether there are any
		//horizontal three of a kind
		for (int i = 0; i < 8; i++) {
			for (int j = 1; j < 7; j++) {
				Token t1 = rowArray[i][j];
				Token t2 = rowArray[i][j-1];
				Token t3 = rowArray[i][j+1];
				if (t1.color == t2.color && t1.color == t3.color) {
					t1.matched = true;
					t2.matched = true;
					t3.matched = true;
				} 
			}
		}
	}
	
	public ArrayList<Token> findHighlightedMatches() {
		ArrayList<Token> matches = new ArrayList<Token>();
		for (int i=0; i < 8; i++) {
			for (int j=0; j < 8; j++) {
				Token tile = rowArray[i][j];
				if (tile.matched) {
					matches.add(tile);
				}
			}
		}
		return matches;
	}
	
	//deletes new matches
	public void deleteMatches(ArrayList<Token> matches) {
		for (int i = 0; i < matches.size(); i++) {
			Token tile = matches.get(i);
			rowArray[tile.y][tile.x].deleted = true;
		}
		if (matches.get(0).x == matches.get(1).x) {
		//match is horizontal
			System.out.println("Vertical Match");
		}
		if (matches.get(0).y == matches.get(1).y) {
		//match is vertical
			System.out.println("Horizontal Match");
		}
	}
	
	public void paint(Graphics g) {	
		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++) {
				
				//find the color of the token
				Token tile = rowArray[i][j];
				if (tile.color == 0) {
					//int x = j * size + border;
					//int y = i * size + border;
					//g.drawImage(this.square, x, y, this);
					g.setColor(blue);
				}
					
				if (tile.color == 1)
					g.setColor(red);
				if (tile.color == 2)
					g.setColor(green);
				if (tile.color == 3)
					g.setColor(yellow);
				if (tile.color == 4)
					g.setColor(magenta);
				if (tile.color == 5)
					g.setColor(cyan);
				if (tile.deleted)
					g.setColor(black);
				
				//draw square of appropriate color
				int x = j * size + border;
				int y = i * size + border;
				g.fillRect(x, y, size, size);
				
				//outline the token in PINK (for now) if tile is selected
				if (tile.selected)
					g.setColor(white);
				//otherwise outline in black as normal
				if (tile.matched)
					g.setColor(white);
				else 
					g.setColor(black);
				g.drawRect(x, y, size-1, size-1);
			}
		}
	}
	
	
	public void swap(Token t1, Token t2) {
	//swaps the colors of t1 & t2 if they are adjacent
		if (areAdjacent(t1,t2)) {
			int t1color = t1.color;
			int t2color = t2.color;
			t1.color = t2color;
			t2.color = t1color;
			highlightMatches();
		}
		
		//Below: figuring out how to get matches deleted
		highlightMatches();
		if (findMatches()) {
			deleteMatches(findHighlightedMatches());
			ArrayList<Token> matches = findHighlightedMatches();
			for (int i=0; i < matches.size(); i++) {
				System.out.println(matches.get(i).x);
				System.out.println(matches.get(i).y);
			}
		}
	}
	
	
	public boolean areAdjacent(Token t1, Token t2) {
	//returns true if the two tiles are adjacent
		return ((Math.abs(t1.x - t2.x) == 1 || Math.abs(t1.y - t2.y) == 1) 
				&& !(Math.abs(t1.x - t2.x) >= 1 && Math.abs(t1.y - t2.y) >= 1));	
	}
	
	
	public void mousePressed(MouseEvent event) {
		Point p = event.getPoint();
		int x = p.x - border;
		int y = p.y - border;
		
		if (x >= 0 && x < n*size &&
				y >= 0 && y < n*size) {
			//convert actual x & y coordinates
			//to array index corresponding to 
			//token at that (x,y) position
			int k = x / size;
			int l = y / size;
			Token tile = rowArray[l][k];
			
			if (numSelected == 0) {
				//selects the clicked tile if none have yet been selected
				this.numSelected++;
				selectedTile = tile;
				tile.selected = true;
			} else {
				//condition met if a tile has already been selected previously
				//attempts to switch the already selected tile (this.selected)
				//with the newly clicked tile
				//whether swap fails (due to adjacency issues) or not, this.selected 
				//and this.numSelected are reset. 
				swap(selectedTile, tile);
				selectedTile.selected = false;
				tile.selected = false;
				selectedTile = null;
				numSelected = 0;
			}
		}
        repaint();
	}

	public void mouseClicked(MouseEvent e) {
		
	}

	public void mouseReleased(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent e) {
		
	}

	public void mouseExited(MouseEvent e) {
		
	}
	
}


class Token {
	//each Token object is represented by a tile on our tokenCanvas
	
	//instance variables
	int color;
	boolean selected;
	boolean matched;
	boolean deleted;
	int x;
	int y;
	
	//constructor
	public Token(int color, int x, int y) {
		this.color = color;
		this.x = x;
		this.y = y;
		selected = false;
		matched = false;
		deleted = false;
	}		
}