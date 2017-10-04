
public class Node {
	
	private State state;
	private Node parent;
	//private int heuristic;
	private int uVal = 0;
	
	public Node(State state, Node parent) {
		this.state = state;
		this.parent = parent;
	}
	
	public Node (State state) {
		this.state = new State(state);
	}
	
	public Node() {
		this.state.reset();
	}
	
	public State getState() {
		return state;
	}
	
	public void setU(int u) {
		this.uVal = u;
	}
	
	public int getU() {
		return this.uVal;
	}
	
	/*
	public void setHeuristic(int h) {
		this.heuristic = h;
	}
	
	
	public int getHeuristic() {
		return heuristic;
	}
	*/

}
