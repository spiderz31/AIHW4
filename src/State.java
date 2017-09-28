
public class State {

	private char[][] board;
	private static final int width = 6;
	private static final int height = 5;
	
	private static final int NO_OPEN = 0;
	private static final int ROW2 = 1;
	private static final int ROW3 = 2;
	
	private static final int BLANK = 3;
	private static final int SAME = 4;
	private static final int DIFFERENT = 5;
	
	public State() {
		board = new char[height][width];
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
	
	public int getHeuristic(char player) {
		int threeForX = 0;
		int twoForX = 0;
		int threeForO = 0;
		int twoForO = 0;
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (j+2 <= width) {
					int horizontalRow = getOpenRow("horizontal", i, j, 'X');
					if (horizontalRow == ROW3) threeForX += 1;
					else if (horizontalRow == ROW2) twoForX += 1;
					
					horizontalRow = getOpenRow("horizontal", i, j, 'O');
					if (horizontalRow == ROW3) threeForO += 1;
					else if (horizontalRow == ROW2) twoForO += 1;
				}
				if (j+2 <= width && i+2 <= height) {
					int diagonalRow = getOpenRow("diagonalright", i, j, 'X');
					if (diagonalRow == ROW3) threeForX += 1;
					else if (diagonalRow == ROW2) twoForX += 1;
					
					diagonalRow = getOpenRow("diagonalright", i, j, 'O');
					if (diagonalRow == ROW3) threeForO += 1;
					else if (diagonalRow == ROW2) twoForO += 1;
				}
				if (j-2 > 0 && i+2 <= height) {
					int diagonalRow = getOpenRow("diagonalleft", i, j, 'X');
					if (diagonalRow == ROW3) threeForX += 1;
					else if (diagonalRow == ROW2) twoForX += 1;
					
					diagonalRow = getOpenRow("diagonalleft", i, j, 'O');
					if (diagonalRow == ROW3) threeForO += 1;
					else if (diagonalRow == ROW2) twoForO += 1;
				}
				if (i+2 <= height) {
					int verticalRow = getOpenRow("vertical", i, j, 'X');
					if (verticalRow == ROW3) threeForX += 1;
					else if (verticalRow == ROW2) twoForX += 1;
					
					verticalRow = getOpenRow("vertical", i, j, 'O');
					if (verticalRow == ROW3) threeForO += 1;
					else if (verticalRow == ROW2) twoForO += 1;
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
	
	private int getOpenRow(String direction, int i, int j, char player) {
		int pos0 = getNext(0, direction, i, j, player);
		int pos1 = getNext(1, direction, i, j, player);

		if (pos0 != SAME) {
			return NO_OPEN;
		}
		if (pos1 != SAME) {
			return NO_OPEN;
		}
		
		int posm1 = getNext(-1, direction, i, j, player);
		int pos2 = getNext(2, direction, i, j, player);
		int pos3 = getNext(3, direction, i, j, player);

		if (posm1 == BLANK) {
			if (pos2 == BLANK) {
				return ROW2;
			}
			if (pos2 == SAME) {
				if (pos3 == BLANK) {
					return ROW3;
				}
				if (pos3 == SAME) {
					return NO_OPEN;
				}
				if (pos3 == DIFFERENT) {
					return ROW3;
				}
			}
			if (pos2 == DIFFERENT) {
				return ROW2;
			}
		}
		if (posm1 == SAME) {
			return NO_OPEN;
		}
		if (posm1 == DIFFERENT) {
			if (pos2 == BLANK) {
				return ROW2;
			}
			if (pos2 == SAME) {
				if (pos3 == BLANK) {
					return ROW3;
				}
				if (pos3 == SAME) {
					return NO_OPEN;
				}
				if (pos3 == DIFFERENT) {
					return NO_OPEN;
				}
			}
			if (pos2 == DIFFERENT) {
				return NO_OPEN;
			}
		}
		return NO_OPEN;
	}
	
	private int getNext(int offset, String direction, int i, int j, char player) {
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
	
	
}
