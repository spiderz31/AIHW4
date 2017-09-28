
public class State {

	private char[][] state;
	private static final width = 6;
	private static final int height = 5;
	
	private static final int NO_OPEN = 0;
	private static final int 2_IN_A_ROW = 1;
	private static final int 3_IN_A_ROW = 2;
	
	public State() {
		state = new char[width][height];
	}
	
	public void reset() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				state[i][j] = ' ';
			}
		}
	}
	
	public void print() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				System.out.print(state[i][j]);
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
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				if (i+2 < width) {
					int horizontalRow = getOpenRow("horizontal", i, j, 'X');
					if (horizontalRow == 3_IN_A_ROW) threeForX += 1;
					else if (horizontalRow == 2_IN_A_ROW) twoForX += 1;
					
					horizontalRow = getOpenRow("horizontal", i, j, 'O');
					if (horizontalRow == 3_IN_A_ROW) threeForO += 1;
					else if (horizontalRow == 2_IN_A_ROW) twoForO += 1;
				}
				if (i+2 < width && j+2 < height) {
					int diagonalRow = getOpenRow("diagonal", i, j, 'X');
					if (diagonalRow == 3_IN_A_ROW) threeForX += 1;
					else if (diagonalRow == 2_IN_A_ROW) twoForX += 1;
					
					diagonalRow = getOpenRow("diagonal", i, j, 'O');
					if (diagonalRow == 3_IN_A_ROW) threeForO += 1;
					else if (diagonalRow == 2_IN_A_ROW) twoForO += 1;
				}
				if (j+2 < height) {
					int verticalRow = getOpenRow("vertical", i, j, 'X');
					if (verticalRow == 3_IN_A_ROW) threeForX += 1;
					else if (verticalRow == 2_IN_A_ROW) twoForX += 1;
					
					verticalRow = getOpenRow("vertical", i, j, 'O');
					if (verticalRow == 3_IN_A_ROW) threeForO += 1;
					else if (verticalRow == 2_IN_A_ROW) twoForO += 1;
				}
			}
		}
		int k = 1;
		if (player == 'O') {
			k = -1;
		}
		return (k*3*threeForX) - (k*3*threeForO) + (k*twoForX) - (k*twoForO);
	}
	
	private getOpenRow(String direction, i, j, player) {
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
		
		if (posm1 == OPEN) {
			if (pos2 == OPEN) {
				return 2_IN_A_ROW;
			}
			if (pos2 == SAME) {
				if (pos3 == OPEN) {
					return 3_IN_A_ROW;
				}
				if (pos3 == SAME) {
					return NO_OPEN;
				}
				if (pos3 == DIFFERENT) {
					return 3_IN_A_ROW;
				}
			}
			if (pos2 == DIFFERENT) {
				return 2_IN_A_ROW;
			}
		}
		if (posm1 == SAME) {
			return NO_OPEN;
		}
		if (posm1 == DIFFERENT) {
			if (pos2 == OPEN) {
				return 2_IN_A_ROW;
			}
			if (pos2 == SAME) {
				if (pos3 == OPEN) {
					return 3_IN_A_ROW;
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
	
	private getNext(int offset, String direction, i, j, player) {
		if (direction.equals("horizontal")) {
			newi = i+offset;
			newj = j;
		}
		else if (direction.equals("diagonal")) {
			newi = i+offset;
			newj = j+offset;
		}
		else if (direction.equals("vertical")) {
			newi = i;
			newj = j+offset;
		}
		
		if (newi < 0 || newi >= width) {
			return DIFFERENT;
		}
		if (newj < 0 || newj >= height) {
			return DIFFERENT;
		}
		else {
			if (state[newi][newj] == ' ') return OPEN;
			else return (state[newi][newj] == player) ? SAME : DIFFERENT;
		}
	}
	
	
}
