
public class Node {
	
	private State state;
	private Node parent;
	private int heuristic;
	
	public Node(State state, Node parent) {
		this.state = state;
		this.parent = parent;
	}
	
	public Node (State state) {
		this.state = state;
	}
	
	public Node() {
		this.state.reset();
	}
	
	public State getState() {
		return state;
	}
	
	
	public void setHeuristic(int h) {
		this.heuristic = h;
	}
	
	
	public int getHeuristic() {
		return heuristic;
	}

}
