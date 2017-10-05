/*
 * AI Homework #4
 * Pedro Carrion Castagnola
 * Trevor Levins
 * Joshua Lewis
 */

/*
 * For the calculation of open rows (open 2 in a row and open 3 in a row) we have implemented a class called “OpenRow”. 
 * An OpenRow object has all the information of an open row: 
 * 	The type of the open row: 2 (open 2 in a row) or 3 (open 3 in a row).
 * 	The position of the blank  at one end of the open row. If it has 2 blank ends, it will return one of the two blanks positions at random.
 * 	The player of whom belongs the open row. Could be ‘X’ or ‘O’. 
 */
public class OpenRow {
	private char player;
	private int type; //2 or 3. 4 if there is a winner
	private int blankPosition[];
	
	public OpenRow(char player, int type, int[] blankPosition) {
		this.player = player;
		this.type = type;
		this.blankPosition = blankPosition;
	}

	public char getPlayer() {
		return player;
	}

	public int getType() {
		return type;
	}

	public int[] getBlankPosition() {
		return blankPosition;
	}
}
