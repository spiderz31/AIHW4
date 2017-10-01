import java.util.ArrayList;
import java.util.HashMap;
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
		//play(BEGINNER, HUMAN);
		play(ADVANCED, BEGINNER);
		
	}

	/*
	 * player 1 is always 'X'
	 */
	private static void play(int player1, int player2) {
		State s = new State();
		char winnerChar = ' ';
		while (winnerChar == ' ') {
			move(player1, 1, s);
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
	
	
	private static int[] minimax(State s, int depth, char player) {
		char opponent = (player == 'X' ? 'O' : 'X');
		char current = (player == 'X' ? 'X' : 'O');
		int bestVal = 0;
		ArrayList<int[]> possibleMoves = s.getBlanks();
		if(possibleMoves.isEmpty() || depth == 0) {
			bestVal = s.getHeuristic(current);
		}
		
		// Max of the result of doing minimize on resultant state of performing action on given state
		ArrayList<State> newPositions = new ArrayList<>();
		for (int i = 0; i < possibleMoves.size(); i++) {
			State newstate = new State(s);
			newstate.newMove(player, possibleMoves.get(i)[0], possibleMoves.get(i)[1]);				
			newPositions.add(newstate);
		}
		// Have all new states to explore
		HashMap<int[], Integer> map = new HashMap<>();
		ArrayList<Integer> highValues = new ArrayList<>();
		int maxVal = Integer.MIN_VALUE;
		
		for (int i = 0; i < newPositions.size(); i++) {
			int temp = minimize(newPositions.get(i), depth-1, opponent);
			if (temp >= maxVal) {
				if (temp > maxVal)  {
					highValues.clear();
					map.clear();
				}
				map.put(possibleMoves.get(i), temp);
				maxVal = temp;
				highValues.add(temp);
			}
		}
		
		// Now have an ArrayList of best heuristics to choose
		// Now have HashMap of moves made -> tied largest values
		// Select randomly from largest values the key we need
		
		ArrayList<int[]> keyArray = new ArrayList<>(map.keySet());
		Random r = new Random();
		return keyArray.get(r.nextInt(keyArray.size()));
	}
	
	private static int maximize(State s, int depth, char player) {
		char opponent = (player == 'X' ? 'O' : 'X');
		int bestVal = Integer.MIN_VALUE;
		ArrayList<State> moves = expand(s, player);
		if(moves.isEmpty() || depth == 0) {
			bestVal = s.getHeuristic(player);
			return bestVal;
		}
		for (State move : moves) {
			bestVal = Math.max(bestVal, minimize(s, depth-1, opponent));
		}
		return bestVal;
	}
	
	private static int minimize(State s, int depth, char player) {
		char opponent = (player == 'X' ? 'O' : 'X');
		int bestVal = Integer.MAX_VALUE;
		ArrayList<State> moves = expand(s, player);
		if(moves.isEmpty() || depth == 0) {
			bestVal = s.getHeuristic(opponent);
			return bestVal;
		}
		for (State move : moves) {
			bestVal = Math.min(bestVal, maximize(s, depth-1, opponent));
		}
		return bestVal;
	}
	
	// Returns a set of successors
	public static ArrayList<State> expand(State state, char player) {
		ArrayList<State> successors = new ArrayList<>();
		ArrayList<int[]> blanks = state.getBlanks();
		for (int i = 0; i < blanks.size(); i++) {
			int[] coordinates = blanks.get(i);
			State newState = new State(state);							// Create a new state from the current one
			newState.newMove(player, coordinates[0], coordinates[1]);	// Assign a new move
			successors.add(newState);									// Add to list
		}
		return successors;												// Return populated list
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
				int[] move = minimax(s, 2, playerChar);
				System.out.println("PLAYER " + playerNumber + " (" + playerChar + ") TURN: ADVANCED");
				s.newMove(playerChar, move[0], move[1]);
				s.print();
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
		ArrayList<OpenRow> openRows = s.getOpenrows();
		for (OpenRow or: openRows) {
			if (or.getType() == 3) {
				return or.getBlankPosition();
			}
		}
		Random rand = new Random();
		ArrayList<int[]> blanks = s.getBlanks();
		return blanks.get(rand.nextInt(blanks.size()));
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
	 * 
	 */
	
	
	
}
