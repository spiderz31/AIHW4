/*
 * AI Homework #4
 * Pedro Carrion Castagnola
 * Trevor Levins
 * Joshua Lewis
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
