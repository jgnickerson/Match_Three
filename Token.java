//Java Implementation of a Bejewled-like Match-3 game
//CS201 Data Structures
//Authors: Gordon Nickerson & Elizabeth Knox

class Token {
	//each Token object is represented by a tile on our tokenCanvas
	
	//instance variables
	int color;
	boolean selected;
	boolean matched;
	int x;
	int y;
	
	//constructor
	Token(int color, int x, int y) {
		this.color = color;
		this.x = x;
		this.y = y;
		selected = false;
		matched = false;
	}
	
	//swaps all values between this and t1
	void Swap(Token t1) {
		Token temp = new Token(t1.color,t1.x,t1.y);
		t1.selected=false;
		t1.matched=false;
		t1.color=this.color;
		this.color=temp.color;
		this.selected=false;
		this.matched=false;	
	}
	
	//t1 becomes this, this.color is randomized, and resets all else
	void swapAndRandomize(Token t1) {
		t1.selected=false;
		t1.matched=false;
		t1.color=this.color;
		this.color=(int)(Math.random() *((4)+1));
		this.selected=false;
		this.matched=false;
	}
	
	//randomizes this.color and resets all else
	void randomize() {
		this.color=(int)(Math.random() *((4)+1));
		this.selected=false;
		this.matched=false;
	}
}