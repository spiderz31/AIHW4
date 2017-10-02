import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/*
 * AI Homework #4
 * 
 * 
 * 
 * 
 */

public class Main {
	
	private static Scanner scanner = new Scanner(System.in);
	
	private static final int HUMAN = 0;
	private static final int BEGINNER = 1;
	private static final int ADVANCED = 2;
	private static final int MASTER = 3;
	
	public static void main(String[] args) {
		/*State s = new State();
		s.reset();
		s.print();*/
		/*
		char testState[][] = {
				{' ', 'O', 'X', ' ', ' ', ' '}, 
				{'X', 'O', 'O', 'X', ' ', ' '},
				{'O', 'X', 'X', 'O', ' ', ' '},
				{' ', ' ', ' ', ' ', ' ', ' '},
				{' ', ' ', ' ', ' ', ' ', ' '},
				};
		State s = new State(testState);
		System.out.println(s.getHeuristic('X'));
		*/
		//Begginer vs. Human
		play(BEGINNER, BEGINNER);
	}

	/*
	 * player 1 is always 'X'
	 */
	private static void play(int player1, int player2) {
		State s = new State();
		char winnerChar = ' ';
		while (winnerChar == ' ') {
			move(player1, 1,  s);
			winnerChar = s.getWinner();
			if (winnerChar == ' ') {
				move(player2, 2, s);
				winnerChar = s.getWinner();
			}
		}
		if (winnerChar == 'D') {
			System.out.println("DRAW");
		}
		else {
			int winnerPlayer = winnerChar == 'X' ? player1 : player2;
			switch (winnerPlayer) {
				case HUMAN:
					System.out.println("GAME OVER, WINNER: HUMAN");
					break;
				case BEGINNER:
					System.out.println("GAME OVER, WINNER: BEGINNER");
					break;
				case ADVANCED:
					System.out.println("GAME OVER, WINNER: ADVANCED");
					break;
				case MASTER:
					System.out.println("GAME OVER, WINNER: MASTER");
					break;
			}
		}
	}

	private static void move(int player, int playerNumber, State s) {
		char playerChar = (playerNumber == 1 ? 'X':'O');
		switch (player) {
			case HUMAN:
				System.out.println("PLAYER " + playerNumber + " (" + playerChar + ") TURN: HUMAN");
				int[] humanMove = getHumanMove(playerChar, s);
				s.newMove(playerChar, humanMove[0], humanMove[1]);
				s.print();
				break;
			case BEGINNER:
				System.out.println("PLAYER " + playerNumber + " (" + playerChar + ") TURN: BEGINNER");
				int[] beginnerMove = getBeginnerMove(playerChar, s);
				s.newMove(playerChar, beginnerMove[0], beginnerMove[1]);
				s.print();
				break;
			case ADVANCED:
				
				break;
			case MASTER:
				
				break;
				
		}
	}

	private static int[] getHumanMove(char playerChar, State s) {
		boolean blankSelected = false;
		int row = 0;
		int column = 0;
		while (!blankSelected) {
			row = getHumanInput("ROW", s);
			column = getHumanInput("COLUMN", s);
			if (s.get(row, column) == ' ') {
				blankSelected = true;
			}
			else {
				System.out.println("ROW: " +  row + ", COLUMN: " + column + " IS NOT BLANK");
			}
		}
		return new int[] {row, column};
	}
	
	private static int getHumanInput(String type, State s) {
		int maxInput = type.equals("ROW") ? State.getHeight() : State.getWidth();
		int userInput = -1;
		String userInputText;
		while (userInput < 0 || userInput >= maxInput) {
			System.out.print("TYPE " + type + ":");
			userInputText = scanner.next();
			try {
				userInput = Integer.parseInt(userInputText);
			} catch (NumberFormatException e) {
			    System.out.println("TYPE A NUMBER");
			}
			if (userInput < 0 || userInput >= maxInput) {
				 System.out.println("TYPE A NUMBER INPUT BETWEEN 0 AND " +  (maxInput-1));
			}
		}
		return userInput;
	}

	private static int[] getBeginnerMove(char playerChar, State s) {
		OpenRow openThreeRow = getOpenThreeRow(playerChar, s);
		if (openThreeRow != null) {
			return openThreeRow.getBlankPosition();
		}
		Random rand = new Random();
		ArrayList<int[]> blanks = s.getBlanks();
		return blanks.get(rand.nextInt(blanks.size()));
	}

	private static OpenRow getOpenThreeRow(char playerChar, State s) {
		ArrayList<OpenRow> openRows = s.getOpenrows();
		OpenRow threeInARow = null;
		OpenRow opponentThreeInARow = null;
		for (OpenRow or: openRows) {
			if (or.getPlayer() == playerChar && or.getType() == 3) {
				threeInARow = or;
				break;
			}
			if (or.getPlayer() != playerChar && or.getType() == 3) {
				opponentThreeInARow = or;
			}
		}
		if (threeInARow != null) {
			return threeInARow;
		}
		if (opponentThreeInARow != null) {
			return opponentThreeInARow;
		}
		return null;
	}
	
	
	/*Needs
	 * Classes
	 * Node class
	 * State class 
	 * Game Loop
	 * 
	 * 
	 * Methods
	 * Minimax Search (2Ply and 4Ply) 
	 * Depth First Search
	 * Beginner Move
	 * Intermediate Move
	 * Master Move
	 * Tournament Mode
	 * Board State Print
	 * Heuristic Calculation
	 * Player Move
	 * 
	 * 
	 * 
	 */
	
	
	
}
