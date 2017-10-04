import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

//the class where our game loop takes place
public class Game {

	private static Scanner scanner = new Scanner(System.in);
	
	private static final int HUMAN = 0;
	private static final int BEGINNER = 1;
	private static final int ADVANCED = 2;
	private static final int MASTER = 3;
	
	private int mWins = 0;
	private int aWins = 0;
	private int bWins = 0;
	
	
	public void Start() {
		//play(BEGINNER , MASTER);
		//play(ADVANCED , BEGINNER);
		//play(MASTER , ADVANCED);
		tournament();
	}
	
	
	private void tournament() {
		this.mWins = 0;
		this.aWins = 0;
		this.bWins = 0;
		
		for(int i = 0; i < 50; i++) {
			play(BEGINNER , MASTER);
		}
		for(int i = 0; i < 50; i++) {
			play(MASTER , BEGINNER);
		}
		
		System.out.println("Beginner won: " + this.bWins);
		System.out.println("Master won : " + this.mWins);
		resetWins();
	
		for(int i = 0; i < 50; i++) {
			play(BEGINNER , ADVANCED);
		}
		for(int i = 0; i < 50; i++) {
			play(ADVANCED, BEGINNER);
		}
		
		System.out.println("Beginner won: " + this.bWins);
		System.out.println("Advanced won : " + this.mWins);
		resetWins();
		
		for(int i = 0; i < 50; i++) {
			play(ADVANCED , MASTER);
		}
		for(int i = 0; i < 50; i++) {
			play(MASTER , ADVANCED);
		}
		
		System.out.println("Advanced won: " + this.bWins);
		System.out.println("Master won : " + this.mWins);
		resetWins();
	}
	
	
	private void resetWins() {
		this.mWins = 0;
		this.aWins = 0;
		this.bWins = 0;
	}
	
	//Player always plays with X
	private void play(int player1, int player2) {
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
					this.bWins++;
					break;
				case ADVANCED:
					System.out.println("GAME OVER, WINNER: ADVANCED");
					this.aWins++;
					break;
				case MASTER:
					System.out.println("GAME OVER, WINNER: MASTER");
					this.mWins++;
					break;
			}
		}
	}
	
	private void move(int player, int playerNumber, State s) {
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
				Node gameState = new Node(s);
				System.out.println("PLAYER " + playerNumber + " (" + playerChar + ") TURN: MASTER");
				int[] masterMove = GetMasterMove(gameState , playerChar);
				s.newMove(playerChar, masterMove[0], masterMove[1]);
				s.print();
				break;
				
		}
	}

	private int[] getHumanMove(char playerChar, State s) {
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
	
	private int getHumanInput(String type, State s) {
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

	private int[] getBeginnerMove(char playerChar, State s) {
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
	//A method that returns the move of the master AI
	//The master AI uses a 4-ply minimax tree to help it decide what move to make next
	//This should help ensure that it beats the other two AI quite frequently
	private int[] GetMasterMove(Node gameState , char player) {
		
		return Decision(gameState , player , 4);
		
	}
	
	private int[] Decision(Node gameState , char player , int depth) {
		
		int best = Integer.MIN_VALUE;
		int [] move = {0,0};
		int temp = best;
		ArrayList<Node> successors = expand(gameState, player);

		for(Node successor : successors) {
			
			best = Math.max(best, Min(successor, depth-1 , player));
			if (temp < best) {
				move = successor.getState().getLastMove();
				//successor.getState().print();]
			}
			//lookup.put(successor, best);
			temp = best;
			// No matter what side the open 4-in-a-row is on, take it
			ArrayList<OpenRow> openRows = successor.getState().getOpenrows();
			for (OpenRow or: openRows) {
				if (or.getType() == 3) {
					return or.getBlankPosition();
				}
			}
		}
		
		return move;
	}
	
	
	//need max
	private int Max(Node node, int depth , char player) {
		
		int test = terminalTest(node , depth , 1 , player);
		if(test == 0) return node.getState().getHVal();
		if(depth == 0) return node.getState().getHeuristic(player);
		int val = Integer.MIN_VALUE;
		
		ArrayList<Node> nodes = expand(node, player);
		
		for(Node successor : nodes) {
			val = Math.max(val,Min(successor, depth-1 , player));
		}
		
		return val;
		
	}
	
	
	//need min
	
	private int Min(Node node , int depth , char player) {
		
		char opPlayer;
		//we have to swap out the player variable so that we can minimize the opponent
		if(player == 'X') opPlayer = 'O';
		else opPlayer = 'X';
		
		int test = terminalTest(node , depth , 2 , opPlayer);
		if (test == 0) return node.getState().getHVal();
		if (depth == 0) return node.getState().getHeuristic(opPlayer);
		int val = Integer.MAX_VALUE;
		
		ArrayList<Node> nodes = expand(node , opPlayer);
		
		for(Node successor : nodes) {
			val = Math.min(val, Max(successor, depth-1 , opPlayer));
		}
		
		
		return val;
	}
	
	
	
	private int terminalTest(Node node, int depth, int mode, char player) {
		int check = 1;
		if (node.getState().terminalCheck('O') == 1) {
			// -Victory incoming for X-
			check = 0;
			if (mode == 2) {
				// Called from mMin - play from 'O', so assign a LOW heuristic value
				node.getState().setHVal(Integer.MIN_VALUE);
			}
			if (mode == 1) {
				node.getState().setHVal(Integer.MAX_VALUE);
			}
		} else if (node.getState().terminalCheck('X') == 1) {
			// -Victory incoming for O-
			check = 0;
			if (mode == 2) {
				node.getState().setHVal(Integer.MIN_VALUE);
			}
			if (mode == 1) {
				node.getState().setHVal(Integer.MAX_VALUE);
			}
		}
		
		return check;
	}
	
	
	//need expand
	private ArrayList<Node> expand(Node node, char player){
		
		ArrayList<Node> successors = new ArrayList<Node>();
		ArrayList<int[]> blanks = node.getState().getBlanks();
		for(int[] blank : blanks) {
			Node newnode = new Node(node.getState());
			Node successor = getSuccessor(newnode, blank, player);
			successors.add(successor);
			//successor.getState().print();
		}
		
		return successors;
	}
	
	//get successors (if needed)
	private Node getSuccessor(Node node , int[] coords , char player) {
		
		State newState = new State(node.getState());
		Node successor = new Node(newState, node);
		newState.newMove(player, coords[0], coords[1]);
		newState.setLastMove(coords);
		return successor;
		
	}
	
	private OpenRow getOpenThreeRow(char playerChar, State s) {
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
	
	
}
