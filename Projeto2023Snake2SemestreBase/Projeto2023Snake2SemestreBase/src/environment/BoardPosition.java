package environment;

import java.io.Serializable;

public class BoardPosition implements Serializable, Comparable<BoardPosition>{//
	public final int x;
	public final int y;

	public BoardPosition(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	@Override
	public boolean equals(Object obj) {
		BoardPosition other = (BoardPosition) obj;
		return other.x==x && other.y == y;
	}
	
	//calculo da posicao mais perto providenciado
	public double distanceTo(BoardPosition other) {
		double delta_x = y - other.y;
		double delta_y = x - other.x;
		return Math.sqrt(delta_x * delta_x + delta_y * delta_y);
	}

	public BoardPosition getCellAbove() {
		return new BoardPosition(x, y-1);
	}
	public BoardPosition getCellBelow() {
		return new BoardPosition(x, y+1);
	}
	public BoardPosition getCellLeft() {
		return new BoardPosition(x-1, y);
	}
	public BoardPosition getCellRight() {
		return new BoardPosition(x+1, y);
	}
	
	public BoardPosition getCellPos() {
		return new BoardPosition(x, y);
	}
	
	
//
	@Override
	public int compareTo(BoardPosition other) {
		 if (this.x == other.x) { // vejo por x primeiro e dps o y e' que interessa
	            return Integer.compare(this.y, other.y);
	        }
	        return Integer.compare(this.x, other.x);
	}
	
	//
	
	public static BoardPosition fromString(String string) {
		// Remove the parentheses
		string = string.replaceAll("[()]", "");
		String[] parts = string.split(", ");
		int x = Integer.parseInt(parts[0]);
		int y = Integer.parseInt(parts[1]);
		return new BoardPosition(x, y);
	}
	
	
}
