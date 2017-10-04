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
		//play(BEGINNER, HUMAN);
		play(ADVANCED, HUMAN);
		
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
	
	// play(advanced, x);
	// I emailed TA/Professor and they said that utility is prioritized over heuristic, 
	// i.e. behave in the same manner as beginner when open-3-in-a-row
	private static int[] mDecision(Node node, int depth, char player) {
		// return the a in Actions(state) maximizing Min-Value(Result(a,state))
		char opponent = (player == 'X' ? 'O' : 'X');
		int best = Integer.MIN_VALUE;
		ArrayList<int[]> moves = new ArrayList<>();
		int[] move = {0,0};
		int temp = best;
		ArrayList<Node> successors = expand(node, player);
		for (Node successor : successors) {
			best = Math.max(best, mMin(successor, depth-1, player));
			if (temp <= best) {
				if (temp < best) {
					moves.clear();
				}
				move = successor.getState().getLastMove();
				moves.add(move);
			}
			temp = best;
			
			OpenRow openThreeRow = getOpenThreeRow(player, successor.getState());
			if (openThreeRow != null) {
				return openThreeRow.getBlankPosition();
			}
			
			ArrayList<OpenRow> openRows = successor.getState().getOpenrows();
			for (OpenRow or: openRows) {
				// This check should be included in terminal test,
				// but it functions correctly here
				if (or.getType() == 4) return successor.getState().getLastMove();
			}
			
		}
		
		// Check if there is a preventable 4-in-a-row for opponent
		ArrayList<Node> oList = expand(node, opponent);
		for (Node oNode : oList) {
			ArrayList<OpenRow> oRows = oNode.getState().getOpenrows();
			for (OpenRow or : oRows) {
				if (or.getType() == 4) return oNode.getState().getLastMove();
			}
		}
		Random r = new Random();
		return moves.get(r.nextInt(moves.size()));
	}
	
	// Maximizing is X
	// Returns the integer value of the max heuristic child
	private static int mMax(Node node, int depth, char player) {
		char opponent = (player == 'X' ? 'O' : 'X');
		int check = terminalTest(node, depth, 1);
		if (check == 0) {
			return node.getU();	// Get utility value: prioritized over heuristic
		}
		
		if (depth == 0) return node.getState().getHeuristic(player);
		int val = Integer.MIN_VALUE;
		
		// For each successor compute max
		ArrayList<Node> successors = expand(node, 'X');
		for (Node successor : successors) {
			val = Math.max(val, mMin(successor, depth-1, opponent));
		}
		return val;
	}
	
	// Minimizing is O
	// Returns the integer value of the min heuristic child
	private static int mMin(Node node, int depth, char player) {
		char opponent = (player == 'X' ? 'O' : 'X');
		int check = terminalTest(node, depth, 2);
		if (check == 0) {
			return node.getU();	// U is the utility value: prioritized over heuristic
		}
		if (depth == 0) return node.getState().getHeuristic(opponent);
		int val = Integer.MAX_VALUE;
		
		// For each successor compute min
		ArrayList<Node> successors = expand(node, 'O');
		for (Node successor : successors) {
			val = Math.min(val, mMax(successor, depth-1, player));
		}
		return val;
	}
	
	// Returns 0 if node is terminal, 1 if node is not
	// Mode = 1 if called from mMax, 2 if called from mMin
	// I might work on this some more
	private static int terminalTest(Node node, int depth, int mode) {
		int check = 1;
		if (node.getState().terminalCheck('X') == 1) {
			// -Victory incoming for X-
			check = 0;
			if (mode == 2) {
				// Called from mMin - play from 'O', so assign a LOW heuristic value
				System.out.println("terminal test for X passed from min");
				node.setU(Integer.MIN_VALUE);
			}
			if (mode == 1) {
				node.setU(Integer.MAX_VALUE);
				//node.getState().setHVal(Integer.MAX_VALUE);
			}
		} else if (node.getState().terminalCheck('O') == 1) {
			// -Victory incoming for O-
			check = 0;
			if (mode == 2) {
				System.out.println("terminal test for O passed from min");
				node.setU(Integer.MIN_VALUE);
				//node.getState().setHVal(Integer.MIN_VALUE);
			}
			if (mode == 1) {
				node.setU(Integer.MAX_VALUE);
				//node.getState().setHVal(Integer.MAX_VALUE);
			}
		} else check = 1;
		
		return check;
	}
	
	// Returns a set of successors
	public static ArrayList<Node> expand(Node node, char player) {
		ArrayList<Node> successors = new ArrayList<>();
		ArrayList<int[]> blanks = node.getState().getBlanks();
		for (int i = 0; i < blanks.size(); i++) {
			int[] coordinates = blanks.get(i);
			Node newnode = new Node(node.getState());
			Node successor = getSuccessor(newnode, coordinates, player);
			successors.add(successor);
			//successor.getState().print();
		}
		return successors;												
	}

	// Function that gets a successor node
	private static Node getSuccessor(Node node, int[] location, char player) {
		State newState = new State(node.getState());
		Node successor = new Node(newState, node);
		newState.newMove(player, location[0], location[1]);
		return successor;
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
				Node current = new Node(s);
				int[] move = mDecision(current, 2, playerChar);
				System.out.println("PLAYER " + playerNumber + " (" + playerChar + ") TURN: ADVANCED");
				s.newMove(playerChar, move[0], move[1]);
				s.print();
				// Drawing in one turn
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
	 * 
	 */
	
	
	
}