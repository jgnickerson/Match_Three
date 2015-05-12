//Java Implementation of a Bejewled-like Match-3 game
//CS201 Data Structures
//Authors: Gordon Nickerson & Elizabeth Knox

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


@SuppressWarnings("serial")

class TokenCanvas extends Canvas implements MouseListener {
	
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
	static final Color lgrey = Color.lightGray;
	
	//initializing all the arrays that will hold tokens
	int n = 8;
	protected Token[] row1 = new Token[n];
	protected Token[] row2 = new Token[n];
	protected Token[] row3 = new Token[n];
	protected Token[] row4 = new Token[n];
	protected Token[] row5 = new Token[n];
	protected Token[] row6 = new Token[n];
	protected Token[] row7 = new Token[n];
	protected Token[] row8 = new Token[n];
	// Token[y][x]...coordinate system is backwards;
	protected Token[][] rowArray = {row1, row2, row3, row4, row5, row6, row7, row8};
	
	//instance variables for board/tile size/styling
	protected int size = 50;
	protected int border = 0;
	MatchGame game;
	
	//game variables
	protected int numSelected; //will help us ensure user only selects max two tokens at a time
	protected Token selectedTile;
	protected int score;
	protected int movesRemaining = 30; 
	
	TokenCanvas(MatchGame game) {
		this.game = game;
	}
	
	//creates an 8x8 board filled with tile
	//representation of Token objects with
	//randomized colors
	public void initializeBoard() {
		numSelected = 0;
		score = 0;
		movesRemaining = 30;
		selectedTile = null;
		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++) {
				int randInt = (int)(Math.random() *((4)+1)); //randomize int between 0-5
				rowArray[i][j] = new Token(randInt,j,i);	//use randInt as color of new Token
			}
		}
		if (findMatches()) {
			//re-initializes board if there are any matches found on initialization
			System.out.println("Matches found after initializing. Re-Initializing");
			initializeBoard();
		}
	}

	//cleans board of all three-or-more matches
	public void cleanBoard() {
		while (findMatches()) { //while there are matches on the board
			System.out.println("Match found");
			highlightMatches();  //change .matched state of matched tokens to true
			
			//sleep allows the user to get a visual representation of 
			//the match rather than the board updating instantaneously
			update(getGraphics()); 
			try {Thread.sleep(300);} catch  (InterruptedException e) { }
			
			checkMatches();  //delete all matches
		}
		System.out.println("Board is Clean");
	}

	//changes state of Token.matched to true for matched tokens
	public void highlightMatches() {
		resetMatched(); //ensures that there aren't any other .matched tokens yet
						//this will help ensure that only one match gets deleted at a time
						//to prevent a bug from popping up with the way checkMatches() works
		
		for (int i = 0; i < 8; i++) {  //first check whether there are any horizontal three of a kind
			for (int j = 1; j < 7; j++) {
				Token t1 = rowArray[i][j];
				Token t2 = rowArray[i][j-1];
				Token t3 = rowArray[i][j+1];
				if (t1.color == t2.color && t1.color == t3.color) {
					t1.matched = true;
					t2.matched = true;
					t3.matched = true;
					if (t3.x < 7) { //checks if there are four in a row
						Token t4 = rowArray[t3.y][t3.x+1];
						if (t4.color == t3.color) {
							t4.matched = true;
						}
					} 
					if (t3.x < 6) { //checks if there are five in a row
						Token t4 = rowArray[t3.y][t3.x+1];
						Token t5 = rowArray[t3.y][t3.x+2];
						if (t5.color == t3.color && t4.color == t3.color) {
							t4.matched = true;
							t5.matched = true;
						}
					}
				return; //returns if a match is found so only one match gets erased at a time
				} 
			}
		}	
		for (int i = 1; i < 7; i++) {  //second check whether there are any vertical 3 of a kind
			for (int j = 0; j < 8; j++) {
				Token t1 = rowArray[i][j];
				Token t2 = rowArray[i-1][j];
				Token t3 = rowArray[i+1][j];
				if (t1.color == t2.color && t1.color == t3.color) {
					t1.matched = true;
					t2.matched = true;
					t3.matched = true;
					if (t3.y < 7) { //checks if there are four in a row
						Token t4 = rowArray[t3.y+1][t3.x];
						if (t4.color == t3.color) {
							t4.matched = true;
						}
					} 
					if (t3.y < 6) { //checks if there are five in a row
						Token t4 = rowArray[t3.y+1][t3.x];
						Token t5 = rowArray[t3.y+2][t3.x];
						if (t5.color == t3.color && t4.color == t3.color) {
							t4.matched = true;
							t5.matched = true;
						}
					}
				return; //returns if a match is found so only one match gets erased at a time
				} 
			}
		}
	}
	
	//adds all (Token.matched) to matches ArrayList
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

	//deletes a match contained in matches then calls necessary pushDown function
	public void checkMatches(){
		ArrayList<Token> matches = findHighlightedMatches(); //add matched tokens to an arrayList
		changeScore(matches.size()); //ensures the score gets updated after a match
		if (matches.get(0).y == matches.get(1).y) { //match is HORIZONTAL
			System.out.println("Horizontal Match");
			pushDownAfterHorizontalMatch(matches);
		}
		if (matches.get(0).x == matches.get(1).x) { //match is VERTICAL
			System.out.println("Vertical Match");
			pushDownAfterVerticalMatch(matches);	
		}
	}
	
	//updates the score depending on the number of blocks matched
	public void changeScore(int lengthOfMatch) {
		if (lengthOfMatch == 3)
			this.score += 300;
		else if (lengthOfMatch == 4)
			this.score += 500;
		else 
			this.score += 700;
		//updates the Score label of our applet
		game.Score.setText("Score: " + Integer.toString(this.score));
	}
	
	//returns true if there are any matches on the board
	public boolean findMatches() {
		//first check whether there are any
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
		//second check whether there are any 
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
		//executes if both nested loops above have run
		//and still have not found any matches
		return false;
	}
	
	//resets all Tokens' .matched to false;
	public void resetMatched() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				rowArray[j][i].matched = false;
			}
		}
	}

	//pushes down board Tokens over a horizontally matched set of Tokens
	//randomizes tiles at the top of the board (above the match)
	public void pushDownAfterHorizontalMatch(ArrayList<Token> matches) {
		for (int i = 0; i < matches.size(); i++) {
			int yLevel = matches.get(i).y;
			//update/sleep allows the user to see a visual representation
			//of the blocks sliding down after a match is made
			update(getGraphics());  
			try {Thread.sleep(100);} catch  (InterruptedException e) { }
			
			//then slide down the blocks above the match
			if (yLevel > 0) {
				for (int j = 0; j < yLevel; j++) {
					Token tileToPush = rowArray[yLevel-1-j][matches.get(i).x];
					tileToPush.swapAndRandomize(rowArray[yLevel-j][matches.get(i).x]);
				}
			//handles the case where the horizontal match is at the very top of the board
			} else { 
				Token tileToRandomize = rowArray[yLevel][matches.get(i).x];
				tileToRandomize.randomize();
				
			}	
		}
	}
	
	//pushes down board Tokens over a vertically matched set of Tokens
	//randomizes tiles at the top of the board (above the match)
	public void pushDownAfterVerticalMatch(ArrayList<Token> matches) {
		int yLevel = matches.get(0).y;
		if (matches.get(0).y != 0) { //if vertical match is not at the very top of the board
			for (int i = 0; i < yLevel; i++) {   
				//swaps the tiles directly above a vertical to the location 
				//of the tiles of the match
				Token tileToPush = rowArray[yLevel-i-1][matches.get(0).x];
				tileToPush.swapAndRandomize(rowArray[tileToPush.y + matches.size()][tileToPush.x]);
				//update/sleep give a visual representation of the block falling down
				//though the effect is not as good as for horizontal matches
				//due to the nature of this vertical method
				update(getGraphics());  
				try {Thread.sleep(100);} catch  (InterruptedException e) { }
			}
		}
		
		for (int j = matches.size()-1; j >= 0; j--) {
			//randomizes the remaining tiles above the swapped tiles
			//above the originally swapped tiles
			Token tileToRandomize = rowArray[j][matches.get(0).x]; 
			tileToRandomize.randomize();
		}
	}

	//swaps tile with selectedTile or selects tile
	public void swap(Token tile) {	
		if (numSelected == 0) { //selects the clicked tile if none have yet been selected
			numSelected++;
			selectedTile = tile;
			tile.selected = true;
			System.out.println("Selecting");
			
		} else {
			//condition met if a tile has already been selected previously.
			//attempts to switch the already selected tile (selectedTile)
			//with the newly clicked tile (tile)
			//whether swap fails (due to adjacency issues) or not, this.selected 
			//and this.numSelected are reset. 
			if (areAdjacent(selectedTile,tile) && swapResultsInMatch(selectedTile,tile)) { 
				selectedTile.Swap(tile);
				update(getGraphics());  
				try {Thread.sleep(150);} catch  (InterruptedException e) { }
				System.out.println("Swapping");
				System.out.println("decrementing MovesRemaining");
				movesRemaining--;
				game.MovesRemaining.setText("Moves Remaining: " + Integer.toString(movesRemaining));
				cleanBoard(); 
			} 
			//resets selection
			selectedTile.selected = false;
			tile.selected = false;
			selectedTile = null;
			numSelected = 0;
			System.out.println("Resetting Selection");
		}
	}
	
	//returns true if the swap will result in a match
	public Boolean swapResultsInMatch(Token t1, Token t2) {
		t1.Swap(t2); //swaps the Tokens  then switches them back
		if (findMatches()) { //checks if there's a match
			System.out.println("Swap will produce match");
			t1.Swap(t2); //switches tokens back
			return true;
		} else {
			System.out.println("Swap will NOT produce match");
			t1.Swap(t2);
			return false;	
		}
	}
	
	//returns true if the two tiles are adjacent
	public boolean areAdjacent(Token t1, Token t2) {
		return ((Math.abs(t1.x - t2.x) == 1 || Math.abs(t1.y - t2.y) == 1) 
				&& !(Math.abs(t1.x - t2.x) >= 1 && Math.abs(t1.y - t2.y) >= 1));	
	}
	
	public synchronized void paint(Graphics g) {	
		for (int i = 0; i < 8; i++){
			for (int j = 0; j < 8; j++) {
				
				//find the color of the token
				Token tile = rowArray[i][j];
				if (tile.color == 0) 
					g.setColor(cyan);
				if (tile.color == 1)
					g.setColor(red);
				if (tile.color == 2)
					g.setColor(green);
				if (tile.color == 3)
					g.setColor(yellow);
				if (tile.color == 4)
					g.setColor(magenta);
				if (tile.color == 5)
					g.setColor(blue);
				
				//draw square of appropriate color
				int x = j * size + border;
				int y = i * size + border;
				g.fillRect(x, y, size, size);
				
				//outline the token in Orange (for now) if tile is selected
				if (tile.selected) {
					g.setColor(Color.ORANGE);
					g.drawRect(x+5, y+5, size-10, size-10);
				}
				//otherwise outline in black as normal
				if (tile.matched) {
					g.setColor(lgrey);
					g.fillRect(x, y, size, size);
					//g.setColor(lgrey);
					//g.drawRect(x, y, size, size);
				}
				else {
					g.setColor(black);
					g.drawRect(x, y, size, size);
				}
			}
		}
	}
	
	//called when a mouse is pressed somewhere within our applet
	public void mousePressed(MouseEvent event) {
		Point p = event.getPoint();
		int x = p.x - border;
		int y = p.y - border;
		if (movesRemaining <= 0) {
			game.endGame();
			return;
		}
		if (x >= 0 && x < n*size && y >= 0 && y < n*size) { //if (we click on the TokenCanvas)
			//convert actual x & y coordinates
			//to array index corresponding to 
			//token at that (x,y) position
			int k = x / size;
			int l = y / size;
			swap(rowArray[l][k]); //swap tile w/ selectedTile or select tile
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