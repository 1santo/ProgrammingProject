package game;

import environment.Board;
import environment.LocalBoard;

public class ObstacleMover extends Thread {
	private Obstacle obstacle ;
	private Board board ;
	
	//
	public ObstacleMover(Obstacle obstacle, LocalBoard board) {
		super();
		this.obstacle = obstacle;
		this.board = board;
	}
	
	@Override
	public void run() {
		//TODO
	}
}
