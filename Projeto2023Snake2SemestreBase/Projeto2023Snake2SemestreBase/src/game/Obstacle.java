package game;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import environment.LocalBoard;

public class Obstacle extends GameElement {
	
	
	public static final int NUM_MOVES=3;
	static final int OBSTACLE_MOVE_INTERVAL = 400;
	private int remainingMoves=NUM_MOVES;
	private Lock lock=new ReentrantLock();
	
	public Obstacle(Board board2) {
		super(board2);
		doInitialPositioning();
	}
	
	public int getRemainingMoves() {
		lock.lock();//
		try {
			return remainingMoves;
		} finally {
			lock.unlock();
		}	
	}
	
	public void decrementRemainingMoves() {
		lock.lock();//
		try {
			remainingMoves--;
		} finally {
			lock.unlock();
		}	
	}

	//
	@Override
	public void doInitialPositioning() {
		
		pos=board.getRandomPosition();
		System.out.println(this+" is obstacle em: "+ pos+" | classObstacle");
		try {
			board.getCell(pos).setGameElement(this);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//this.setCell(board.getCell(pos));
		//board.getObstacles().add(this);	
		//board.addObstacles(this);
	//	board.setChanged();	

	}
}
