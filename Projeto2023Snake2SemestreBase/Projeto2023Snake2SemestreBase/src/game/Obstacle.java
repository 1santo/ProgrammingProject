package game;

import environment.Board;
import environment.BoardPosition;
import environment.LocalBoard;

public class Obstacle extends GameElement {
	
	
	private static final int NUM_MOVES=3;
	static final int OBSTACLE_MOVE_INTERVAL = 400;
	private int remainingMoves=NUM_MOVES;
	private Board board;
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
		
		BoardPosition pos=board.getRandomPosition();
		try {
			board.getCell(pos).setGameElement(this);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//this.setCell(board.getCell(pos));
		//board.getObstacles().add(this);
		System.out.println("obst: "+this);	
		board.addObstacles(this);
		board.setChanged();	

	}
	

}
