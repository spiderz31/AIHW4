import java.util.ArrayList;
import java.util.Random;

public class State {

	private char[][] board;
	private static final int width = 6;
	private static final int height = 5;
	
	private static final int BLANK = 0;
	private static final int SAME = 1;
	private static final int DIFFERENT = 2;
	
	public State() {
		board = new char[height][width];
		reset();
	}
	
	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}

	public State(char[][] board) {
		this.board = board;
	}
	
	public void reset() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				board[i][j] = ' ';
			}
		}
	}
	
	public void print() {
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				System.out.print(board[i][j]);
				if (j != width-1) System.out.print(" | ");
			}
			System.out.println();
			if (i != height-1) System.out.print("----------------------");
			System.out.println();
		}
	}
	
	public ArrayList<int[]> getBlanks() {
		ArrayList<int[]> blanks = new ArrayList<>();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (board[i][j] == ' ') {
					blanks.add(new int[] {i,j});
				}
			}
		}
		return blanks;
	}
	
	public void newMove(char playerChar, int i, int j) {
		board[i][j] = playerChar;
	}
	
	public char get(int i, int j) {
		return board[i][j];
	}
	
	public int getHeuristic(char player) {
		int threeForX = 0;
		int twoForX = 0;
		int threeForO = 0;
		int twoForO = 0;
		ArrayList<OpenRow> openRows = getOpenrows();
		for (OpenRow or: openRows) {
			if (or.getPlayer() == 'X') {
				if (or.getType() == 2) {
					twoForX++;
				}
				else if (or.getType() == 3) {
					threeForX++;
				}
			}
			else if (or.getPlayer() == 'O') {
				if (or.getType() == 2) {
					twoForO++;
				}
				else if (or.getType() == 3) {
					threeForO++;
				}
			}
		}
		
		/*
		System.out.println("threeForX " + threeForX);
		System.out.println("twoForX " + twoForX);
		System.out.println("threeForO " + threeForO);
		System.out.println("twoForO " + twoForO);
		*/
		
		int k = 1;
		if (player == 'O') {
			k = -1;
		}
		return (k*3*threeForX) - (k*3*threeForO) + (k*twoForX) - (k*twoForO);
	}
	
	public ArrayList<OpenRow> getOpenrows() {
		ArrayList<OpenRow> openRows = new ArrayList<>();
		OpenRow newOpenRow;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (j+2 <= width) {
					newOpenRow = getOpenRow("horizontal", i, j, 'X');
					if (newOpenRow != null) {
						openRows.add(newOpenRow);
					}
					newOpenRow = getOpenRow("horizontal", i, j, 'O');
					if (newOpenRow != null) {
						openRows.add(newOpenRow);
					}
				}
				if (j+2 <= width && i+2 <= height) {
					newOpenRow = getOpenRow("diagonalright", i, j, 'X');
					if (newOpenRow != null) {
						openRows.add(newOpenRow);
					}
					newOpenRow = getOpenRow("diagonalright", i, j, 'O');
					if (newOpenRow != null) {
						openRows.add(newOpenRow);
					}
				}
				if (j-2 > 0 && i+2 <= height) {
					newOpenRow = getOpenRow("diagonalleft", i, j, 'X');
					if (newOpenRow != null) {
						openRows.add(newOpenRow);
					}
					newOpenRow = getOpenRow("diagonalleft", i, j, 'O');
					if (newOpenRow != null) {
						openRows.add(newOpenRow);
					}
				}
				if (i+2 <= height) {
					newOpenRow = getOpenRow("vertical", i, j, 'X');
					if (newOpenRow != null) {
						openRows.add(newOpenRow);
					}
					newOpenRow = getOpenRow("vertical", i, j, 'O');
					if (newOpenRow != null) {
						openRows.add(newOpenRow);
					}
				}
			}
		}
		return openRows;
	}

	private OpenRow getOpenRow(String direction, int i, int j, char player) {
		int pos0 = getNext(0, direction, i, j, player);
		int pos1 = getNext(1, direction, i, j, player);

		if (pos0 != SAME) {
			return null;
		}
		if (pos1 != SAME) {
			return null;
		}
		
		int posm1 = getNext(-1, direction, i, j, player);
		int pos2 = getNext(2, direction, i, j, player);
		int pos3 = getNext(3, direction, i, j, player);

		if (posm1 == BLANK) {
			if (pos2 == BLANK) {
				Random rand = new Random();
				return new OpenRow(player, 2, getCoordinate((rand.nextInt(1) == 0 ? -1 : 2), direction, i, j));
			}
			if (pos2 == SAME) {
				if (pos3 == BLANK) {
					Random rand = new Random();
					return new OpenRow(player, 3, getCoordinate((rand.nextInt(1) == 0 ? -1 : 3), direction, i, j));
				}
				if (pos3 == SAME) {//there is a winner
					return new OpenRow(player, 4, null);
				}
				if (pos3 == DIFFERENT) {
					return new OpenRow(player, 3, getCoordinate(-1, direction, i, j));
				}
			}
			if (pos2 == DIFFERENT) {
				return new OpenRow(player, 2, getCoordinate(-1, direction, i, j));
			}
		}
		if (posm1 == SAME) {
			return null;
		}
		if (posm1 == DIFFERENT) {
			if (pos2 == BLANK) {
				return new OpenRow(player, 2, getCoordinate(2, direction, i, j));
			}
			if (pos2 == SAME) {
				if (pos3 == BLANK) {
					return new OpenRow(player, 3, getCoordinate(3, direction, i, j));
				}
				if (pos3 == SAME) {//there is a winner
					return new OpenRow(player, 4, null);
				}
				if (pos3 == DIFFERENT) {
					return null;
				}
			}
			if (pos2 == DIFFERENT) {
				return null;
			}
		}
		return null;
	}
	
	private int getNext(int offset, String direction, int i, int j, char player) {
		int [] newCoordinates = getCoordinate(offset, direction, i, j);
		int newi = newCoordinates[0];
		int newj = newCoordinates[1];
		
		if (newi < 0 || newi >= height) {
			return DIFFERENT;
		}
		if (newj < 0 || newj >= width) {
			return DIFFERENT;
		}
		else {
			if (board[newi][newj] == ' ') {
				return BLANK;
			}
			else {
				return (board[newi][newj] == player) ? SAME : DIFFERENT;
			}
		}
	}
	
	private int[] getCoordinate(int offset, String direction, int i, int j) {
		int newi=0;
		int newj=0;
		if (direction.equals("horizontal")) {
			newi = i;
			newj = j+offset;
		}
		else if (direction.equals("diagonalright")) {
			newi = i+offset;
			newj = j+offset;
		}
		else if (direction.equals("diagonalleft")) {
			newi = i+offset;
			newj = j-offset;
		}
		else if (direction.equals("vertical")) {
			newi = i+offset;
			newj = j;
		}
		return new int[] {newi, newj};
	}

	public char getWinner() {
		if (getBlanks().isEmpty()) {
			return 'D'; // draw
		}
		ArrayList<OpenRow> openRows = getOpenrows();
		for (OpenRow or: openRows) {
			if (or.getType() == 4) {
				return or.getPlayer();
			}
		}
		return ' ';
	}
	
}
