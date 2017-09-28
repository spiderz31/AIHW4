/*
 * AI Homework #4
 * 
 * 
 * 
 * 
 */




public class Main {
	//This is where it all starts....
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		char testState[][] = {
				{' ', 'O', 'X', 'X', ' ', 'O'}, 
				{'X', 'O', 'O', 'X', 'O', 'O'},
				{'O', 'X', 'X', 'O', ' ', ' '},
				{' ', 'X', ' ', ' ', ' ', ' '},
				{' ', ' ', ' ', 'X', 'X', ' '},
				};
		State s = new State(testState);
		System.out.println(s.getHeuristic('X'));
		/*State s = new State();
		s.reset();
		s.print();*/
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
