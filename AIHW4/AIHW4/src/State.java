
public class State {

	private char[][] state = new char[5][6];
	
	public State() {
		
	}
	
	public void reset() {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 6; j++) {
				state[i][j] = ' ';
			}
		}
	}
	
	public void print() {
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 6; j++) {
				System.out.print(state[i][j]);
				if (j != 5) System.out.print(" | ");
			}
			System.out.println();
			if (i != 4) System.out.print("----------------------");
			System.out.println();
		}
	}
	
}
