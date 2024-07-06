package game;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import environment.Board;
import environment.BoardPosition;
import environment.Cell;
import environment.LocalBoard;

public class Obstacle extends GameElement {
	
	
	public static final int NUM_MOVES=4;
	static final int OBSTACLE_MOVE_INTERVAL = 300;
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


}
