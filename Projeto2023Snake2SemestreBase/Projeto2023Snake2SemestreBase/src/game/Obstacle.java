package game;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import environment.LocalBoard;

public class Obstacle extends GameElement {
	
	
	public static final int NUM_MOVES=3;
	static final int OBSTACLE_MOVE_INTERVAL = 400;
	private int remainingMoves=NUM_MOVES;
	private Board board;
	private BoardPosition pos;//
	
	public Obstacle(Board board2) {
		super();
		this.board = board2;
	}
	
	public int getRemainingMoves() {
		synchronized (this) {//
			return remainingMoves;
		}
	}
	public void decrementRemainingMoves() {
		synchronized (this) {//
			remainingMoves--;
		}
	}

	//
public void doInitialPositioning() {
		
		pos=board.getRandomPosition();
		System.out.println(this+" is obstacle em: "+ pos);
		try {
			board.getCell(pos).setGameElement(this);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//this.setCell(board.getCell(pos));
		//board.getObstacles().add(this);	
		board.addObstacles(this);
		board.setChanged();	

	}




//
public BoardPosition getPos() {
	return pos;
}

public void setPos(BoardPosition newPos) {
	pos=newPos;
}

}
